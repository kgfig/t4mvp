package com.aclass.edx.helloworld.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewEntry;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Topic;
import com.aclass.edx.helloworld.adapters.ContentRecyclerAdapter;
import com.aclass.edx.helloworld.adapters.CursorRecyclerViewAdapter;
import com.aclass.edx.helloworld.utils.ActivityUtils;

public class ContentListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = ContentListActivity.class.getSimpleName();

    private static final int FETCH_CONTENT_LOADER = 2;

    private ContentRecyclerAdapter adapter;
    private RecyclerView lessonList;
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        // Get selected module
        this.topic = getIntent().getParcelableExtra(getString(R.string.topics_selected_topic_key));

        // Init toolbar
        getSupportActionBar().setTitle(topic.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init content
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        adapter = new ContentRecyclerAdapter(this, null, this);
        lessonList = (RecyclerView) findViewById(R.id.content_list_recycler_view);

        lessonList.setLayoutManager(layoutManager);
        lessonList.addItemDecoration(divider);
        lessonList.setAdapter(adapter);

        // Load data
        getLoaderManager().initLoader(FETCH_CONTENT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                ContentEntry.CONTENT_URI,
                ContentEntry.ALL_COLUMN_NAMES,
                ContentEntry.COLUMN_NAME_TOPIC_ID + " = ?",
                new String[]{topic.getId() + ""},
                null
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(int clickedItemPosition) {
        Cursor contentCursor = adapter.getItem(clickedItemPosition);
        Content content = new Content();
        content.setValues(contentCursor);

        switch (content.getType()) {
            case ContentEntry.TYPE_LESSON_MEDIA:
                ActivityUtils.goToMediaActivity(this, content);
                break;
            case ContentEntry.TYPE_LESSON_PRACTICE_INTERIEW:
                ActivityUtils.goToInterviewActivity(this, content);
                break;
            default:
                Toast.makeText(this, "Content type " + content.getType() +
                        " not supported", Toast.LENGTH_SHORT);
        }
    }

}
