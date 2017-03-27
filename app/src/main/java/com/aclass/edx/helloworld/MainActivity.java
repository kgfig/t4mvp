package com.aclass.edx.helloworld;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aclass.edx.helloworld.activity.AudioLessonActivity;
import com.aclass.edx.helloworld.activity.DashboardActivity;

public class MainActivity extends ListActivity {

    private final static String MENU_ITEM_AUDIO = "Audio Recorder and Player";
    private final static String MENU_ITEM_DASHBOARD = "Dashboard";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        String[] menuItems = res.getStringArray(R.array.menu_items);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        setListAdapter(listAdapter);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        String menuItem = (String) getListAdapter().getItem(position);
        Intent intent = null;

        switch(menuItem) {
            case MENU_ITEM_AUDIO:
                intent = new Intent(this, AudioLessonActivity.class);
                break;
            case MENU_ITEM_DASHBOARD:
                intent = new Intent(this, DashboardActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

    }
}
