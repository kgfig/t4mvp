package com.aclass.edx.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.edx.helloworld.data.models.Media;

/**
 * Created by ertd on 3/9/2017.
 */

public class MediaRecyclerAdapter extends CursorRecyclerViewAdapter<MediaRecyclerAdapter.MediaTitleViewHolder> {

    private ListItemClickListener clickListener;

    public MediaRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener clickListener) {
        super(context, cursor);
        this.clickListener = clickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public MediaTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item, parent, false);
        MediaTitleViewHolder holder = new MediaTitleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MediaTitleViewHolder holder, Cursor cursor) {
        Media media = Media.fromCursor(cursor);
        holder.bind(media);
    }

    public class MediaTitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;

        public MediaTitleViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.video_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onListItemClick(position);
        }

        void bind(Media newMedia) {
            titleView.setText(newMedia.getTitle());
        }
    }
}
