package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.aclass.edx.helloworld.data.contracts.AppContract.TopicEntry;

/**
 * Created by tictocproject on 07/04/2017.
 */

public class Topic extends DataModel {

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    private long moduleId;
    private String title;
    private Module module;

    public Topic() {
        super(0);
    }

    public Topic(long moduleId, String title) {
        this(0, moduleId, title);
    }

    public Topic(Parcel parcel) {
        this(parcel.readLong(), parcel.readLong(), parcel.readString());
    }

    public Topic(long id, long moduleId, String title) {
        super(id);
        this.moduleId = moduleId;
        this.title = title;
    }

    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(TopicEntry._ID)));
        setModuleId(cursor.getLong(cursor.getColumnIndex(TopicEntry.COLUMN_NAME_MODULE_ID)));
        setTitle(cursor.getString(cursor.getColumnIndex(TopicEntry.COLUMN_NAME_TITLE)));
    }

    @Override
    public ContentValues toContentValues() {
        return null;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(moduleId);
        dest.writeString(title);
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
