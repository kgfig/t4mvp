package com.aclass.edx.helloworld;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AudioRecorderActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    private static final String TAG = AudioRecorderActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final int STORE_PERMISSION = 0;
    private static final int RECORD_PERMISSION = 1;

    // Status and activity vars
    private String[] activityPermissions = new String[]{ WRITE_EXTERNAL_STORAGE, RECORD_AUDIO};
    private boolean permissionToRecordGranted = false, permissionToStoreGranted = false;
    private boolean isRecording = false;
    private String audioFilename, action;

    // Views
    private Button recordButton;
    private TextView recordedFilenameView;

    // Media stuff
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length >= 2) {
                    permissionToStoreGranted = grantResults[STORE_PERMISSION] == PackageManager.PERMISSION_GRANTED;
                    permissionToRecordGranted = grantResults[RECORD_PERMISSION] == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions();
        setContentView(R.layout.activity_audio_recorder);

        recordButton = (Button) findViewById(R.id.record_button);
        recordedFilenameView = (TextView) findViewById(R.id.recorded_file);
        mediaController = new MediaController(this);

        recordButton.setOnClickListener(this);
        mediaController.setAnchorView(recordedFilenameView);
        mediaController.setMediaPlayer(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mediaRecorder != null) {
            stopRecording();
        }

        if (mediaPlayer != null) {
            stopPlaying();
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        mediaController.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.record_button:
                onClickRecord();
                break;
            default:
                Log.e(TAG, "Do nothing");
                break;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, activityPermissions, REQUEST_PERMISSION_CODE);
    }

    private void onClickRecord() {
        try {
            if (isRecording) {
                action = "stop recording";
                stopRecording();
                recordButton.setText("New Record");
                isRecording = false;
                recordedFilenameView.setText(audioFilename);
                setMediaControllerVisible(true);
            } else {
                action = "start recording";
                startRecording();
                recordButton.setText("Stop Recording");
                isRecording = true;
                setMediaControllerVisible(false);
            }
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed to %s", action));
        }
    }

    private void startRecording() throws IOException {
        if (permissionToRecordGranted && permissionToStoreGranted) {
            audioFilename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            audioFilename += generateRandomFilename() + ".3gp";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilename);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } else {
            requestPermissions();
        }
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void prepareRecordedAudio() throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(audioFilename);
        mediaPlayer.prepare();
    }

    private void stopPlaying() {
        mediaPlayer.stop();
    }

    private void setMediaControllerVisible(boolean visible) {
        mediaController.setEnabled(visible);

        if (visible) {
            mediaController.show();
        } else {
            mediaController.hide();
        }
    }

    private static String generateRandomFilename() {
        Random random = new Random();
        long randomValue = random.nextLong();
        return "t4" + randomValue;
    }

    @Override
    public void start() {
        try {
            if (mediaPlayer != null) {
                prepareRecordedAudio();
                recordButton.setEnabled(false);
            }
        } catch(IOException e) {
            Log.e(TAG, "Failed to prepare audio for playing");
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            stopPlaying();
        }
        recordButton.setEnabled(true);
    }

    @Override
    public int getDuration() {
        return mediaPlayer == null ? 0 : mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer == null ? 0 : mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer == null ? false : mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
        //return mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer == null ? 0 : mediaPlayer.getAudioSessionId();
    }
}
