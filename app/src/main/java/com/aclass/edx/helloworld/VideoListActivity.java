package com.aclass.edx.helloworld;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;

import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;
import com.aclass.edx.helloworld.viewgroup.utils.MediaRecyclerAdapter;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;

public class VideoListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.ListItemClickListener {

    private static final int VIDEO_LIST_LOADER = 0;
    public static final String SELECTED = "VideoListActivity.SELECTED";

    private MediaRecyclerAdapter adapter;
    private RecyclerView videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());

        adapter = new MediaRecyclerAdapter(this, null, this);
        videoList = (RecyclerView) findViewById(R.id.video_list);

        videoList.setLayoutManager(layoutManager);
        videoList.addItemDecoration(divider);
        videoList.setAdapter(adapter);

        getLoaderManager().initLoader(VIDEO_LIST_LOADER, null, VideoListActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(
                this, // context
                MediaEntry.CONTENT_URI,
                MediaEntry.ALL_COLUMN_NAMES,
                null, // select columns
                null, //select args
                null //sortOrder
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    // Pass filename of video resource to be played in the next activity
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Cursor cursor = adapter.getItem(clickedItemIndex);
        String filename = cursor.getString(cursor.getColumnIndex(MediaEntry.COLUMN_NAME_FILENAME));
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(VideoListActivity.SELECTED, filename);

        startActivity(intent);
    }
}
