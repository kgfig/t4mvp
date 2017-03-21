package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.aclass.edx.helloworld.data.contracts.MediaContract;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;

/**
 * Created by ertd on 3/9/2017.
 */

public class Media extends DataModel {

    static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    private String title;
    private String filename;
    private int type;

    public Media() {}

    public Media(String title, String filename, int type) {
        this(0, title, filename, type);
    }

    public Media(Parcel parcel) {
        this(parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readInt());
    }

    public Media(long id, String title, String filename, int type) {
        super(id);
        this.title = title;
        this.filename = filename;
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(filename);
        parcel.writeInt(type);
    }

    /**
     * @param cursor
     * @return Media model instance with fields initialized from Cursor
     */
    public void setValues(Cursor cursor) {
        long mId = cursor.getLong(cursor.getColumnIndex(MediaContract.MediaEntry._ID));
        String mTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_TITLE));
        String mFilename = cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_FILENAME));
        int mType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_TYPE));

        setId(mId);
        setTitle(mTitle);
        setFilename(mFilename);
        setType(mType);
    }

    /**
     * @return ContentValues from attributes in this Media model instance
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        if (id > 0) {
            values.put(MediaEntry._ID, getId());
        }

        values.put(MediaEntry.COLUMN_NAME_TITLE, getTitle());
        values.put(MediaEntry.COLUMN_NAME_FILENAME, getFilename());
        values.put(MediaEntry.COLUMN_NAME_TYPE, getType());

        return values;
    }
}


