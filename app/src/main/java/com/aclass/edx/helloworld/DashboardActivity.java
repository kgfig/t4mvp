package com.aclass.edx.helloworld;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ModuleEntry;

import com.aclass.edx.helloworld.data.asynctasks.AsyncInsertModule;
import com.aclass.edx.helloworld.data.contracts.MediaContract;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private static final String SELECTED_MODULE = "DashboardActivity.SELECTED_MODULE";
    private static final int FETCH_MODULES_LOADER = 1;

    private RecyclerView moduleList;
    private ModuleRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        AsyncInsertModule asyncInsertTask = new AsyncInsertModule(getContentResolver()) {
            @Override
            protected void onPostExecute(List list) {
                super.onPostExecute(list);
                getLoaderManager().initLoader(FETCH_MODULES_LOADER, null, DashboardActivity.this);
            }
        };

        asyncInsertTask.execute(new Module("Interview"), new Module("Meetings"), new Module("Grammar"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());

        adapter = new ModuleRecyclerAdapter(this, null, this);
        moduleList = (RecyclerView) findViewById(R.id.module_list);

        moduleList.setLayoutManager(layoutManager);
        moduleList.addItemDecoration(divider);
        moduleList.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getLoaderManager().initLoader(FETCH_MODULES_LOADER, null, this);
    }

    @Override
    protected void onDestroy() {
        getContentResolver().delete(ModuleEntry.CONTENT_URI, null, null);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();

        switch (menuItemId) {
            case R.id.dashboard_action_profile:
                Toast.makeText(this, "Go to profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.dashboard_action_practice:
                Toast.makeText(this, "Practice", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(int clickedItemPosition) {
        Cursor cursor = adapter.getItem(clickedItemPosition);
        Module module = Module.from(cursor);

        Intent intent = new Intent(this, VideoListActivity.class);
        intent.putExtra(SELECTED_MODULE, module);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ModuleEntry._ID,
                ModuleEntry.COLUMN_NAME_TITLE
        };

        CursorLoader cursorLoader = new CursorLoader(
                this, //context
                ModuleEntry.CONTENT_URI,
                projection,
                null, // select
                null, // select args
                null // sortorder
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

}
