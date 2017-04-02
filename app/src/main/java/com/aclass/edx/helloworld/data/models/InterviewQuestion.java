package com.aclass.edx.helloworld.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewQuestionEntry;

/**
 * Created by tictocproject on 01/04/2017.
 */

public class InterviewQuestion extends DataModel {

    public static final Parcelable.Creator<InterviewQuestion> CREATOR = new Parcelable.Creator<InterviewQuestion>(){
        @Override
        public InterviewQuestion createFromParcel(Parcel source) {
            return new InterviewQuestion(source);
        }

        @Override
        public InterviewQuestion[] newArray(int size) {
            return new InterviewQuestion[size];
        }
    };

    private long interviewId;
    private String question;
    private long mediaId;
    private int seqNum;
    private Media media;

    public InterviewQuestion() {
        super(0);
    }

    public InterviewQuestion(Parcel parcel) {
        this(parcel.readLong(), parcel.readLong(), parcel.readString(), parcel.readLong(), parcel.readInt());
    }

    public InterviewQuestion(long interviewId, String question, long mediaId, int seqNum) {
        this(0, interviewId, question, mediaId, seqNum);
    }

    public InterviewQuestion(long id, long interviewId, String question, long mediaId, int seqNum) {
        super(id);
        this.interviewId = interviewId;
        this.question = question;
        this.mediaId = mediaId;
        this.seqNum = seqNum;
    }

    @Override
    public void setValues(Cursor cursor) {
        setId(cursor.getLong(cursor.getColumnIndex(InterviewQuestionEntry._ID)));
        setInterviewId(cursor.getLong(cursor.getColumnIndex(InterviewQuestionEntry.COLUMN_NAME_INTERVIEW_ID)));
        setQuestion(cursor.getString(cursor.getColumnIndex(InterviewQuestionEntry.COLUMN_NAME_QUESTION)));
        setMediaId(cursor.getLong(cursor.getColumnIndex(InterviewQuestionEntry.COLUMN_NAME_MEDIA_ID)));
        setSeqNum(cursor.getInt(cursor.getColumnIndex(InterviewQuestionEntry._ID)));

    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (id > 0) {
            values.put(InterviewQuestionEntry._ID, getId());
        }
        values.put(InterviewQuestionEntry.COLUMN_NAME_INTERVIEW_ID, getInterviewId());
        values.put(InterviewQuestionEntry.COLUMN_NAME_QUESTION, getQuestion());
        values.put(InterviewQuestionEntry.COLUMN_NAME_MEDIA_ID, getMediaId());
        values.put(InterviewQuestionEntry.COLUMN_NAME_SEQ_NUM, getSeqNum());
        return values;
    }

    @Override
    public String getText() {
        return question;
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
        dest.writeLong(interviewId);
        dest.writeString(question);
        dest.writeLong(mediaId);
        dest.writeInt(seqNum);
    }

    public long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(long interviewId) {
        this.interviewId = interviewId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
