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

public class MediaRecyclerAdapter extends CursorRecyclerViewAdapter<MediaRecyclerAdapter.MediaTitleViewHolder> {

    private ListItemClickListener clickListener;

    public MediaRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener clickListener) {
        super(context, cursor);
        this.clickListener = clickListener;
    }

    @Override
    public MediaTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list_item, parent, false);
        return new MediaTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaTitleViewHolder holder, Cursor cursor) {
        Media media = new Media();
        media.setValues(cursor);
        holder.bind(media);
    }

    public class MediaTitleViewHolder extends ClickableViewHolder<Media> {
        TextView titleView;

        public MediaTitleViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.recycler_title_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onListItemClick(getAdapterPosition());
        }

        @Override
        public void bind(Media newMedia) {
            titleView.setText(newMedia.getTitle());
        }
    }
}
