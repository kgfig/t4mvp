package com.aclass.edx.helloworld.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.Module;

/**
 * Created by tictocproject on 13/03/2017.
 */

public class ModuleRecyclerAdapter extends CursorRecyclerViewAdapter<ClickableViewHolder> {

    private ListItemClickListener clickListener;

    public ModuleRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener listener) {
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
