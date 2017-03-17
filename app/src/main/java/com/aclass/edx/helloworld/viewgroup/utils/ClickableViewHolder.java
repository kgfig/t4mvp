package com.aclass.edx.helloworld.viewgroup.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tictocproject on 13/03/2017.
 */

public abstract class ClickableViewHolder<D> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ClickableViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public abstract void onClick(View view);
    public abstract void bind(D data);
}
