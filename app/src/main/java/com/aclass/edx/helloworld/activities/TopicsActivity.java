package com.aclass.edx.helloworld.activities;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aclass.edx.helloworld.R;

import static com.aclass.edx.helloworld.data.contracts.AppContract.TopicEntry;

import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.data.models.Topic;
import com.aclass.edx.helloworld.viewgroup.utils.ClickableViewHolder;
import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;

public class TopicsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.ListItemClickListener {

    private RecyclerView topicsView;
    private CursorRecyclerViewAdapter<ClickableViewHolder> topicsViewAdapter;
    private Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        // Get selected module
        module = getIntent().getParcelableExtra(getString(R.string.dashboard_selected_module_key));

        // Init toolbar
        getSupportActionBar().setTitle(module.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Init content
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        topicsViewAdapter = new TopicRecyclerAdapter(this, null, this);
        topicsView = (RecyclerView) findViewById(R.id.topics_recyclerview_topics);

        topicsView.setLayoutManager(layoutManager);
        topicsView.addItemDecoration(divider);
        topicsView.setAdapter(topicsViewAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                this,
                TopicEntry.CONTENT_URI,
                TopicEntry.ALL_COLUMN_NAMES,
                TopicEntry.COLUMN_NAME_MODULE_ID + " = ?",
                new String[]{module.getId() + ""},
                null
        );
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        topicsViewAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        topicsViewAdapter.changeCursor(null);
    }

    @Override
    public void onListItemClick(int clickedItemPosition) {
        Cursor cursor = topicsViewAdapter.getItem(clickedItemPosition);
        Topic topic = new Topic();
        topic.setValues(cursor);

        Intent intent = new Intent(this, ContentListActivity.class);
        intent.putExtra(getString(R.string.topics_selected_topic_key), topic);
        startActivity(intent);
    }

    private class TopicRecyclerAdapter extends CursorRecyclerViewAdapter<ClickableViewHolder> {

        private CursorRecyclerViewAdapter.ListItemClickListener clickListener;

        public TopicRecyclerAdapter(Context context, Cursor cursor, CursorRecyclerViewAdapter.ListItemClickListener listener) {
            super(context, cursor);
            this.clickListener = listener;
        }

        @Override
        public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.recycler_list_item, parent, false);
            return new ClickableViewHolder(view, clickListener);
        }

        @Override
        public void onBindViewHolder(ClickableViewHolder viewHolder, Cursor cursor) {
            Module module = new Module();
            module.setValues(cursor);
            viewHolder.bind(module);
        }

    }
}
