package com.aclass.edx.helloworld.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;

import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;

import com.aclass.edx.helloworld.data.models.Media;

import java.io.IOException;

public class AudioLessonActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TAG = AudioLessonActivity.class.getSimpleName();
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    private static final int MILLIS_PER_SEC = 1000;
    private static final int SEC_PER_MIN = 60;

    private Media audio;
    private MediaPlayer audioPlayer;
    private ProgressBar progressBar;
    private TextView time;
    private int minutes, seconds; // TODO move to a separate class?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_lesson);

        // Get or init Media object to be played
        Intent intent = getIntent();
        String paramName = getString(R.string.content_list_selected_content_key);
        audio = intent.hasExtra(paramName) ? (Media) intent.getParcelableExtra(paramName) : new Media("Sample audio", "audio1", MediaEntry.TYPE_AUDIO);
        Uri audioUri = Uri.parse("android.resource://" + getPackageName() + "/" + getResources().getIdentifier(audio.getFilename(), "raw", getPackageName()));

        // Prepare MediaPlayer
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(this, audioUri);
            audioPlayer.setOnPreparedListener(this);
            audioPlayer.setOnErrorListener(this);
            audioPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Unable to prepare audio with uri " + audioUri);
        }

        // Init views
        progressBar = (ProgressBar) findViewById(R.id.audio_lesson_progress_bar);
        time = (TextView) findViewById(R.id.audio_lesson_tv_time);

        // Simulate progress bar update
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", MIN_PROGRESS, MAX_PROGRESS); // see this max value coming back here, we animale towards that value
        animation.setDuration(10000); //in milliseconds
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
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
    public void onPrepared(MediaPlayer mp) {
        setTimeInfo(audioPlayer.getDuration());
        audioPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private void setTimeInfo(long millis) {
        int totalSeconds = (int) (millis / MILLIS_PER_SEC);
        this.minutes = totalSeconds / SEC_PER_MIN;
        this.seconds = totalSeconds % SEC_PER_MIN;
    }
}
