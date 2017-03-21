package com.aclass.edx.helloworld.viewgroup.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Module;

/**
 * Created by ertd on 3/9/2017.
 */

public class MediaRecyclerAdapter extends CursorRecyclerViewAdapter<ClickableViewHolder> {

    private ListItemClickListener clickListener;

    public MediaRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener clickListener) {
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
        Media media = new Media();
        media.setValues(cursor);
        holder.bind(media);
    }
}
