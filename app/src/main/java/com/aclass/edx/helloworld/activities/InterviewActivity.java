package com.aclass.edx.helloworld.activities;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.Manifest;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.controllers.InterviewController;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.fragments.PlayAudioButtonFragment;
import com.aclass.edx.helloworld.fragments.RecordButtonFragment;
import com.aclass.edx.helloworld.utils.PrefUtils;

public class InterviewActivity extends AppCompatActivity implements RecordButtonFragment.OnRecordButtonClickListener, PlayAudioButtonFragment.OnPlayButtonClickListener {

    private static final String TAG = InterviewActivity.class.getSimpleName();

    private static final int PRACTICE_QUESTION = 1;
    private static final int PLAYBACK_QUESTION = 2;
    private static final int PLAYBACK_ANSWER = 3;

    // Permission fields
    private static final int REQUEST_PERMISSION_CODE = 200;
    private static final int RECORD_PERMISSION = 0;
    private boolean permissionToRecordGranted = false;
    private String[] activityPermissions = new String[]{Manifest.permission.RECORD_AUDIO};

    // Media and INTERVIEW controllers and status
    private InterviewController interviewController;
    private boolean isPlayingAnswer = false;

    // Views
    private TextView textViewQuestion, textViewQuestionNum;
    private RecordButtonFragment recordButtonFragment;
    private PlayAudioButtonFragment playAnswerFragment, playQuestionFragment;
    private Button nextQuestion, prevQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get Interview or initialize controller
        Intent intent = getIntent();
        String paramName = getString(R.string.content_list_selected_video_key);
        Interview interview = intent.getParcelableExtra(paramName);
        if (interview == null) {
            Cursor cursor = getContentResolver().query(
                    Uri.parse(AppContract.InterviewEntry.CONTENT_URI + "/" + 1),
                    AppContract.InterviewEntry.ALL_COLUMN_NAMES,
                    null,
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                interview = new Interview();
                interview.setValues(cursor);
            }
        }

