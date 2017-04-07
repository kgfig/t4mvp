package com.aclass.edx.helloworld.activities;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.Manifest;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.controllers.InterviewController;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.utils.PrefUtils;
import com.aclass.edx.helloworld.views.AudioControllerView;

public class InterviewActivity extends AppCompatActivity implements SurfaceHolder.Callback, AudioControllerView.AudioPlayerControl {

    private static final String TAG = InterviewActivity.class.getSimpleName();

    // Permission fields
    private static final int REQUEST_PERMISSION_CODE = 200;
    private static final int RECORD_PERMISSION = 0;
    private boolean permissionToRecordGranted = false;
    private String[] activityPermissions = new String[]{Manifest.permission.RECORD_AUDIO};

    // Media and interview controllers and status
    private MediaPlayer audioPlayer;
    private MediaRecorder recorder;
    private InterviewController interviewController;
    private int currentPosition = 0;
    private boolean isRecording = false, isPlayingAnswer = false;

    // Views
//    private ViewGroup surfaceViewContainer;
//    private SurfaceView surfaceView;
//    private SurfaceHolder surfaceHolder;
    private TextView textViewQuestion, textViewQuestionNum;
    private Button buttonRecord;
    private ImageButton buttonPlay;

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
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        showNextQuestion();
        Toast.makeText(this, "Show first question", Toast.LENGTH_SHORT).show();
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
            if (isRecording) {
                isRecording = false;
                stopRecording();
                interviewController.markCurrentQuestionAnswered();

                if (interviewController.hasNext()) {
                    showNextQuestion();
                } else {
                    finishInterview();
                }
            } else {
                isRecording = true;
                startRecording();
            }
        }
    };

    private void initViews() {
        getSupportActionBar().setTitle(interviewController.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
        surfaceViewContainer = (FrameLayout) findViewById(R.id.interview_framelayout_surfaceview_container);
        surfaceView = (SurfaceView) findViewById(R.id.interview_surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        */

        textViewQuestion = (TextView) findViewById(R.id.interview_textview_question);
        textViewQuestionNum = (TextView) findViewById(R.id.interview_textview_questionnum);
        buttonRecord = (Button) findViewById(R.id.interview_button_record);
        buttonPlay = (ImageButton) findViewById(R.id.interview_button_play_question);
        buttonRecord.setOnClickListener(recordClickListener);
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

        /*
        audioControllerView = new AudioControllerView(this) {
            protected View makeControllerView() {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                root = inflater.inflate(R.layout.view_interview_audio_controller, null);
                initControllerView(
                        root,
                        R.id.interview_audio_controller_button_pause,
                        AudioControllerView.NO_VIEW,
                        AudioControllerView.NO_VIEW,
                        AudioControllerView.NO_VIEW
                );
                return root;
            }
        };
        */
    }

    /**
     * TODO is it a good idea to instantiate MediaPlayer and MediaRecorder for each question?
     */
    private void showNextQuestion() {
        stopPlaying();
        stopRecording();

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

    private void stopRecording() {
        buttonRecord.setText("Tap to record your answer.");
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
            //audioControllerView.setPlayer(InterviewActivity.this);
            //audioControllerView.setAnchorView(surfaceViewContainer);
            audioPlayer.start();
            //audioControllerView.show();
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
            return false;
        }
    };

    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                Toast.makeText(InterviewActivity.this, "Finished playing question", Toast.LENGTH_SHORT).show();
                initRecorder();
                buttonRecord.setEnabled(true);
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
        audioPlayer = new MediaPlayer();

        //audioPlayer.setDisplay(display);
        audioPlayer.setOnPreparedListener(thisOnPreparedListener);
        audioPlayer.setOnCompletionListener(thisOnCompletionListener);
        audioPlayer.setOnErrorListener(onPlayerErrorListener);
        audioPlayer.setDataSource(this, audioUri);
        audioPlayer.prepareAsync();
    }

    private void initRecorder() throws IOException {
        String filename = String.format("%d_%d_%s",
                interviewController.getInterviewId(),
                interviewController.getCurrentQuestionId(),
                PrefUtils.getNickname(this)
        );
        String absPath = makePathInCacheDir(filename);
        interviewController.saveAnswerForCurrentQuestion(filename);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(absPath);
        recorder.prepare();
    }

    private void startRecording() {
        recorder.start();
        buttonRecord.setText("Tap to stop recording");
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
     */
    private final MediaPlayer.OnCompletionListener playbackOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                stopPlaying();

                if (isPlayingAnswer) {
                    if (interviewController.hasNext()) {
                        Toast.makeText(InterviewActivity.this, "Play next question!", Toast.LENGTH_LONG).show();
                        InterviewQuestion question = interviewController.nextQuestion();
                        prepareAudioForPlayback(question.getMedia().getFilename());
                    } else { // interview playback done
                        Toast.makeText(InterviewActivity.this, "Playback done!", Toast.LENGTH_LONG).show();
                        cleanup();
                    }
                } else {
                    Toast.makeText(InterviewActivity.this, "Play answer!", Toast.LENGTH_LONG).show();
                    prepareAudioForPlayback(interviewController.getAnswerFilename());
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot prepare player for interview playback");
                e.printStackTrace();
            }
        }
    };

    private void prepareAudioForPlayback(String audioFilename) throws IOException {
        isPlayingAnswer = !isPlayingAnswer;
        initPlayer(makePathInCacheDir(audioFilename), null, playbackOnPreparedListener, playbackOnCompletionListener);
    }

    private void finishInterview() {
        stopPlaying();
        stopRecording();
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
