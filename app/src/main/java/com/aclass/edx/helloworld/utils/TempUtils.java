package com.aclass.edx.helloworld.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;

import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tictocproject on 20/03/2017.
 */
// TODO remove this class in production code
// IMPORTANT NOTE:
// Most of the code here are placeholders and are to be deleted.

public class TempUtils {

    // Object test data
    public static Media COURTESY = new Media("Courtesy", "video1", MediaEntry.TYPE_VIDEO);
    public static Media WARMTH = new Media("Warmth", "video2", MediaEntry.TYPE_VIDEO);
    public static Media INITIATIVE = new Media("Initiative", "video3", MediaEntry.TYPE_VIDEO);
    public static Media TEAMWORK = new Media("Teamwork", "video4", MediaEntry.TYPE_VIDEO);
    public static Media KNOWLEDGE = new Media("Knowledge", "video5", MediaEntry.TYPE_VIDEO);
    public static Media AUDIO1 = new Media("Audio Lesson Title", "audio1", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION1 = new Media("Interview question", "interview01_question01", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION2 = new Media("Interview question", "interview01_question02", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION3 = new Media("Interview question", "interview01_question03", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION4 = new Media("Interview question", "interview01_question04", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION5 = new Media("Interview closing", "interview01_question05", MediaEntry.TYPE_AUDIO);
    public static Module INTERVIEW = new Module("Interview");
    public static Module MEETINGS = new Module("Meetings");
    public static Module BUSINESS_CORRESPONDENCE = new Module("Business Writing");

    // List test data
    public static Media[] TEST_DATA_MEDIA = new Media[]{COURTESY, WARMTH, INITIATIVE, TEAMWORK, KNOWLEDGE, AUDIO1};
    public static Module[] TEST_DATA_MODULES = new Module[]{INTERVIEW, MEETINGS, BUSINESS_CORRESPONDENCE};
    public static Media[] TEST_DATA_AUDIO = new Media[]{AUDIO_QUESTION1, AUDIO_QUESTION2, AUDIO_QUESTION3, AUDIO_QUESTION4, AUDIO_QUESTION5};
    public static String[] TEST_DATA_INTERVIEW_QUESTIONS = new String[] {
            "Tell me about yourself.",
            "How do you see yourself 5 years from now?",
            "What is your greatest achievement?",
            "I see. Do you have any other questions?",
            "Great. We will contact in a week's time."
    };

    public static void insertMediaTestData(SQLiteDatabase db) {
        for (Media media : TEST_DATA_MEDIA) {
            db.insertOrThrow(MediaEntry.TABLE_NAME, null, media.toContentValues());
        }

        for (Media audio: TEST_DATA_AUDIO) {
            db.insertOrThrow(MediaEntry.TABLE_NAME, null, audio.toContentValues());
        }
    }

    public static void insertModuleTestData(SQLiteDatabase db) {
        for (Module module : TEST_DATA_MODULES) {
            db.insert(ModuleEntry.TABLE_NAME, null, module.toContentValues());
        }
    }

    public static Media getMedia(SQLiteDatabase db) {
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, null, null, null, null, null, MediaEntry._ID, "1");
        Media sample = new Media();

        if (cursor.moveToNext()) {
            sample.setValues(cursor);
        }

        return new Media();
    }

    public static long getMediaId(SQLiteDatabase db, String filename) {
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, new String[]{MediaEntry._ID}, MediaEntry.COLUMN_NAME_FILENAME + " = ?", new String[]{filename}, null, null, MediaEntry._ID, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(MediaEntry._ID)) : 0;
    }

    public static long getModuleId(SQLiteDatabase db, String title) {
        Cursor cursor = db.query(ModuleEntry.TABLE_NAME, new String[]{ModuleEntry._ID}, ModuleEntry.COLUMN_NAME_TITLE + " = ?", new String[]{title}, null, null, ModuleEntry._ID, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(ModuleEntry._ID)) : 0;
    }

    public static void insertContentTestData(SQLiteDatabase db) {
        long courtesyId = getMediaId(db, COURTESY.getFilename());
        long warmthId = getMediaId(db, WARMTH.getFilename());
        long initiativeId = getMediaId(db, INITIATIVE.getFilename());
        long teamworkId = getMediaId(db, TEAMWORK.getFilename());
        long knowledgeId = getMediaId(db, KNOWLEDGE.getFilename());
        long audioId = getMediaId(db, AUDIO1.getFilename());

        long meetingsId = getModuleId(db, MEETINGS.getTitle());
        long interviewsId = getModuleId(db, INTERVIEW.getTitle());
        long businessId = getModuleId(db, BUSINESS_CORRESPONDENCE.getTitle());

        String courtesyTitle = "Lesson on courtesy and respect";
        String warmthTitle = "Lesson on warmth";
        String initiativeTitle = "To do or not to do - A lesson on taking initiative";
        String teamworkTitle = "TEAM stands for Together Everyone Achieves More";
        String knowledgeTitle = "Brush up your knowledge of products and services";

        Interview interview = new Interview("Character Interview");
        long interviewId = db.insert(AppContract.InterviewEntry.TABLE_NAME, null, interview.toContentValues());

        for (int i = 0; i < TEST_DATA_AUDIO.length ; i++) {
            String textQuestion = TEST_DATA_INTERVIEW_QUESTIONS[i];
            long audioQuestionId = getMediaId(db, "interview01_question0"+ (i+1));
            Log.d(TempUtils.class.getSimpleName(), "Insert question: " + textQuestion + " with media id " + audioQuestionId);
            InterviewQuestion question = new InterviewQuestion(interviewId, textQuestion, audioQuestionId, 1);
            db.insert(AppContract.InterviewQuestionEntry.TABLE_NAME, null, question.toContentValues());
        }

        Content meetingContent1 = new Content(meetingsId, ContentEntry.TYPE_LESSON_MEDIA, courtesyTitle, courtesyId, 1);
        Content meetingContent2 = new Content(meetingsId, ContentEntry.TYPE_LESSON_MEDIA, warmthTitle, warmthId, 2);
        Content interviewContent1 = new Content(interviewsId, ContentEntry.TYPE_LESSON_MEDIA, initiativeTitle, initiativeId, 1);
        Content interviewContent2 = new Content(interviewsId, ContentEntry.TYPE_LESSON_MEDIA, teamworkTitle, teamworkId, 2);
        Content interviewContent3 = new Content(interviewId, ContentEntry.TYPE_LESSON_PRACTICE_INTERIEW, "Practice Interview", interviewId, 3);
        Content businessContent = new Content(businessId, ContentEntry.TYPE_LESSON_MEDIA, knowledgeTitle, knowledgeId, 1);
        Content audioContent = new Content(businessId, ContentEntry.TYPE_LESSON_MEDIA, "Sample audio lesson", audioId, 2);
        Content[] contents = new Content[]{meetingContent1, meetingContent2, interviewContent1, interviewContent2, interviewContent3, businessContent, audioContent};

        for (Content content : contents) {
            db.insert(ContentEntry.TABLE_NAME, null, content.toContentValues());
        }

        Cursor cursor = db.query(
                ContentEntry.TABLE_NAME,
                ContentEntry.ALL_COLUMN_NAMES,
                ContentEntry.COLUMN_NAME_MODULE_ID + " = ?",
                new String[]{meetingsId + ""},
                null, null, null);

        while (cursor.moveToNext()) {
            Content content = new Content();
            content.setValues(cursor);
            Log.d("TEST", "fetched stuff " + content);
        }
    }
}
