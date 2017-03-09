package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;

import com.aclass.edx.helloworld.data.contracts.MediaContract;

/**
 * Created by ertd on 3/9/2017.
 */

public class Media {

    private String title;
    private String filename;
    private int type;

    public Media(String title, String filename, int type) {
        this.title = title;
        this.filename = filename;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getFilename() {
        return filename;
    }

    public int getType() {
        return type;
    }

    public ContentValues toContenValues() {
        ContentValues values = new ContentValues();
        values.put(MediaContract.MediaEntry.COLUMN_NAME_TITLE, getTitle());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_FILENAME, getFilename());
        values.put(MediaContract.MediaEntry.COLUMN_NAME_TYPE, getType());

        return values;
    }
}


