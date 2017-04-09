package com.aclass.edx.helloworld.activities;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.controllers.InterviewController;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.fragments.RecordButtonFragment;
import com.aclass.edx.helloworld.utils.PrefUtils;

public class InterviewActivity extends AppCompatActivity implements RecordButtonFragment.OnRecordButtonClickListener {

    private static final String TAG = InterviewActivity.class.getSimpleName();

    // Permission fields
    private static final int REQUEST_PERMISSION_CODE = 200;
    private static final int RECORD_PERMISSION = 0;
    private boolean permissionToRecordGranted = false;
    private String[] activityPermissions = new String[]{Manifest.permission.RECORD_AUDIO};

    // Media and INTERVIEW controllers and status
    private MediaPlayer audioPlayer;
    private InterviewController interviewController;
    private int currentPosition = 0;
    private boolean isRecording = false, isPlayingAnswer = false;

    // Views
    private TextView textViewQuestion, textViewQuestionNum;
    private ImageButton buttonPlay;
    private RecordButtonFragment recordButtonFragment;

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
        if (audioPlayer != null) {
            currentPosition = audioPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (audioPlayer != null) {
            currentPosition = audioPlayer.getCurrentPosition();
            audioPlayer.pause();
            outState.putInt(getString(R.string.media_player_position), currentPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (audioPlayer != null) {
            currentPosition = savedInstanceState.getInt(getString(R.string.media_player_position));
            audioPlayer.seekTo(currentPosition);
            audioPlayer.start();
        }
    };

    @Override
    public void onStopRecording() {
        interviewController.markCurrentQuestionAnswered();

        if (interviewController.hasNext()) {
            showNextQuestion();
        } else {
            finishInterview();
        }
    }

    private void initViews() {
        getSupportActionBar().setTitle(interviewController.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewQuestion = (TextView) findViewById(R.id.interview_textview_question);
        textViewQuestionNum = (TextView) findViewById(R.id.interview_textview_questionnum);
        buttonPlay = (ImageButton) findViewById(R.id.interview_button_play_question);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer != null) {
                    if (!audioPlayer.isPlaying()) {
                        audioPlayer.seekTo(0);
                        audioPlayer.start();
                    }
                }
            }
        });

