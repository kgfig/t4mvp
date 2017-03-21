package com.aclass.edx.helloworld.viewgroup.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.DataModel;
import com.aclass.edx.helloworld.data.models.Module;

/**
 * Created by tictocproject on 13/03/2017.
 */

public class ClickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView titleTextView;
    protected CursorRecyclerViewAdapter.ListItemClickListener clickListener;

    public ClickableViewHolder(View itemView, CursorRecyclerViewAdapter.ListItemClickListener listener) {
        super(itemView);
        this.titleTextView = (TextView) itemView.findViewById(R.id.recycler_title_text_view);
        this.clickListener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.onListItemClick(getAdapterPosition());
    }

    public void bind(DataModel model) {
        titleTextView.setText(model.getText());
    }
}
