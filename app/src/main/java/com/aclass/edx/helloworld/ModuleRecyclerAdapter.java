package com.aclass.edx.helloworld;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.viewgroup.utils.ClickableViewHolder;
import com.aclass.edx.helloworld.viewgroup.utils.CursorRecyclerViewAdapter;

/**
 * Created by tictocproject on 13/03/2017.
 */

public class ModuleRecyclerAdapter extends CursorRecyclerViewAdapter<ModuleRecyclerAdapter.ModuleTitleViewHolder> {

    private ListItemClickListener clickListener;

    public ModuleRecyclerAdapter(Context context, Cursor cursor, ListItemClickListener listener) {
        super(context, cursor);
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(ModuleTitleViewHolder viewHolder, Cursor cursor) {
        Module module = Module.from(cursor);
        viewHolder.bind(module);
    }

    @Override
    public ModuleTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.module_list_item, parent, false);
        return new ModuleTitleViewHolder(view);
    }

    public class ModuleTitleViewHolder extends ClickableViewHolder<Module> {

        TextView moduleTitle;

        public ModuleTitleViewHolder(View itemView) {
            super(itemView);

            this.moduleTitle = (TextView) itemView.findViewById(R.id.module_title_view);
        }

        @Override
        public void onClick(View v) {
            clickListener.onListItemClick(getAdapterPosition());
        }

        @Override
        public void bind(Module module) {
            moduleTitle.setText(module.getTitle());
        }

    }
}
