package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.asynctasks.AsyncInsertMedia;

import java.util.List;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;

public class VideoListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int VIDEO_LIST_LOADER = 0;
    public static final String SELECTED = "VideoListActivity.SELECTED";

    private MediaCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: learn and apply best practices in populating db
        Media courtesy = new Media("Courtesy", "video1", MediaEntry.TYPE_VIDEO);
        Media warmth = new Media("Warmth", "video2", MediaEntry.TYPE_VIDEO);
        AsyncInsertMedia asyncInsertMedia = new AsyncInsertMedia(getContentResolver()){
            @Override
            protected void onPostExecute(List list) {
                // Load data from db and init list only after insert queries are done
                initLoaderAndAdapter();
            }
        };

        asyncInsertMedia.execute(courtesy, warmth);
    }

    @Override
    protected void onDestroy() {
        this.getContentResolver().delete(MediaEntry.CONTENT_URI, null, null);
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
        String[] projection = {
                MediaEntry._ID,
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_FILENAME };

        CursorLoader cursorLoader = new CursorLoader(
                this, // context
                MediaEntry.CONTENT_URI,
                projection,
                null, // select columns
                null, //select args
                null //sortOrder
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void initLoaderAndAdapter() {
        getLoaderManager().initLoader(VIDEO_LIST_LOADER, null, VideoListActivity.this);
        cursorAdapter = new MediaCursorAdapter(VideoListActivity.this, null);
        setListAdapter(cursorAdapter);
    }

}
