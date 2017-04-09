package com.aclass.edx.helloworld.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.activities.TopicsActivity;
import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.data.models.Topic;
import com.aclass.edx.helloworld.utils.ActivityUtils;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;

public class NextButtonFragment extends Fragment {

    private Button buttonNextContent;
    private Content currentContent;

    public NextButtonFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentContent = getArguments().getParcelable(getString(R.string.current_content));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_next_button, container, false);
        buttonNextContent = (Button) view.findViewById(R.id.fragment_btn_next_content);
        buttonNextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        return view;
    }

    /**
     * Go to the activity for the next content or return to the list of topics
     */
    public void onButtonPressed() {
        Context context = getActivity();
        Content nextContent = ActivityUtils.getNextContent(context, currentContent);
        Topic parent = ActivityUtils.getParentTopic(context, currentContent);
        Module ancestor = ActivityUtils.getParentModule(context, parent);

        if (nextContent.isEmpty()) {
            // If this topic is done, go to the list of topics for the ancestor module
            Intent intent = new Intent(context, TopicsActivity.class);
            intent.putExtra(getString(R.string.dashboard_selected_module_key), ancestor);
            startActivity(intent);
        } else {
            // Otherwise, proceed to the next content in the same topic
            switch (nextContent.getType()) {
                case ContentEntry.TYPE_LESSON_MEDIA:
                    ActivityUtils.goToMediaActivity(context, nextContent);
                    break;
                case ContentEntry.TYPE_LESSON_PRACTICE_INTERIEW:
                    ActivityUtils.goToInterviewActivity(context, nextContent);
                    break;
                default:
                    Toast.makeText(context, "Content type " + nextContent.getType() +
                            " not supported", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
