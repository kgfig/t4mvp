package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.aclass.edx.helloworld.R;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ContentEntry;

/**
 * Created by tictocproject on 20/03/2017.
 */

public class Content extends DataModel {

    static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    private int type;
    private long moduleId;
    private long contentId;

    public Content() {
        super();
    }

    public Content(int type, long moduleId, long contentId) {
        super();
        this.type = type;
        this.moduleId = moduleId;
        this.contentId = contentId;
    }

    public Content(Parcel parcel) {
        this(parcel.readLong(), parcel.readInt(), parcel.readLong(), parcel.readLong());
    }

    public Content(long id, int type, long moduleId, long contentId) {
        super(id);
        this.type = type;
        this.moduleId = moduleId;
        this.contentId = contentId;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeInt(type);
        parcel.writeLong(moduleId);
        parcel.writeLong(contentId);
    }

    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ContentEntry._ID)));
        setType(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TYPE)));
        setModuleId(cursor.getLong(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_MODULE_ID)));
        setContentId(cursor.getLong(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_CONTENT_ID)));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        if (getId() > 0) {
            values.put(ContentEntry._ID, getId());
        }

        values.put(ContentEntry.COLUMN_NAME_TYPE, getType());
        values.put(ContentEntry.COLUMN_NAME_MODULE_ID, getModuleId());
        values.put(ContentEntry.COLUMN_NAME_CONTENT_ID, getContentId());
        return values;
    }

    @Override
    public String toString() {
        return String.format("%s(%s=%d, %s=%d, %s=%d, %s=%d)",
                ContentEntry.TABLE_NAME,
                ContentEntry._ID, id,
                ContentEntry.COLUMN_NAME_TYPE, type,
                ContentEntry.COLUMN_NAME_MODULE_ID, moduleId,
                ContentEntry.COLUMN_NAME_CONTENT_ID, contentId);
    }

    @Override
    public String getText() {
        return toString();
    }

    @Override
    public int getImageId() {
        switch(type) {
            case ContentEntry.TYPE_LESSON_MEDIA:
                return R.drawable.ic_account_circle_black_24dp;
            default:
                return 0;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }


}
