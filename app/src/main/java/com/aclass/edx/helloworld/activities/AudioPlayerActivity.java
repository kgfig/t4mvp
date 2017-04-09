package com.aclass.edx.helloworld.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.fragments.NextButtonFragment;
import com.aclass.edx.helloworld.views.AudioControllerView;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioControllerView.AudioPlayerControl {

    private static final String TAG = AudioPlayerActivity.class.getSimpleName();

    private MediaPlayer audioPlayer;
    private AudioControllerView controllerView;
    private Media audio;
    private Content content;
    private int currentPosition = 0;

    private TextView textViewTranscript;
    private FrameLayout surfaceViewContainer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // Get or init Media object to be played
        Intent intent = getIntent();
        audio = intent.getParcelableExtra(getString(R.string.content_list_selected_audio_key));
        content = intent.getParcelableExtra(getString(R.string.content_list_selected_content_key));

        // Init views
        getSupportActionBar().setTitle(audio.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTranscript = (TextView) findViewById(R.id.audio_player_textview_transcript);
        surfaceViewContainer = (FrameLayout) findViewById(R.id.audio_player_surfaceview_container);
        surfaceView = (SurfaceView) findViewById(R.id.audio_player_surfaceview);

        if (textViewTranscript != null) {
            textViewTranscript.setText(R.string.large_text);
        }
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        NextButtonFragment nextButtonFragment = new NextButtonFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.current_content), content);
        nextButtonFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.audio_player_container_next, nextButtonFragment).commit();
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
            controllerView.stopTracking();
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
        initAudioPlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controllerView.setPlayer(this);
        controllerView.setAnchorView(surfaceViewContainer);
        audioPlayer.start();
        controllerView.show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
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

    private void initAudioPlayer() {
        Uri audioUri = Uri.parse("android.resource://" + getPackageName() + "/" + getResources().getIdentifier(audio.getFilename(), "raw", getPackageName()));
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDisplay(surfaceHolder);
            audioPlayer.setOnPreparedListener(this);
            audioPlayer.setOnErrorListener(this);
            audioPlayer.setDataSource(this, audioUri);
            audioPlayer.prepareAsync();
            controllerView = new AudioControllerView(this);
        } catch (IOException e) {
            Log.e(TAG, "Unable to prepare audio with uri " + audioUri);
        }
    }
}
