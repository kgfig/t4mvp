package com.aclass.edx.helloworld;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;

import com.aclass.edx.helloworld.data.models.Content;
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

        // TODO add test for passing parcel
        this.module = getIntent().getParcelableExtra(getString(R.string.dashboard_selected_module_key));

        TextView moduleName = (TextView) findViewById(R.id.content_list_module_name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        adapter = new ContentRecyclerAdapter(this, null, this);
        lessonList = (RecyclerView) findViewById(R.id.content_list_recycler_view);

        moduleName.setText(module.getTitle());
        lessonList.setLayoutManager(layoutManager);
        lessonList.addItemDecoration(divider);
        lessonList.setAdapter(adapter);

        getLoaderManager().initLoader(FETCH_CONTENT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                ContentEntry.CONTENT_URI,
                ContentEntry.ALL_COLUMN_NAMES,
                ContentEntry.COLUMN_NAME_MODULE_ID + " = ?",
                new String[]{ module.getId() + "" },
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

        switch(content.getType()) {
            case ContentEntry.TYPE_LESSON_MEDIA:
                goToMediaActivity(content.getContentId());
                break;
            default:
                Toast.makeText(this, "Content type " + content.getType() +
                        " not supported", Toast.LENGTH_SHORT);
        }
    }

    private void goToMediaActivity(long contentId) {
        Cursor mediaCursor = getContentResolver().query(
                MediaEntry.CONTENT_URI,
                MediaEntry.ALL_COLUMN_NAMES,
                MediaEntry._ID + "= ?",
                new String[]{contentId +""},
                null
        );

        if (mediaCursor.moveToNext()) {
            Media media = new Media();
            media.setValues(mediaCursor);

            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra(getString(R.string.content_list_selected_video_key), media);
            startActivity(intent);
        } else {
            throw new RuntimeException(getString(R.string.all_error_no_media_found_by_id));
        }
    }
}
