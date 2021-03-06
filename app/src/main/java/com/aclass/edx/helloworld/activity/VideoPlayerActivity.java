package com.aclass.edx.helloworld.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.Media;

public class VideoPlayerActivity extends AppCompatActivity {

    private int position;
    private VideoView videoView;
    private MediaController mediaController;
    private Media video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Get selected content and video
        Intent intent = getIntent();
        video = intent.getParcelableExtra(getString(R.string.content_list_selected_content_key));
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" +
                getResources().getIdentifier(video.getFilename(), "raw", getPackageName()));

        // Init toolbar
        getSupportActionBar().setTitle(video.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init video player and controls
        mediaController = new MediaController(VideoPlayerActivity.this);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
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
        outState.putInt(getString(R.string.media_player_position), videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(getString(R.string.media_player_position));
        videoView.seekTo(position);
    }

}
