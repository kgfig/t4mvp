package com.aclass.edx.helloworld.activities;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.utils.PrefUtils;
import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;
import com.aclass.edx.helloworld.viewgroup.utils.ModuleRecyclerAdapter;

public class DashboardActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private static final int FETCH_MODULES_LOADER = 1;

    private RecyclerView moduleList;
    private ModuleRecyclerAdapter adapter;
    private TextView greetUser, chooseATopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (PrefUtils.skippedOrSavedNickname(this)) {
            initViews();
        } else {
            Intent intent = new Intent(getApplicationContext(), GetNameActivity.class);
            startActivity(intent);
            finish();
        }
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
        Module module = new Module();
        module.setValues(cursor);

        Intent intent = new Intent(this, ContentListActivity.class);
        intent.putExtra(getString(R.string.dashboard_selected_module_key), module);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(
                this, //context
                ModuleEntry.CONTENT_URI,
                ModuleEntry.ALL_COLUMN_NAMES,
                null, // select
                null, // select args
                null // sortorder
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    private String getGreeting() {
        String greeting;

        if (PrefUtils.skippedNickname(this)) {
            greeting = getString(R.string.all_hello);
        } else {
            greeting = String.format(getString(R.string.all_hello_format), PrefUtils.getNickname(this));
        }

        return greeting;
    }

    private void initViews() {
        getLoaderManager().initLoader(FETCH_MODULES_LOADER, null, DashboardActivity.this);

        chooseATopic = (TextView) findViewById(R.id.dashboard_choose_a_topic);
        greetUser = (TextView) findViewById(R.id.dashboard_greet_user);
        greetUser.setText(getGreeting());

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
}
