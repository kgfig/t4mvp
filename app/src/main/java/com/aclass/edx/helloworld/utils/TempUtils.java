package com.aclass.edx.helloworld.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.TopicEntry;

import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Content;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.models.Module;
import com.aclass.edx.helloworld.data.models.Topic;

/**
 * Created by tictocproject on 20/03/2017.
 */
// TODO remove this class in production code
// IMPORTANT NOTE:
// Most of the code here are placeholders and are to be deleted.

public class TempUtils {

    private static final String TAG = TempUtils.class.getSimpleName();

    // Object test data
    public static Media COURTESY = new Media("Courtesy", "video1", MediaEntry.TYPE_VIDEO);
    public static Media WARMTH = new Media("Warmth", "video2", MediaEntry.TYPE_VIDEO);
    public static Media INITIATIVE = new Media("Initiative", "video3", MediaEntry.TYPE_VIDEO);
    public static Media TEAMWORK = new Media("Teamwork", "video4", MediaEntry.TYPE_VIDEO);
    public static Media KNOWLEDGE = new Media("Knowledge", "video5", MediaEntry.TYPE_VIDEO);
    public static Media LESSON_AUDIO = new Media("Audio Lesson Title", "audio1", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION1 = new Media("Character interview question", "interview01_question01", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION2 = new Media("Character interview question", "interview01_question02", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION3 = new Media("Character interview question", "interview01_question03", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION4 = new Media("Character interview question", "interview01_question04", MediaEntry.TYPE_AUDIO);
    public static Media AUDIO_QUESTION5 = new Media("Character interview question", "interview01_question05", MediaEntry.TYPE_AUDIO);
    public static Module MODULE_INTERVIEW = new Module("Interview");
    public static Module MODULE_MEETINGS = new Module("Meetings");
    public static Module MODULE_CORRESPONDENCE = new Module("Business Writing");
    public static Module MODULE_PRESENTATION = new Module("Presentation");
    public static Interview INTERVIEW = new Interview("Character Interview");
    public static String LOOK_PRESENTABLE = "Look Presentable";
    public static String QUESTIONS_AHEAD = "Questions Ahead";
    public static String MAKE_AN_IMPACT = "How to Make an Impact";

    // List test data
    public static Media[] TEST_DATA_MEDIA = new Media[]{COURTESY, WARMTH, INITIATIVE, TEAMWORK, KNOWLEDGE, LESSON_AUDIO};
    public static Module[] TEST_DATA_MODULES = new Module[]{MODULE_INTERVIEW, MODULE_PRESENTATION, MODULE_MEETINGS, MODULE_CORRESPONDENCE};
    public static Media[] TEST_DATA_AUDIO = new Media[]{AUDIO_QUESTION1, AUDIO_QUESTION2, AUDIO_QUESTION3, AUDIO_QUESTION4, AUDIO_QUESTION5};
    public static String[] TEST_DATA_INTERVIEW_QUESTIONS = new String[]{
            "Tell me about yourself.",
            "How do you see yourself 5 years from now?",
            "What is your greatest achievement?",
            "I see. Do you have any other questions?",
            "Great. We will contact in a week's time."
    };

    public static void insertMediaTestData(SQLiteDatabase db) {
        for (Media media : TEST_DATA_MEDIA) {
            long id = db.insertOrThrow(MediaEntry.TABLE_NAME, null, media.toContentValues());
            Log.d(TAG, "Inserted media " + media.getFilename() + " with id " + id);
        }

        for (Media audio : TEST_DATA_AUDIO) {
            long id = db.insertOrThrow(MediaEntry.TABLE_NAME, null, audio.toContentValues());
            Log.d(TAG, "Inserted audio " + audio.getFilename() + " with id " + id);
        }
    }

    public static void insertModuleTestData(SQLiteDatabase db) {
        for (Module module : TEST_DATA_MODULES) {
            long id = db.insert(ModuleEntry.TABLE_NAME, null, module.toContentValues());
            Log.d(TAG, "Inserted module " + module.getTitle() + " with id " + id);
        }
    }

    public static void insertTopicTestData(SQLiteDatabase db) {
        long interviewId = getModuleId(db, MODULE_INTERVIEW.getTitle());
        long presentationId = getModuleId(db, MODULE_PRESENTATION.getTitle());
        insertTopicTestData(db, interviewId, presentationId);
    }

    private static void insertTopicTestData(SQLiteDatabase db, long interviewModuleId, long presentationModuleId) {
        Topic lookPresentable = new Topic(interviewModuleId, LOOK_PRESENTABLE);
        Topic questionsAhead = new Topic(interviewModuleId, QUESTIONS_AHEAD);
        Topic makeAnImpact = new Topic(presentationModuleId, MAKE_AN_IMPACT);
        Topic[] topics = {lookPresentable, questionsAhead, makeAnImpact};
        for (Topic topic : topics) {
            long id = db.insertOrThrow(TopicEntry.TABLE_NAME, null, topic.toContentValues());
            Log.d(TAG, "Inserted topic " + topic.getTitle() + " with id " + id);
        }
    }

    public static long insertInterviewTestData(SQLiteDatabase db) {
        long interviewId = db.insert(AppContract.InterviewEntry.TABLE_NAME, null, INTERVIEW.toContentValues());
        Log.d(TAG, "Inserted interview " + INTERVIEW.getTitle() + " with id " + interviewId);

        for (int i = 0; i < TEST_DATA_AUDIO.length; i++) {
            String textQuestion = TEST_DATA_INTERVIEW_QUESTIONS[i];
            long audioQuestionId = getMediaId(db, "interview01_question0" + (i + 1));
            InterviewQuestion question = new InterviewQuestion(interviewId, textQuestion, audioQuestionId, 1);
            long id = db.insert(AppContract.InterviewQuestionEntry.TABLE_NAME, null, question.toContentValues());
            Log.d(TAG, "Inserted interview question: " + textQuestion + " with id " + id + " and media id " + audioQuestionId);
        }

        return interviewId;
    }

    public static void insertContentTestData(SQLiteDatabase db) {
        long courtesyId = getMediaId(db, COURTESY.getFilename());
        long warmthId = getMediaId(db, WARMTH.getFilename());
        long initiativeId = getMediaId(db, INITIATIVE.getFilename());
        long teamworkId = getMediaId(db, TEAMWORK.getFilename());
        long knowledgeId = getMediaId(db, KNOWLEDGE.getFilename());
        long audioId = getMediaId(db, LESSON_AUDIO.getFilename());

        long presentableId = getTopicId(db, LOOK_PRESENTABLE);
        long questionsAheadId = getTopicId(db, QUESTIONS_AHEAD);
        long impactId = getTopicId(db, MAKE_AN_IMPACT);
        long interviewObjId = getInterviewId(db);

        String courtesyTitle = "Lesson on courtesy and respect";
        String warmthTitle = "Lesson on warmth";
        String initiativeTitle = "To do or not to do - A lesson on taking initiative";
        String teamworkTitle = "TEAM stands for Together Everyone Achieves More";
        String knowledgeTitle = "Brush up your knowledge of products and services";

        Content interviewVideo1 = new Content(presentableId, ContentEntry.TYPE_LESSON_MEDIA, initiativeTitle, initiativeId, 1);
        Content interviewAudio1 = new Content(questionsAheadId, ContentEntry.TYPE_LESSON_MEDIA, LESSON_AUDIO.getTitle(), audioId, 2);
        Content interviewMock1 = new Content(questionsAheadId, ContentEntry.TYPE_LESSON_PRACTICE_INTERIEW, INTERVIEW.getTitle(), interviewObjId, 3);
        Content presentationVideo1 = new Content(impactId, ContentEntry.TYPE_LESSON_MEDIA, teamworkTitle, teamworkId, 2);
        Content[] contents = new Content[]{interviewVideo1, interviewAudio1, interviewMock1, presentationVideo1};

        for (Content content : contents) {
            long id = db.insertOrThrow(ContentEntry.TABLE_NAME, null, content.toContentValues());
            Log.d(TAG, "Inserted content " + content.getTitle() + " with id " + id + " to topic " + content.getTopicId());
        }

        Cursor cursor = db.query(
                ContentEntry.TABLE_NAME,
                ContentEntry.ALL_COLUMN_NAMES,
                ContentEntry.COLUMN_NAME_TOPIC_ID + " = ?",
                new String[]{presentableId + ""},
                null, null, null);

        while (cursor.moveToNext()) {
            Content content = new Content();
            content.setValues(cursor);
            Log.d("TEST", "fetched stuff " + content);
        }
    }

    public static long getMediaId(SQLiteDatabase db, String filename) {
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, new String[]{MediaEntry._ID}, MediaEntry.COLUMN_NAME_FILENAME + " = ?", new String[]{filename}, null, null, MediaEntry._ID, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(MediaEntry._ID)) : 0;
    }

    public static long getModuleId(SQLiteDatabase db, String title) {
        Cursor cursor = db.query(ModuleEntry.TABLE_NAME, new String[]{ModuleEntry._ID}, ModuleEntry.COLUMN_NAME_TITLE + " = ?", new String[]{title}, null, null, ModuleEntry._ID, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(ModuleEntry._ID)) : 0;
    }

    public static long getInterviewId(SQLiteDatabase db) {
        Cursor cursor = db.query(AppContract.InterviewEntry.TABLE_NAME, new String[]{AppContract.InterviewEntry._ID}, AppContract.InterviewEntry.COLUMN_NAME_TITLE + " = ?", new String[]{INTERVIEW.getTitle()}, null, null, null, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(AppContract.InterviewEntry._ID)) : 0;
    }

    public static long getTopicId(SQLiteDatabase db, String title) {
        Cursor cursor = db.query(TopicEntry.TABLE_NAME, new String[]{TopicEntry._ID}, TopicEntry.COLUMN_NAME_TITLE + " = ?", new String[]{title}, null, null, null, "1");
        return cursor.moveToNext() ? cursor.getLong(cursor.getColumnIndex(TopicEntry._ID)) : 0;
    }

    public static Media getMedia(SQLiteDatabase db) {
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, null, null, null, null, null, MediaEntry._ID, "1");
        Media sample = new Media();

        if (cursor.moveToNext()) {
            sample.setValues(cursor);
        }

        return new Media();
    }
}
