package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.aclass.edx.helloworld.R;
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

    private String filename;
    private int type;

    public Media() {}

    public Media(String filename, int type) {
        this(0, filename, type);
    }

    public Media(Parcel parcel) {
        this(parcel.readLong(), parcel.readString(), parcel.readInt());
    }

    public Media(long id, String filename, int type) {
        super(id);
        this.filename = filename;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(filename);
        parcel.writeInt(type);
    }

    /**
     * @param cursor
     * @return Media model instance with fields initialized from Cursor
     */
    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(MediaContract.MediaEntry._ID)));
        setFilename(cursor.getString(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_FILENAME)));
        setType(cursor.getInt(cursor.getColumnIndexOrThrow(MediaContract.MediaEntry.COLUMN_NAME_TYPE)));
    }

    /**
     * @return ContentValues from attributes in this Media model instance
     */
    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        if (id > 0) {
            values.put(MediaEntry._ID, getId());
        }

        values.put(MediaEntry.COLUMN_NAME_FILENAME, getFilename());
        values.put(MediaEntry.COLUMN_NAME_TYPE, getType());

        return values;
    }

    @Override
    public String toString() {
        return String.format("%s(%s=%d, %s=%s, %s=%d)", MediaEntry.TABLE_NAME,
                MediaEntry._ID, id,
                MediaEntry.COLUMN_NAME_FILENAME, filename,
                MediaEntry.COLUMN_NAME_TYPE, type);
    }

    @Override
    public String getText() {
        return filename;
    }

    @Override
    public int getImageId() {
        switch(type) {
            case MediaEntry.TYPE_VIDEO:
                return R.drawable.ic_account_circle_black_24dp;
            case MediaEntry.TYPE_AUDIO:
                return R.drawable.ic_create_black_24dp;
            default:
                return 0;
        }
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


}


