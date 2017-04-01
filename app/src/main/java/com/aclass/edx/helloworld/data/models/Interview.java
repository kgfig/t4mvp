package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewEntry;

/**
 * Created by tictocproject on 01/04/2017.
 */

public class Interview extends DataModel {

    static final Parcelable.Creator<Interview> CREATOR = new Parcelable.Creator<Interview>(){
        @Override
        public Interview createFromParcel(Parcel source) {
            return new Interview(source);
        }

        @Override
        public Interview[] newArray(int size) {
            return new Interview[size];
        }
    };

    private String title;

    public Interview() {super(0);}

    public Interview(Parcel parcel) {
        this(parcel.readLong(), parcel.readString());
    }

    public Interview(String title) {
        this(0, title);
    }

    public Interview(long id, String title) {
        super(id);
        this.title = title;
    }

    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(InterviewEntry._ID)));
        setTitle(cursor.getString(cursor.getColumnIndex(InterviewEntry.COLUMN_NAME_TITLE)));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (id > 0) {
            values.put(InterviewEntry._ID, getId());
        }
        values.put(InterviewEntry.COLUMN_NAME_TITLE, getTitle());
        return values;
    }

    @Override
    public String getText() {
        return title;
    }

    @Override
    public int getImageId() {
        return 0;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
