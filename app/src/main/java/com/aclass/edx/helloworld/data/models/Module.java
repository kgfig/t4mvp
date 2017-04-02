package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.aclass.edx.helloworld.R;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;

/**
 * Created by tictocproject on 13/03/2017.
 * Use Parcelable interface to pass objects between 2 activities via intent.
 * Implementing the interface requires a static CREATOR field as done below.
 */

public class Module extends DataModel {

    public static final Parcelable.Creator<Module> CREATOR = new Parcelable.Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel source) {
            return new Module(source);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    private String title;

    public Module() {
        super();
    }

    public Module(String title) {
        this(0, title);
    }

    public Module(Parcel parcel) {
        this(parcel.readLong(), parcel.readString());
    }

    public Module(long id, String title) {
        super(id);
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

    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ModuleEntry._ID)));
        setTitle(cursor.getString(cursor.getColumnIndex(ModuleEntry.COLUMN_NAME_TITLE)));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ModuleEntry.COLUMN_NAME_TITLE, title);
        return values;
    }

    @Override
    public String toString() {
        return String.format("%s(%s=%d, %s=%s)", ModuleEntry.TABLE_NAME,
                ModuleEntry._ID, id,
                ModuleEntry.COLUMN_NAME_TITLE, title);
    }

    // TODO store icon per module
    @Override
    public int getImageId() {
        return 0;
    }


    @Override
    public String getText() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
