package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ContentEntry;

/**
 * Created by tictocproject on 20/03/2017.
 */

public class Content {

    private long id;
    private int type;
    private long module_id;
    private long content_id;

    public Content(int type, long module_id, long content_id) {
        this(0, type, module_id, content_id);
    }

    public Content(long id, int type, long module_id, long content_id) {
        this.id = id;
        this.type = type;
        this.module_id = module_id;
        this.content_id = content_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getModule_id() {
        return module_id;
    }

    public void setModule_id(long module_id) {
        this.module_id = module_id;
    }

    public long getContent_id() {
        return content_id;
    }

    public void setContent_id(long content_id) {
        this.content_id = content_id;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ContentEntry._ID, getId());
        values.put(ContentEntry.COLUMN_NAME_MODULE_ID, getModule_id());
        values.put(ContentEntry.COLUMN_NAME_TYPE, getType());
        values.put(ContentEntry.COLUMN_NAME_CONTENT_ID, getContent_id());
        return values;
    }
}
