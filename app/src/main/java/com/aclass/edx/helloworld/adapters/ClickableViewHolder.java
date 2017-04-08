package com.aclass.edx.helloworld.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aclass.edx.helloworld.R;
import com.aclass.edx.helloworld.data.models.DataModel;

/**
 * Created by tictocproject on 13/03/2017.
 */

public class ClickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected ImageView icon;
    protected TextView text;
    protected CursorRecyclerViewAdapter.ListItemClickListener clickListener;

    public ClickableViewHolder(View itemView, CursorRecyclerViewAdapter.ListItemClickListener listener) {
        super(itemView);

        this.clickListener = listener;
        this.icon = (ImageView) itemView.findViewById(R.id.recycler_row_icon);
        this.text = (TextView) itemView.findViewById(R.id.recycler_row_text);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.onListItemClick(getAdapterPosition());
    }

    public void bind(DataModel model) {
        int imageId = model.hasImageId() ? model.getImageId() : R.drawable.ic_create_black_24dp;

        icon.setImageResource(imageId);
        text.setText(model.getText());
    }
}
