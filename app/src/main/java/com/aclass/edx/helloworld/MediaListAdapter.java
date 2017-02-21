package com.aclass.edx.helloworld;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aclass.edx.helloworld.data.models.Media;

import java.util.List;

/**
 * Taken from https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 * Created by ertd on 2/21/2017.
 */

public class MediaListAdapter extends ArrayAdapter<Media> {
    // View lookup cache to improve performance
    // because findViewById() can be slow in practice
    // And it's called for every row in ArrayAdapter.getView()
    private static class ViewHolder {
        TextView title;
    }

    public MediaListAdapter(Context context, int resource, List<Media> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Media item at this position
        Media media = getItem(position);
        // View lookup cache stored in tag
        ViewHolder viewHolder;
        // Check if this view is being recycled
        if (convertView == null) {
            // New view, inflate a new view for row
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(android.R.id.text1);
            // Cache the ViewHolder inside the fresh view
            // From docs: Tags can also be used to store data within a view without resorting to another data structure.
            convertView.setTag(viewHolder);
        } else { // Recycled view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Set view with data from Media object
        viewHolder.title.setText(media.getTitle());
        // Return completed view to render on screen
        return convertView;
    }
}
