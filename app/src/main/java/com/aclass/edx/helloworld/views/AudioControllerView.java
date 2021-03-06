/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aclass.edx.helloworld.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by tictocproject on 29/03/2017.
 */

public class AudioControllerView extends FrameLayout {

    private static final String TAG = AudioControllerView.class.getSimpleName();
    private static final int MILLS_PER_SECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;

    private Context context;
    private ViewGroup anchor;
    private View root;
    private AudioPlayer player;
    private SeekBar seekBar;
    private TextView textViewCurrentTime, textViewDuration;
    private boolean dragging;
    StringBuilder timeFormatBuilder;
    Formatter timeFormatter;
    private ImageButton pauseButton;

    public AudioControllerView(@NonNull Context context) {
        super(context);
        this.context = context;
        initFormatter();
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        makeControllerView();
    }

    private View.OnClickListener pauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };

    public void setPlayer(AudioPlayer audioPlayer) {
        player = audioPlayer;
        updatePausePlay();
    }

    public void setAnchorView(ViewGroup view) {
        anchor = view;

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    public void show() {
        setProgress();
        pauseButton.requestFocus();

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        anchor.addView(this, frameParams);

        updatePausePlay();
        post(showProgress);
    }

    // TODO intended to be overriden by derived classes
    protected View makeControllerView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = inflater.inflate(R.layout.view_audio_controller, null);
        pauseButton = (ImageButton) root.findViewById(R.id.audio_controller_button_pause);
        seekBar = (SeekBar) root.findViewById(R.id.audio_controller_seekbar);
        textViewCurrentTime = (TextView) root.findViewById(R.id.audio_controller_textview_currenttime);
        textViewDuration = (TextView) root.findViewById(R.id.audio_controller_textview_duration);

        pauseButton.requestFocus();
        pauseButton.setOnClickListener(pauseListener);
        seekBar.setOnSeekBarChangeListener(seekListener);
        seekBar.setMax(1000);
        return root;
    }

    private final Runnable showProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (!dragging && player.isPlaying()) {
                postDelayed(showProgress, 1000 - (pos % 1000));
            }
        }
    };

    private void initFormatter() {
        timeFormatBuilder = new StringBuilder();
        timeFormatter = new Formatter(timeFormatBuilder, Locale.getDefault());
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / MILLS_PER_SECOND;

        int seconds = totalSeconds % SECONDS_PER_MINUTE;
        int minutes = (totalSeconds / SECONDS_PER_MINUTE) % SECONDS_PER_MINUTE;

        timeFormatBuilder.setLength(0);

        return timeFormatter.format("%02d:%02d", minutes, seconds).toString();
    }

    private int setProgress() {
        if (player == null || seekBar == null || textViewCurrentTime == null
                || textViewDuration == null || dragging) {
            return 0;
        }

        int position = player.getCurrentPosition();
        int duration = player.getDuration();
        int percent = player.getBufferPercentage();

        if (duration > 0) {
            long pos = 1000L * position / duration;
            seekBar.setProgress((int) pos);
        }

        seekBar.setSecondaryProgress(percent * 10);
        textViewCurrentTime.setText(stringForTime(position));
        textViewDuration.setText(stringForTime(duration));

        return position;
    }

    public void updatePausePlay() {
        if (root == null || pauseButton == null || player == null)
            return;

        if (player.isPlaying()) {
            pauseButton.setImageResource(R.drawable.ic_media_pause);
        } else {
            pauseButton.setImageResource(R.drawable.ic_media_play);
        }
    }

    private final SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser)
                return;

            long duration = player.getDuration();
            long newPosition = (duration * progress) / 1000L;
            player.seekTo((int) newPosition);
            textViewCurrentTime.setText(stringForTime((int) newPosition));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            dragging = true;
            stopTracking();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            dragging = false;
            setProgress();
            updatePausePlay();
            post(showProgress);
        }
    };

    private void doPauseResume() {
        if (player == null)
            return;

        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
        updatePausePlay();
    }

    public void stopTracking() {
        removeCallbacks(showProgress);
    }

    public interface AudioPlayer {
        void start();

        void pause();

        void seekTo(int pos);

        int getDuration();

        int getCurrentPosition();

        int getBufferPercentage();

        boolean isPlaying();
    }
}
