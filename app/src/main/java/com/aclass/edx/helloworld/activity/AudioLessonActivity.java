package com.aclass.edx.helloworld.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

import com.aclass.edx.helloworld.R;

import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;

import com.aclass.edx.helloworld.data.models.Media;

import java.io.IOException;

public class AudioLessonActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaController.MediaPlayerControl {

    private static final String TAG = AudioLessonActivity.class.getSimpleName();
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    private static final int MILLIS_PER_SEC = 1000;
    private static final int SEC_PER_MIN = 60;

    private MediaPlayer audioPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private MediaController mediaController;
    private Handler handler = new Handler();

    private Media audio;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_lesson);

        // Get or init Media object to be played
        Intent intent = getIntent();
        String paramName = getString(R.string.content_list_selected_content_key);
        audio = intent.hasExtra(paramName) ? (Media) intent.getParcelableExtra(paramName) : new Media("Sample audio", "audio1", MediaEntry.TYPE_AUDIO);

        // Init views
        surfaceView = (SurfaceView) findViewById(R.id.audio_lesson_surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                toggleController();
                return false;
            }
        });
        // Simulate progress bar update
        /*
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", MIN_PROGRESS, MAX_PROGRESS); // see this max value coming back here, we animale towards that value
        animation.setDuration(10000); //in milliseconds
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
        */
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
            audioPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        currentPosition = audioPlayer.getCurrentPosition();
        audioPlayer.pause();
        outState.putInt(getString(R.string.media_player_position), currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPosition = savedInstanceState.getInt(getString(R.string.media_player_position));
        audioPlayer.seekTo(currentPosition);
        audioPlayer.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Uri audioUri = Uri.parse("android.resource://" + getPackageName() + "/" + getResources().getIdentifier(audio.getFilename(), "raw", getPackageName()));

        // Prepare MediaPlayer
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDisplay(surfaceHolder);
            audioPlayer.setOnPreparedListener(this);
            audioPlayer.setOnErrorListener(this);
            audioPlayer.setDataSource(this, audioUri);
            audioPlayer.prepareAsync();
            mediaController = new MediaController(this);
        } catch (IOException e) {
            Log.e(TAG, "Unable to prepare audio with uri " + audioUri);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        audioPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(surfaceView);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void start() {
        audioPlayer.start();
    }

    @Override
    public void pause() {
        audioPlayer.pause();
    }

    @Override
    public int getDuration() {
        return audioPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return audioPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        audioPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return audioPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return audioPlayer.getAudioSessionId();
    }

    private void toggleController() {
        if (mediaController != null) {
            if (mediaController.isShown()) {
                mediaController.hide();
            } else {
                mediaController.show();
            }
        }
    }
}
