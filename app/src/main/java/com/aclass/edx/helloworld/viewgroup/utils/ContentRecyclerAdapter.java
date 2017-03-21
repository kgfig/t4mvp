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

public class ContentRecyclerAdapter extends CursorRecyclerViewAdapter<ContentRecyclerAdapter.ContentViewHolder> {

    private static final String TAG  = ContentRecyclerAdapter.class.getSimpleName();

    private ListItemClickListener clickListener;

    public ContentRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener clickListener) {
        super(context, cursor);
        this.clickListener = clickListener;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_list_item, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, Cursor cursor) {
        Content content = new Content();
        content.setValues(cursor);
        holder.bind(content);
    }

    public class ContentViewHolder extends ClickableViewHolder<Content> {

        TextView title;

        public ContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recycler_title_text_view);
        }

        @Override
        public void onClick(View v) {
            clickListener.onListItemClick(getAdapterPosition());
        }

        @Override
        public void bind(Content content) {
            title.setText(content.getId() + "-" + content.getModuleId() + "-" + content.getContentId());
        }
    }
}
