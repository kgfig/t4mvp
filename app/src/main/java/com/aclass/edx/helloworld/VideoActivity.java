package com.aclass.edx.helloworld;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VideoActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        String selectedFilename = intent.getStringExtra(MainActivity.SELECTED_FILENAME);

        TextView filename = (TextView) findViewById(R.id.filename);
        filename.setText(selectedFilename);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mediaPlayer = MediaPlayer.create(this, R.raw.video1);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mediaPlayer.getCurrentPosition());
        mediaPlayer.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int position = savedInstanceState.getInt("position");
        mediaPlayer.seekTo(position);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
