package com.aclass.edx.helloworld;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VideoActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        String selectedFilename = intent.getStringExtra(MainActivity.SELECTED_FILENAME);

        TextView filename = (TextView) findViewById(R.id.filename);
        filename.setText(selectedFilename);

        mediaPlayer = MediaPlayer.create(this, R.raw.closing);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();

        currentPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.seekTo(currentPosition);
        mediaPlayer.start();
    }
}
