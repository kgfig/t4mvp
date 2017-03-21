package com.aclass.edx.helloworld;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.MediaController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Media;

public class VideoActivity extends AppCompatActivity {

    private int position;
    private VideoView videoView;
    private MediaController mediaController;
    private Media videoModel;
    private Content content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Get selected content and video
        Intent intent = getIntent();
        content = intent.getParcelableExtra(getString(R.string.content_list_selected_content));
        videoModel = intent.getParcelableExtra(getString(R.string.content_list_selected_video_key));
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" +
                getResources().getIdentifier(videoModel.getFilename(), "raw", getPackageName()));

        // Init toolbar
        getSupportActionBar().setTitle(content.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init video player and controls
        mediaController = new MediaController(VideoActivity.this);
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
        outState.putInt(getString(R.string.video_position), videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(getString(R.string.video_position));
        videoView.seekTo(position);
    }

}
