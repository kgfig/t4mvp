package com.aclass.edx.helloworld.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.MediaContract.ModuleEntry;

import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Module;

/**
 * Created by tictocproject on 20/03/2017.
 */

// IMPORTANT NOTE:
// Most of the code here are placeholders and are to be deleted.

public class TempUtils {

    // Object test data
    public static Media COURTESY = new Media("Courtesy", "video1", MediaEntry.TYPE_VIDEO);
    public static Media WARMTH = new Media("Warmth", "video2", MediaEntry.TYPE_VIDEO);
    public static Media INITIATIVE = new Media("Initiative", "video3", MediaEntry.TYPE_VIDEO);
    public static Media TEAMWORK = new Media("Teamwork", "video4", MediaEntry.TYPE_VIDEO);
    public static Media KNOWLEDGE = new Media("Knowledge", "video5", MediaEntry.TYPE_VIDEO);
    public static Module INTERVIEW = new Module("Interview");
    public static Module MEETINGS = new Module("Meetings");
    public static Module BUSINESS_CORRESPONDENCE = new Module("Business Writing");

    // List test data
    public static Media[] TEST_DATA_MEDIA = new Media[]{COURTESY, WARMTH, INITIATIVE, TEAMWORK, KNOWLEDGE};
    public static Module[] TEST_DATA_MODULES = new Module[]{INTERVIEW, MEETINGS, BUSINESS_CORRESPONDENCE};

    public static void insertMediaTestData(SQLiteDatabase db) {
        for (Media media : TEST_DATA_MEDIA) {
            db.insertOrThrow(MediaEntry.TABLE_NAME, null, media.toContentValues());
        }
    }

    public static void insertModuleTestData(SQLiteDatabase db) {
        for (Module module : TEST_DATA_MODULES) {
            db.insert(ModuleEntry.TABLE_NAME, null, module.toContentValues());
        }
    }

    public static Media getMedia(SQLiteDatabase db) {
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, null, null, null, null, null, MediaEntry._ID, "1");
        if (cursor.moveToNext()) {
            return Media.fromCursor(cursor);
        } else {
            return new Media();
        }
    }

    public static long getMediaId(SQLiteDatabase db, String title) {
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, new String[]{ MediaEntry._ID}, "title = ?", new String[]{title}, null, null, MediaEntry._ID, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(MediaEntry._ID)) : 0;
    }

    public static long getModuleId(SQLiteDatabase db, String title) {
        Cursor cursor = db.query(ModuleEntry.TABLE_NAME, new String[]{ ModuleEntry._ID}, "title = ?", new String[]{title}, null, null, MediaEntry._ID, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(ModuleEntry._ID)) : 0;
    }

    public static void insertContentTestData(SQLiteDatabase db) {
        long courtesyId = getMediaId(db, COURTESY.getTitle());
        long warmthId = getMediaId(db, WARMTH.getTitle());
        long initiativeId = getMediaId(db, INITIATIVE.getTitle());
        long teamworkId = getMediaId(db, TEAMWORK.getTitle());
        long knowledgeId = getMediaId(db, KNOWLEDGE.getTitle());
        long meetingsId = getModuleId(db, MEETINGS.getTitle());
        long interviewsId = getModuleId(db, INTERVIEW.getTitle());
        long businessId = getModuleId(db, BUSINESS_CORRESPONDENCE.getTitle());

        Content meetingContent1 = new Content(ContentEntry.TYPE_LESSON_MEDIA, meetingsId, courtesyId);
        Content meetingContent2 = new Content(ContentEntry.TYPE_LESSON_MEDIA, meetingsId, warmthId);
        Content interviewContent1 = new Content(ContentEntry.TYPE_LESSON_MEDIA, interviewsId, initiativeId);
        Content interviewContent2 = new Content(ContentEntry.TYPE_LESSON_MEDIA, interviewsId, teamworkId);
        Content businessContent = new Content(ContentEntry.TYPE_LESSON_MEDIA, businessId, knowledgeId);
        Content[] contents = new Content[]{meetingContent1, meetingContent2, interviewContent1, interviewContent2, businessContent};

        for (Content content : contents) {
            db.insert(ContentEntry.TABLE_NAME, null, content.toContentValues());
        }
    }
}
