package com.aclass.edx.helloworld;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private int position;
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        String selectedFilename = intent.getStringExtra(MainActivity.SELECTED_FILENAME);

        // Init video player and controls
        mediaController = new MediaController(VideoActivity.this);
        videoView = (VideoView)findViewById (R.id.videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(getVideoUri(selectedFilename));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(position);

                if (position == 0) {
                    videoView.start();
                } else {
                    videoView.resume();
                }

                mediaController.setEnabled(true);
                mediaController.show(0);
            }
        });

        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("position");
        videoView.seekTo(position);
    }

    private Uri getVideoUri(String filename) {
        int videoId;
        switch(filename) {
            case "Courtesy":
                videoId = R.raw.video1;
                break;
            case "Warmth":
                videoId = R.raw.video2;
                break;
            case "Initiative":
                videoId = R.raw.video3;
                break;
            case "Teamwork":
                videoId = R.raw.video4;
                break;
            case "Knowledge":
                videoId = R.raw.video5;
                break;
            default:
                videoId = R.raw.video5;
                break;
        }

        return Uri.parse("android.resource://"+getPackageName() + "/" + videoId);
    }

}