        interviewController = new InterviewController(this, interview);
        // TODO remove this
        cleanup();
        requestPermissions();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, activityPermissions, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length >= 1) {
                    permissionToRecordGranted = grantResults[RECORD_PERMISSION] == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }

        if (permissionToRecordGranted) {
            initViews();
            showNextQuestion();
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        playAnswerFragment.release();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    ;

    @Override
    public void onStopRecording() {
        interviewController.markCurrentQuestionAnswered();

        if (interviewController.hasNext()) {
            showNextQuestion();
        } else {
            finishInterview();
        }
    }

    @Override
    public void onAudioPlayerRelease(int customType) {
        try {
            if (isPlayingAnswer && customType == PLAYBACK_ANSWER) {
                // do nothing when finished playing answer
            } else {
                if (!isPlayingAnswer && customType == PLAYBACK_QUESTION) {
                    // automatically play answer after the question (part 2)
                    Toast.makeText(InterviewActivity.this, "Play answer! " + interviewController.getAnswerFilename(), Toast.LENGTH_LONG).show();
                    isPlayingAnswer = !isPlayingAnswer;
                    Uri audioUri = Uri.parse(makePathInCacheDir(interviewController.getAnswerFilename()));
                    playAnswerFragment.initializePlayer(audioUri);
                }

                if (customType == PRACTICE_QUESTION) {
                    Toast.makeText(InterviewActivity.this, "Finished playing question", Toast.LENGTH_SHORT).show();
                    String filename = String.format("%d_%d_%s",
                            interviewController.getInterviewId(),
                            interviewController.getCurrentQuestionId(),
                            PrefUtils.getNickname(InterviewActivity.this));

                    interviewController.saveAnswerForCurrentQuestion(filename);
                    recordButtonFragment.initializeRecorder(makePathInCacheDir(filename));
                    recordButtonFragment.setButtonEnabled(true);
                    playQuestionFragment.setButtonEnabled(true);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize player");
            e.printStackTrace();
        }
    }

    private final View.OnClickListener onClickNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (interviewController.hasNext()) {
                isPlayingAnswer = !isPlayingAnswer;
                showNextQuestion();
            } else { // INTERVIEW playback done
                playAnswerFragment.setButtonEnabled(false);
                playQuestionFragment.setButtonEnabled(false);
                prevQuestion.setEnabled(false);
                nextQuestion.setEnabled(false);
                Toast.makeText(InterviewActivity.this, "Playback done!", Toast.LENGTH_LONG).show();
                cleanup();
            }
        }
    };

    private void initViews() {
        getSupportActionBar().setTitle(interviewController.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewQuestion = (TextView) findViewById(R.id.interview_textview_question);
        textViewQuestionNum = (TextView) findViewById(R.id.interview_textview_questionnum);
        nextQuestion = (Button) findViewById(R.id.interview_playback_btn_next);
        prevQuestion = (Button) findViewById(R.id.interview_playback_btn_prev);
        playQuestionFragment = PlayAudioButtonFragment.newInstance(PRACTICE_QUESTION);
        recordButtonFragment = RecordButtonFragment.newInstance();
        prevQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InterviewActivity.this, "DO NOTHING", Toast.LENGTH_SHORT).show();
            }
        });
        nextQuestion.setOnClickListener(onClickNextListener);
        prevQuestion.setEnabled(false);
        nextQuestion.setEnabled(false);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.interview_btn_question_container, playQuestionFragment)
                .add(R.id.interview_btn_answer_container, recordButtonFragment).commit();
    }

    /**
     * TODO is it a good idea to instantiate MediaPlayer and MediaRecorder for each question?
     */
    private void showNextQuestion() {
        recordButtonFragment.setButtonEnabled(false);

        InterviewQuestion question = interviewController.nextQuestion();
        textViewQuestion.setText(question.getQuestion());
        textViewQuestionNum.setText(String.format("Question %d of %d", interviewController.getCurrentQuestionNo() + 1,
                interviewController.getNumQuestions()));

        try {
            Uri uri = getInternalAudioUri(question.getMedia().getFilename());
            Log.d(TAG, "Uri: " + uri);
            playQuestionFragment.release();
            playQuestionFragment.initializePlayer(this, uri);

            if (playAnswerFragment != null) {
                playAnswerFragment.setButtonEnabled(false);
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to init player or recorder");
            e.printStackTrace();
        }
    }

    private void finishInterview() {
        Toast.makeText(this, "Interview done! TODO: Start playback.", Toast.LENGTH_SHORT).show();

        prevQuestion.setEnabled(true);
        nextQuestion.setEnabled(true);
        playQuestionFragment.setCustomType(PLAYBACK_QUESTION);

        playAnswerFragment = PlayAudioButtonFragment.newInstance(PLAYBACK_ANSWER);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.interview_btn_answer_container, playAnswerFragment).commit();

        interviewController.resetQuestionTracker();
        showNextQuestion();
    }

    private void cleanup() {
        Collection<String> filenames = interviewController.getAnswerFilenames();
        for (String filename : filenames) {
            File file = new File(makePathInCacheDir(filename));
            if (file.exists()) {
                file.delete();
            }
        }

        File cacheDir = new File(getExternalCacheDir().getAbsolutePath());
        if (cacheDir.isDirectory()) {
            for (File file : cacheDir.listFiles()) {
                file.delete();
            }
        }

        Toast.makeText(this, "Deleted files!", Toast.LENGTH_SHORT).show();
    }

    private String makePathInCacheDir(String filename) {
        return String.format("%s/%s.3gp", getExternalCacheDir().getAbsolutePath(), filename);
    }

    private Uri getInternalAudioUri(String filename) {
        return Uri.parse("android.resource://" + getPackageName() + "/" +
                getResources().getIdentifier(filename, "raw", getPackageName()));
    }
}
