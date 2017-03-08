package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.aclass.edx.helloworld.data.DBHelper;
import com.aclass.edx.helloworld.data.MediaContentProvider;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.services.MediaService;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int VIDEO_LIST_LOADER = 0;
    public static final String SELECTED = "VideoListActivity.SELECTED";

    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDb();

        String[] projection = new String[] { MediaEntry.COLUMN_NAME_TITLE};
        int[] label = new int[] { android.R.id.text1 };
        getLoaderManager().initLoader(VIDEO_LIST_LOADER, null, this);
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, projection, label, 0);
        setListAdapter(cursorAdapter);
    }

    @Override
    protected void onDestroy() {
        this.getContentResolver().delete(MediaContentProvider.CONTENT_URI, null, null);
        super.onDestroy();
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = (Cursor) cursorAdapter.getItem(position);
        String filename = cursor.getString(cursor.getColumnIndex(MediaEntry.COLUMN_NAME_FILENAME));
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(VideoListActivity.SELECTED, filename);

        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { MediaEntry._ID, MediaEntry.COLUMN_NAME_TITLE, MediaEntry.COLUMN_NAME_FILENAME };
        CursorLoader cursorLoader = new CursorLoader(this,
                MediaContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        cursorAdapter.swapCursor(null);
    }

    private void initDb() {
        List<Media> mediaFiles = new ArrayList<Media>(5);
        mediaFiles.add(new Media("Courtesy", "video1", MediaEntry.TYPE_VIDEO));
        mediaFiles.add(new Media("Warmth", "video2", MediaEntry.TYPE_VIDEO));
        mediaFiles.add(new Media("Initiative", "video3", MediaEntry.TYPE_VIDEO));

        MediaService mediaService = new MediaService(this.getContentResolver());
        for (Media mediaFile : mediaFiles) {
            mediaService.insertSingleRow(mediaFile.getTitle(), mediaFile.getFilename(), MediaEntry.TYPE_VIDEO);
        }
    }
}
