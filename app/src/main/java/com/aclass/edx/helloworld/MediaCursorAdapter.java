package com.aclass.edx.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

/**
 * Created by ertd on 3/8/2017.
 */

public class MediaCursorAdapter extends CursorAdapter {

    public MediaCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleLabel = (TextView) view.findViewById(android.R.id.text1);
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaEntry.COLUMN_NAME_TITLE));
        titleLabel.setText(title);
    }
}
