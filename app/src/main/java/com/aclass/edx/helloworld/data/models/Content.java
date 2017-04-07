package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.aclass.edx.helloworld.R;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;

/**
 * Created by tictocproject on 20/03/2017.
 */

public class Content extends DataModel {

    public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    private long topicId;
    private int type;
    private String title;
    private long contentId;
    private int seqNum;

    public Content() {
        super();
    }

    public Content(long topicId, int type, String title, long contentId, int seqNum) {
        this(0, topicId, type, title, contentId, seqNum);
    }

    public Content(Parcel parcel) {
        this(parcel.readLong(), parcel.readLong(), parcel.readInt(), parcel.readString(), parcel.readLong(), parcel.readInt());
    }

    public Content(long id, long moduleId, int type, String title, long contentId, int seqNum) {
        super(id);
        this.topicId = moduleId;
        this.type = type;
        this.title = title;
        this.contentId = contentId;
        this.seqNum = seqNum;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeLong(topicId);
        parcel.writeInt(type);
        parcel.writeString(title);
        parcel.writeLong(contentId);
        parcel.writeInt(seqNum);
    }

    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(ContentEntry._ID)));
        setTopicId(cursor.getLong(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TOPIC_ID)));
        setType(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TYPE)));
        setTitle(cursor.getString(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_TITLE)));
        setContentId(cursor.getLong(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_CONTENT_ID)));
        setSeqNum(cursor.getInt(cursor.getColumnIndex(ContentEntry.COLUMN_NAME_SEQ_NUM)));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        if (getId() > 0) {
            values.put(ContentEntry._ID, getId());
        }

        values.put(ContentEntry.COLUMN_NAME_TOPIC_ID, getTopicId());
        values.put(ContentEntry.COLUMN_NAME_TITLE, getTitle());
        values.put(ContentEntry.COLUMN_NAME_TYPE, getType());
        values.put(ContentEntry.COLUMN_NAME_CONTENT_ID, getContentId());
        values.put(ContentEntry.COLUMN_NAME_SEQ_NUM, getSeqNum());
        return values;
    }

    @Override
    public String toString() {
        return String.format("%s(%s=%d, %s=%d, %s=%d, %s=%s, %s=%d, %s=%d)",
                ContentEntry.TABLE_NAME,
                ContentEntry._ID, id,
                ContentEntry.COLUMN_NAME_TOPIC_ID, topicId,
                ContentEntry.COLUMN_NAME_TYPE, type,
                ContentEntry.COLUMN_NAME_TITLE, title,
                ContentEntry.COLUMN_NAME_CONTENT_ID, contentId,
                ContentEntry.COLUMN_NAME_SEQ_NUM, seqNum);
    }

    @Override
    public String getText() {
        return title;
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

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }
}