        recordButtonFragment = RecordButtonFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.interview_btn_answer_container, recordButtonFragment).commit();
    }

    /**
     * TODO is it a good idea to instantiate MediaPlayer and MediaRecorder for each question?
     */
    private void showNextQuestion() {
        stopPlaying();
        // TODO make sure to stop ongoing recording before this point
        // stopRecording();

        InterviewQuestion question = interviewController.nextQuestion();
        textViewQuestion.setText(question.getQuestion());
        textViewQuestionNum.setText(String.format("Question %d of %d", interviewController.getCurrentQuestionNo() + 1,
                interviewController.getNumQuestions()));
        try {
            initPlayer(question.getMedia().getFilename(), null, onPreparedListener, onCompletionListener);
        } catch (IOException e) {
            Log.e(TAG, "Unable to init player or recorder");
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (audioPlayer != null) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            audioPlayer.start();
            buttonPlay.setEnabled(false);
        }
    };

    private final MediaPlayer.OnErrorListener onPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(InterviewActivity.this, "Something went wrong with the player", Toast.LENGTH_LONG).show();
            switch (what){
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e(TAG, "unknown media playback error");
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e(TAG, "server connection died");
                default:
                    Log.e(TAG, "generic audio playback error");
                    break;
            }

            switch (extra){
                case MediaPlayer.MEDIA_ERROR_IO:
                    Log.e(TAG, "IO media error");
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    Log.e(TAG, "media error, malformed");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    Log.e(TAG, "unsupported media content");
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    Log.e(TAG, "media timeout error");
                    break;
                default:
                    Log.e(TAG, "unknown playback error");
                    break;
            }
            return true;
        }
    };

    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                Toast.makeText(InterviewActivity.this, "Finished playing question", Toast.LENGTH_SHORT).show();
                String filename = String.format("%d_%d_%s",
                        interviewController.getInterviewId(),
                        interviewController.getCurrentQuestionId(),
                        PrefUtils.getNickname(InterviewActivity.this));

                interviewController.saveAnswerForCurrentQuestion(filename);
                recordButtonFragment.initializeRecorder(makePathInCacheDir(filename));
                recordButtonFragment.setButtonEnabled(true);
                buttonPlay.setEnabled(true);
            } catch (IOException e) {
                Toast.makeText(InterviewActivity.this, "Failed to initialize recorder", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to initialize recorder");
            }
        }
    };

    private void initPlayer(String filename, SurfaceHolder display, MediaPlayer.OnPreparedListener thisOnPreparedListener,
                            MediaPlayer.OnCompletionListener thisOnCompletionListener) throws IOException {
        Uri audioUri = Uri.parse("android.resource://" + getPackageName() + "/" +
                getResources().getIdentifier(filename, "raw", getPackageName()));
        initPlayer(audioUri, display, thisOnPreparedListener, thisOnCompletionListener);
    }

    private void initPlayer(Uri audioUri, SurfaceHolder display, MediaPlayer.OnPreparedListener thisOnPreparedListener,
                            MediaPlayer.OnCompletionListener thisOnCompletionListener) throws IOException {
        audioPlayer = new MediaPlayer();
        audioPlayer.setOnPreparedListener(thisOnPreparedListener);
        audioPlayer.setOnCompletionListener(thisOnCompletionListener);
        audioPlayer.setOnErrorListener(onPlayerErrorListener);
        audioPlayer.setDataSource(this, audioUri);
        audioPlayer.prepareAsync();
    }

    private final MediaPlayer.OnPreparedListener playbackOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            audioPlayer.start();
        }
    };

    /**
     * After playing the question for this item, prepare the answer for playback.
     * After playing the answer for this item, prepare the next question for playback if there's any.
     * TODO refactor this messy method
     */
    private final MediaPlayer.OnCompletionListener playbackOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                stopPlaying();

                if (isPlayingAnswer) {
                    if (interviewController.hasNext()) {
                        isPlayingAnswer = !isPlayingAnswer;
                        InterviewQuestion question = interviewController.nextQuestion();
                        Toast.makeText(InterviewActivity.this, "Play next question!" + question.getMedia().getFilename(), Toast.LENGTH_LONG).show();
                        initPlayer(question.getMedia().getFilename(), null, playbackOnPreparedListener, playbackOnCompletionListener);
                    } else { // INTERVIEW playback done
                        Toast.makeText(InterviewActivity.this, "Playback done!", Toast.LENGTH_LONG).show();
                        cleanup();
                    }
                } else {
                    Toast.makeText(InterviewActivity.this, "Play answer! " + interviewController.getAnswerFilename(), Toast.LENGTH_LONG).show();
                    isPlayingAnswer = !isPlayingAnswer;
                    Uri audioUri = Uri.parse(makePathInCacheDir(interviewController.getAnswerFilename()));
                    initPlayer(audioUri, null, playbackOnPreparedListener, playbackOnCompletionListener);
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot prepare player for INTERVIEW playback");
                e.printStackTrace();
            }
        }
    };


    private void finishInterview() {
        stopPlaying();
        //TODO make sure recording is done before this method
        //stopRecording();
        Toast.makeText(this, "Interview done! TODO: Start playing whole sequence.", Toast.LENGTH_SHORT).show();

        try {
            interviewController.resetQuestionTracker();
            InterviewQuestion question = interviewController.nextQuestion();
            initPlayer(question.getMedia().getFilename(), null, playbackOnPreparedListener, playbackOnCompletionListener);
        } catch (IOException e) {
            Log.e(TAG, "Cannot initialize player!");
            e.printStackTrace();
        }
    }

    private void cleanup() {
        Collection<String> filenames = interviewController.getAnswerFilenames();
        for (String filename : filenames) {
            File file = new File(makePathInCacheDir(filename));
            if (file.exists()) {
                file.delete();
                Toast.makeText(this, "Delete files!", Toast.LENGTH_SHORT).show();
            }
        }

        File cacheDir = new File(getExternalCacheDir().getAbsolutePath());
        if (cacheDir.isDirectory()) {
            for (File file : cacheDir.listFiles()) {
                file.delete();
                Toast.makeText(this, "Delete files!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String makePathInCacheDir(String filename) {
        return String.format("%s/%s.3gp", getExternalCacheDir().getAbsolutePath(), filename);
    }

}
