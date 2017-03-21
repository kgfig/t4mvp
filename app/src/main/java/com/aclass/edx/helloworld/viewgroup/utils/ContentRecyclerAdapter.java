package com.aclass.edx.helloworld.viewgroup.utils;

/**
 * Created by tictocproject on 20/03/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.contracts.MediaContract;
import com.aclass.edx.helloworld.data.models.Content;

/**
 * Created by ertd on 3/9/2017.
 */

public class ContentRecyclerAdapter extends CursorRecyclerViewAdapter<ClickableViewHolder> {

    private static final String TAG  = ContentRecyclerAdapter.class.getSimpleName();

    private ListItemClickListener clickListener;

    public ContentRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener clickListener) {
        super(context, cursor);
        this.clickListener = clickListener;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list_item, parent, false);
        return new ClickableViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, Cursor cursor) {
        Content content = new Content();
        content.setValues(cursor);
        holder.bind(content);
    }
}
