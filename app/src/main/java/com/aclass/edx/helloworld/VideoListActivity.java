package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.aclass.edx.helloworld.data.MediaContentProvider;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

public class VideoListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int VIDEO_LIST_LOADER = 0;
    public static final String SELECTED = "VideoListActivity.SELECTED";

    private MediaCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDb();

        getLoaderManager().initLoader(VIDEO_LIST_LOADER, null, this);
        cursorAdapter = new MediaCursorAdapter(this, null);
        setListAdapter(cursorAdapter);
    }

    @Override
    protected void onDestroy() {
        this.getContentResolver().delete(MediaContentProvider.MEDIA_URI, null, null);
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
                MediaContentProvider.MEDIA_URI,
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

    // This is a dummy method for pre-populating database.
    // TODO find out how to do this properly
    private void initDb() {
        String[] titles = new String[]{"Courtesy", "Warmth", "Initiative"};
        String[] filenames = new String[]{"video1", "video2", "video3"};

        for (int i = 0; i < titles.length && i < filenames.length ; i++) {
            ContentValues values = new ContentValues();
            values.put(MediaEntry.COLUMN_NAME_TITLE, titles[i]);
            values.put(MediaEntry.COLUMN_NAME_FILENAME, filenames[i]);
            values.put(MediaEntry.COLUMN_NAME_TYPE, MediaEntry.TYPE_VIDEO);
            getContentResolver().insert(MediaContentProvider.MEDIA_URI, values);
        }
    }

}
