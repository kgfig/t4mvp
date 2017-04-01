package com.aclass.edx.helloworld.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewQuestionEntry;

import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.utils.TempUtils;

/**
 * Created by ertd on 2/21/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "t4.db";
    public static final int DATABASE_VERSION = 1;

    private class DBQueries {

        private static final String INT_PK_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
        private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
        private static final String INT_NOT_NULL = " INTEGER NOT NULL";
        private static final String CREATE_TABLE = "CREATE TABLE ";
        private static final String COMMA = ",";

        public static final String CREATE_TABLE_MEDIA = CREATE_TABLE + MediaEntry.TABLE_NAME + " (" +
                MediaEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
                MediaEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + COMMA +
                MediaEntry.COLUMN_NAME_FILENAME + TEXT_NOT_NULL + COMMA +
                MediaEntry.COLUMN_NAME_TYPE + INT_NOT_NULL + ");";

        public static final String CREATE_TABLE_MODULE = CREATE_TABLE + ModuleEntry.TABLE_NAME + " (" +
                ModuleEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
                ModuleEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + ");";

        public static final String CREATE_TABLE_CONTENT = CREATE_TABLE + ContentEntry.TABLE_NAME + " (" +
                ContentEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
                ContentEntry.COLUMN_NAME_MODULE_ID + INT_NOT_NULL + COMMA +
                ContentEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + COMMA +
                ContentEntry.COLUMN_NAME_TYPE + INT_NOT_NULL + COMMA +
                ContentEntry.COLUMN_NAME_CONTENT_ID + INT_NOT_NULL + COMMA +
                ContentEntry.COLUMN_NAME_SEQ_NUM + INT_NOT_NULL + COMMA +
                ContentEntry.FOREIGN_KEY_MODULE_ID + COMMA +
                ContentEntry.UNIQUE_COMPOSITE_KEY_MODULE_CONTENT + ");";

        public static final String CREATE_TABLE_INTERVIEW = CREATE_TABLE + InterviewEntry.TABLE_NAME + " (" +
                InterviewEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
                InterviewEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + ");";

        public static final String CREATE_TABLE_INTERVIEW_QUESTION = CREATE_TABLE + InterviewQuestionEntry.TABLE_NAME + " (" +
                InterviewQuestionEntry._ID + INT_PK_AUTOINCREMENT + COMMA +
                InterviewQuestionEntry.COLUMN_NAME_INTERVIEW_ID + INT_NOT_NULL + COMMA +
                InterviewQuestionEntry.COLUMN_NAME_QUESTION + TEXT_NOT_NULL + COMMA +
                InterviewQuestionEntry.COLUMN_NAME_MEDIA_ID + INT_NOT_NULL + COMMA +
                InterviewQuestionEntry.COLUMN_NAME_SEQ_NUM + INT_NOT_NULL + COMMA +
                InterviewQuestionEntry.FOREIGN_KEY_INTERVIEW_ID + COMMA +
                InterviewQuestionEntry.FOREIGN_KEY_MEDIA_ID + ")";

        private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS %s;\n";
    }

    // Singleton
    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, DBQueries.CREATE_TABLE_INTERVIEW);
        Log.d(TAG, DBQueries.CREATE_TABLE_INTERVIEW_QUESTION);
        db.execSQL(DBQueries.CREATE_TABLE_MEDIA);
        db.execSQL(DBQueries.CREATE_TABLE_MODULE);
        db.execSQL(DBQueries.CREATE_TABLE_CONTENT);
        db.execSQL(DBQueries.CREATE_TABLE_INTERVIEW);
        db.execSQL(DBQueries.CREATE_TABLE_INTERVIEW_QUESTION);
        TempUtils.insertMediaTestData(db);
        TempUtils.insertModuleTestData(db);
        TempUtils.insertContentTestData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If data is replicated somewhere else
        db.execSQL(getDropTablesQuery());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static String getDropTablesQuery() {
        String[] tableNames = {
                MediaEntry.TABLE_NAME,
                ModuleEntry.TABLE_NAME,
                ContentEntry.TABLE_NAME,
                InterviewEntry.TABLE_NAME,
                InterviewQuestionEntry.TABLE_NAME
        };
        StringBuilder query = new StringBuilder();

        for (String tableName : tableNames) {
            String deleteTableQuery = String.format(DBQueries.DROP_TABLE_IF_EXISTS, tableName);
            query.append(deleteTableQuery);
        }

        return query.toString();
    }

}
