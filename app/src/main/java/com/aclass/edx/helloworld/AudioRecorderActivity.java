package com.aclass.edx.helloworld;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aclass.edx.helloworld.utils.ActivityUtils;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AudioRecorderActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = AudioRecorderActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final int STORE_PERMISSION = 0;
    private static final int RECORD_PERMISSION = 1;

    // Status fields
    private String[] activityPermissions = new String[]{
            WRITE_EXTERNAL_STORAGE,
            RECORD_AUDIO
    };
    private boolean permissionToRecordGranted = false,
            permissionToStoreGranted = false;
    private boolean isRecording = false,
            isPlaying = false;
    private String audioFilename, action;

    // Views
    private Button recordButton, playButton;
    private TextView recordedFilenameView;

    // Media stuff
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions();
        setContentView(R.layout.activity_audio_recorder);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length >= 2) {
                    permissionToStoreGranted = grantResults[STORE_PERMISSION] == PackageManager.PERMISSION_GRANTED;
                    permissionToRecordGranted = grantResults[RECORD_PERMISSION] == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }

        if (permissionToStoreGranted && permissionToRecordGranted) {
            initViews();
        } else {
            finish();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playButton.setEnabled(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.isPlaying()) mp.stop();
        donePlaying();
        recordedFilenameView.setText("Completed!!!");
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

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, activityPermissions, REQUEST_PERMISSION_CODE);
    }

    private final View.OnClickListener recordButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (isRecording) {
                    action = "prepare audio for playing";
                    stopRecording();
                    recordButton.setText("New Record");
                    isRecording = false;
                    prepareAudioForPlaying();
                } else {
                    action = "start recording";
                    startRecordingIfPermissionGranted();
                }
            } catch (IOException e) {
                recordedFilenameView.setText(String.format("Failed to %s", action));
            }
        }
    };

    private final View.OnClickListener playButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isPlaying) {
                stopPlaying();
                donePlaying();
                action = "stop playing audio";
            } else {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                recordButton.setEnabled(false);
                isPlaying = true;
                playButton.setText("Stop Playing");
                action = "play audio";
            }
        }
    };

    private void initViews() {
        recordButton = (Button) findViewById(R.id.record_button);
        playButton = (Button) findViewById(R.id.play_button);
        recordedFilenameView = (TextView) findViewById(R.id.recorded_file);

        recordButton.setOnClickListener(recordButtonListener);
        playButton.setOnClickListener(playButtonListener);
        playButton.setEnabled(false);
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, activityPermissions[STORE_PERMISSION]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, activityPermissions[RECORD_PERMISSION]) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions();
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void startRecordingIfPermissionGranted() throws IOException {
        if (!hasPermission()) {
            requestPermissions();
            return;
        }

        startRecording();
    }

    private void startRecording() throws IOException {
        audioFilename = getExternalCacheDir().getAbsolutePath() + "/";
        audioFilename += "t4-" + ActivityUtils.generateRandomFilename() + getString(R.string.recorder_audio_ext_name);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFilename);
        mediaRecorder.prepare();
        mediaRecorder.start();

        recordButton.setText("Stop Recording");
        recordedFilenameView.setText(audioFilename);
        isRecording = true;
        playButton.setEnabled(false);
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void prepareAudioForPlaying() throws IOException {
        mediaPlayer = MediaPlayer.create(this, Uri.parse(audioFilename));
        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void stopPlaying() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void donePlaying() {
        recordButton.setEnabled(true);
        isPlaying = false;
        playButton.setText("Play");
    }
}
