package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aclass.edx.helloworld.data.DBHelper;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.services.MediaService;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends ListActivity {

    public static final String SELECTED = "VideoListActivity.SELECTED";

    private MediaService mediaService;
    private MediaListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: use asynctask when getting database
        mediaService = new MediaService(this.getContentResolver());

        initDb();

        List<Media> videos = mediaService.getAllVideoOrderById();
        listAdapter = new MediaListAdapter(this, android.R.layout.simple_list_item_1, videos);
        setListAdapter(listAdapter);
    }

    @Override
    protected void onDestroy() {
        mediaService.deleteMedia(null, null);
        super.onDestroy();
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Media media = listAdapter.getItem(position);

        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(VideoListActivity.SELECTED, media.getFilename());

        startActivity(intent);
    }

    private void initDb() {
        List<Media> mediaFiles = new ArrayList<Media>(5);
        mediaFiles.add(new Media("Courtesy", "video1", MediaEntry.TYPE_VIDEO));
        mediaFiles.add(new Media("Warmth", "video2", MediaEntry.TYPE_VIDEO));
        mediaFiles.add(new Media("Initiative", "video3", MediaEntry.TYPE_VIDEO));

        for (Media mediaFile : mediaFiles) {
            mediaService.insertSingleRow(mediaFile.getTitle(), mediaFile.getFilename(), MediaEntry.TYPE_VIDEO);
        }
    }
}
