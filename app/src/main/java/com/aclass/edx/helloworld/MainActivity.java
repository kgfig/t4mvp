package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    public final static String SELECTED_FILENAME = "com.aclass.edx.helloworld.SELECTED_FILENAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        String[] filenames = res.getStringArray(R.array.filenames);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filenames);
        setListAdapter(listAdapter);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(MainActivity.SELECTED_FILENAME, filename);
        startActivity(intent);
    }
}
