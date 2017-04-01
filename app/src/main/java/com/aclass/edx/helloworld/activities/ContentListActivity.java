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
import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.viewgroup.utils.ContentRecyclerAdapter;
import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;

public class ContentListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = ContentListActivity.class.getSimpleName();

    private static final int FETCH_CONTENT_LOADER = 2;

    private ContentRecyclerAdapter adapter;
    private RecyclerView lessonList;
    private Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        // Get selected module
        this.module = getIntent().getParcelableExtra(getString(R.string.dashboard_selected_module_key));

        // Init toolbar
        getSupportActionBar().setTitle(module.getTitle());
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
                ContentEntry.COLUMN_NAME_MODULE_ID + " = ?",
                new String[]{module.getId() + ""},
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
                goToMediaActivity(content);
                break;
            case ContentEntry.TYPE_LESSON_PRACTICE_INTERIEW:
                goToInterviewActivity(content);
                break;
            default:
                Toast.makeText(this, "Content type " + content.getType() +
                        " not supported", Toast.LENGTH_SHORT);
        }
    }

    private void goToInterviewActivity(Content content) {
        Cursor cursor = getContentResolver().query(
                Uri.parse(InterviewEntry.CONTENT_URI + "/" + content.getContentId()),
                InterviewEntry.ALL_COLUMN_NAMES,
                null,
                null,
                null
        );

        if (cursor.moveToNext()) {
            Interview interview = new Interview();
            interview.setValues(cursor);

            Intent intent = new Intent(this, InterviewActivity.class);
            intent.putExtra(getString(R.string.content_list_selected_content_key), interview);
            Toast.makeText(this, String.format("Go to interview with id %d and title %s", interview.getId(), interview.getTitle()), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }


    private void goToMediaActivity(Content content) {
        Cursor mediaCursor = getContentResolver().query(
                Uri.parse(MediaEntry.CONTENT_URI + "/" + content.getContentId()),
                MediaEntry.ALL_COLUMN_NAMES,
                null,
                null,
                null
        );

        if (mediaCursor.moveToNext()) {
            Media media = new Media();
            media.setValues(mediaCursor);

            goToMediaActivity(media);
        } else {
            throw new RuntimeException(getString(R.string.all_error_no_media_found_by_id));
        }
    }

    private void goToMediaActivity(Media media) {
        Intent intent;

        switch (media.getType()) {
            case MediaEntry.TYPE_VIDEO:
                intent = new Intent(this, VideoPlayerActivity.class);
                break;
            case MediaEntry.TYPE_AUDIO:
                intent = new Intent(this, AudioPlayerActivity.class);
                break;
            default:
                throw new RuntimeException("Invalid media type " + media.getType());
        }

        intent.putExtra(getString(R.string.content_list_selected_content_key), media);
        startActivity(intent);
    }
}
