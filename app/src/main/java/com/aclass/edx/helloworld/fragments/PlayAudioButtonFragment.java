package com.aclass.edx.helloworld.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPlayButtonClickListener} interface
 * to handle interaction events.
 * Use the {@link PlayAudioButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayAudioButtonFragment extends Fragment {

    private static final String TAG = PlayAudioButtonFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private MediaPlayer audioPlayer;
    private int currentPosition;
    private int customType;

    private Button playButton;
    private OnPlayButtonClickListener mListener;

    public PlayAudioButtonFragment() {
        // Required empty public constructor
    }

    public static PlayAudioButtonFragment newInstance(int customType) {
        PlayAudioButtonFragment fragment = new PlayAudioButtonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, customType);
        fragment.setArguments(args);
        return fragment;
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playButton.setEnabled(false);

            if (audioPlayer != null) {
                if (!audioPlayer.isPlaying()) {
                    audioPlayer.seekTo(0);
                    audioPlayer.start();
                }
            }
        }
    };

    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            audioPlayer.start();
            playButton.setEnabled(false);
        }
    };

    private final MediaPlayer.OnErrorListener onPlayerErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(getActivity(), "Something went wrong with the player", Toast.LENGTH_LONG).show();
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e(TAG, "unknown media playback error");
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e(TAG, "server connection died");
                default:
                    Log.e(TAG, "generic audio playback error");
                    break;
            }

            switch (extra) {
                case MediaPlayer.MEDIA_ERROR_IO:
                    Log.e(TAG, "IO media error");
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    Log.e(TAG, "media error, malformed");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    Log.e(TAG, "unsupported media content");
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    Log.e(TAG, "media timeout error");
                    break;
                default:
                    Log.e(TAG, "unknown playback error");
                    break;
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            customType = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_answer, container, false);
        playButton = (Button) view.findViewById(R.id.fragment_btn_play);
        playButton.setOnClickListener(onClickListener);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayButtonClickListener) {
            mListener = (OnPlayButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPlayButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (audioPlayer != null) {
            currentPosition = audioPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onDestroy() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (audioPlayer != null) {
            currentPosition = audioPlayer.getCurrentPosition();
            audioPlayer.pause();
            outState.putInt(getString(R.string.media_player_position), currentPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void initializePlayer(Uri uri) throws IOException {
        initializePlayer(getActivity(), uri);
    }

    public void initializePlayer(Context context, Uri uri) throws IOException {
        audioPlayer = new MediaPlayer();
        audioPlayer.setOnPreparedListener(onPreparedListener);
        audioPlayer.setOnErrorListener(onPlayerErrorListener);
        audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playButton.setEnabled(true);
                if (mListener != null) {
                    mListener.onAudioPlayerRelease(customType);
                }
            }
        });
        audioPlayer.setDataSource(context, uri);
        audioPlayer.prepareAsync();
    }

    public void setButtonEnabled(boolean enabled) {
        if (playButton!=null) {
            playButton.setEnabled(enabled);
        }
    }

    public void release() {
        if (audioPlayer != null) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    public int getCustomType() {
        return customType;
    }

    public void setCustomType(int customType) {
        this.customType = customType;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPlayButtonClickListener {
        // TODO: Update argument type and name
        void onAudioPlayerRelease(int customType);
    }
}
