package com.aclass.edx.helloworld;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;

public class DashboardActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.ListItemClickListener {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private static final String SELECTED_MODULE = "DashboardActivity.SELECTED_MODULE";

    private RecyclerView moduleList;
    private ModuleRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        moduleList = (RecyclerView) findViewById(R.id.module_list);
        adapter = new ModuleRecyclerAdapter(this, null, this);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();

        switch(menuItemId) {
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
}
