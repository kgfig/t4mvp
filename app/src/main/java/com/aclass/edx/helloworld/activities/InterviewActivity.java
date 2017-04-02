package com.aclass.edx.helloworld.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.controllers.InterviewController;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.views.AudioControllerView;

import java.io.IOException;

public class InterviewActivity extends AppCompatActivity implements SurfaceHolder.Callback, AudioControllerView.AudioPlayerControl {

    private static final String TAG = InterviewActivity.class.getSimpleName();

    // Permission stuff
    private static final int REQUEST_PERMISSION_CODE = 200;
    private static final int RECORD_PERMISSION = 0;
    private boolean permissionToRecordGranted = false;

    // Media and interview controllers and status
    private MediaPlayer audioPlayer;
    private MediaRecorder recorder;
    private InterviewController interviewController;
    private int currentPosition = 0;

    // Views
    private AudioControllerView audioControllerView;
    private FrameLayout surfaceViewContainer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView textViewQuestion, textViewQuestionNum;
    private Button buttonRecord;
    private SeekBar seekBarQuestion;

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
            showNextQuestion();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get Interview or initialize controller
        Intent intent = getIntent();
        String paramName = getString(R.string.content_list_selected_content_key);
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

        // Init views
        getSupportActionBar().setTitle(interview.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        surfaceViewContainer = (FrameLayout) findViewById(R.id.interview_framelayout_surfaceview_container);
        surfaceView = (SurfaceView) findViewById(R.id.interview_surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPosition = audioPlayer.getCurrentPosition();
    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioControllerView.stopTracking();
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
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initViews();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void start() {
        if (audioPlayer != null) {
            audioPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (audioPlayer != null) {
            audioPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        if (audioPlayer != null) {
            return audioPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (audioPlayer != null) {
            return audioPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (audioPlayer != null) {
            audioPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if (audioPlayer != null) {
            return audioPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    private final View.OnClickListener recordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (interviewController.hasNext()) {
                if (interviewController.hasAnswerForCurrentQuestion()) {
                    showNextQuestion();
                } else {
                    startRecording();
                }
            } else {
                finishInterview();
            }
        }
    };

    private void initViews() {
        textViewQuestion = (TextView) findViewById(R.id.interview_textview_question);
        textViewQuestionNum = (TextView) findViewById(R.id.interview_textview_questionnum);
        buttonRecord = (Button) findViewById(R.id.interview_button_record);
        seekBarQuestion = (SeekBar) findViewById(R.id.interview_seekbar_question);

        buttonRecord.setOnClickListener(recordClickListener);
        seekBarQuestion.setMax(interviewController.getNumQuestions());
        seekBarQuestion.setProgress(0);

        audioControllerView = new AudioControllerView(this) {
            protected View makeControllerView() {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                root = inflater.inflate(R.layout.view_interview_audio_controller, null);
                initControllerView(
                        root,
                        R.id.interview_audio_controller_button_pause,
                        R.id.interview_audio_controller_seekbar,
                        R.id.interview_audio_controller_textview_currenttime,
                        R.id.interview_audio_controller_textview_duration
                );
                return root;
            }
        };

    }

    /**
     * TODO is it a good idea to instantiate MediaPlayer and MediaRecorder for each question?
     */
    private void showNextQuestion() {
        stopPlayingQuestion();
        stopRecording();

        InterviewQuestion question = interviewController.nextQuestion();
        textViewQuestion.setText(question.getQuestion());
        textViewQuestionNum.setText(String.format("Question %d of %d", interviewController.getCurrentQuestionNo() + 1,
                interviewController.getNumQuestions()));
        seekBarQuestion.setProgress(interviewController.getCurrentQuestionNo());

        try {
            initPlayer(question.getMedia().getFilename());
            initRecorder();
        } catch (IOException e) {
            Log.e(TAG, "Unable to init player or recorder");
            e.printStackTrace();
        }
    }

    private void stopPlayingQuestion() {
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    private void stopRecording() {
        buttonRecord.setText("Record you answer.");
        buttonRecord.setEnabled(false);

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            audioControllerView.setPlayer(InterviewActivity.this);
            audioControllerView.setAnchorView(surfaceViewContainer);
            audioPlayer.start();
            audioControllerView.show();
        }
    };

    private final MediaPlayer.OnErrorListener onPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            buttonRecord.setEnabled(true);
        }
    };

    private void initPlayer(String filename) throws IOException {
        Uri audioUri = Uri.parse("android.resource://" + getPackageName() + "/" +
                getResources().getIdentifier(filename, "raw", getPackageName()));
        audioPlayer = new MediaPlayer();

        audioPlayer.setDisplay(surfaceHolder);
        audioPlayer.setOnPreparedListener(onPreparedListener);
        audioPlayer.setOnCompletionListener(onCompletionListener);
        audioPlayer.setOnErrorListener(onPlayerErrorListener);
        audioPlayer.setDataSource(this, audioUri);
        audioPlayer.prepareAsync();
    }

    private void initRecorder() throws IOException {
        String filename = String.format("%s/%s%s", getExternalCacheDir().getAbsolutePath(),
                interviewController.getAnswerFilename(), getString(R.string.recorder_audio_ext_name));
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filename);
        recorder.prepare();
    }

    private void startRecording() {
        recorder.start();
        buttonRecord.setText("Ongoing recording...");
    }

    private void finishInterview() {
        stopPlayingQuestion();
        stopRecording();
        Toast.makeText(this, "Interview done! TODO: Start playing whole sequence.", Toast.LENGTH_SHORT).show();
    }
}
