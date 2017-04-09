package com.aclass.edx.helloworld.fragments;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.utils.PrefUtils;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecordButtonClickListener} interface
 * to handle interaction events.
 * Use the {@link RecordButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordButtonFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MediaRecorder recorder;
    private Button buttonRecord;
    private boolean isRecording = false;

    private OnRecordButtonClickListener mListener;

    public RecordButtonFragment() {
        // Required empty public constructor
    }

    public static RecordButtonFragment newInstance() {
        RecordButtonFragment fragment = new RecordButtonFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_button, container, false);
        buttonRecord = (Button) view.findViewById(R.id.fragment_btn_record);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(v);
            }
        });
        setButtonEnabled(false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (isRecording) {
            isRecording = false;
            stopRecording();
            if (mListener != null) {
                mListener.onStopRecording();
            }
        } else {
            isRecording = true;
            startRecording();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecordButtonClickListener) {
            mListener = (OnRecordButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecordButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void initializeRecorder(String outputFilePath) throws IOException {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(outputFilePath);
        recorder.prepare();
    }

    private void startRecording() {
        recorder.start();
        buttonRecord.setText("Tap to stop recording");
    }

    private void stopRecording() {
        buttonRecord.setText("Tap to record your answer.");
        setButtonEnabled(false);

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public void setButtonEnabled(boolean enabled) {
        if (buttonRecord != null) {
            buttonRecord.setEnabled(enabled);
        }
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
    public interface OnRecordButtonClickListener {
        // TODO: Update argument type and name
        void onStopRecording();
    }
}
