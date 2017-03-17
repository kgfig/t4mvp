package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ModuleEntry;

/**
 * Created by tictocproject on 13/03/2017.
 * Use Parcelable interface to pass objects between 2 activities via intent.
 * Implementing the interface requires a static CREATOR field as done below.
 */

public class Module implements Parcelable {

    static final Parcelable.Creator<Module> CREATOR = new Parcelable.Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel source) {
            return new Module(source);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    private long id;
    private String title;

    public Module() {}

    public Module(String title) {
        this(0, title);
    }

    public Module(Parcel parcel) {
        this(parcel.readLong(), parcel.readString());
    }

    public Module(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(title);
    }

    public static Module from(Cursor cursor) {
        Module module = new Module();
        module.setId(cursor.getInt(cursor.getColumnIndex(ModuleEntry._ID)));
        module.setTitle(cursor.getString(cursor.getColumnIndex(ModuleEntry.COLUMN_NAME_TITLE)));

        return module;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ModuleEntry.COLUMN_NAME_TITLE, title);
        return values;
    }
}
