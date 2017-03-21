package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

/**
 * Created by tictocproject on 20/03/2017.
 */

public abstract class DataModel implements Parcelable {

    protected long id;
    protected String text;

    public DataModel() { this(0, ""); }

    public DataModel(long id) {
        this(id, "");
    }

    public DataModel(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public abstract void setValues(Cursor cursor);
    public abstract ContentValues toContentValues();
    public abstract String getText();
    public abstract int getImageId();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean hasImageId() {
        return getImageId() > 0;
    }
}
