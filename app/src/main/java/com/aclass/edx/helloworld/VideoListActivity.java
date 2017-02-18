package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aclass.edx.helloworld.models.Video;

import java.util.List;

public class VideoListActivity extends ListActivity {

    public static final String SELECTED = "VideoListActivity.SELECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Video.prepopulate();

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Video.getAllTitles());
        setListAdapter(listAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //TODO: try using getSelectedItem
        String selectedFilename = (String) getListAdapter().getItem(position);

        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(VideoListActivity.SELECTED, selectedFilename);
        startActivity(intent);
    }
}
