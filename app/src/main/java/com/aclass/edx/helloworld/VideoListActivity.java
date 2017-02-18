package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aclass.edx.helloworld.data.VideoDbHelper;

public class VideoListActivity extends ListActivity {

    public static final String SELECTED = "VideoListActivity.SELECTED";

    VideoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDb();

        Resources res = getResources();
        String[] filenames = res.getStringArray(R.array.filenames);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filenames);

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

    private void initDb() {
        dbHelper = new VideoDbHelper(this);
        dbHelper.getWritableDatabase();

        dbHelper
    }
}
