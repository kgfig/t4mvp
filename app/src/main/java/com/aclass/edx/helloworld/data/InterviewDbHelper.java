package com.aclass.edx.helloworld.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.aclass.edx.helloworld.data.InterviewContract.InterviewEntry;

/**
 * Created by ertd on 2/18/2017.
 */

public class InterviewDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "interview.db";

    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS";
    private static final String COMMA = ", ";
    private static final String INT_PK_AUTOINC = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String TEXT_ALLOW_NULL = " TEXT";
    private static final String UNIQUE = " UNIQUE";
    private static final String FK = "FOREIGN KEY ";
    private static final String FK_REFERENCES = " REFERENCES ";
    private static final String END_SQL_STATEMENT = ";";

    private static final String SQL_CREATE_ENTRIES =
            CREATE_TABLE + InterviewEntry.TABLE_NAME + " (" +
                    InterviewEntry._ID + INT_PK_AUTOINC + COMMA +
                    InterviewEntry.COLUMN_TITLE + TEXT_NOT_NULL +
                    ")" + END_SQL_STATEMENT;

    private static final String SQL_DELETE_ENTRIES =
            DROP_TABLE_IF_EXISTS + InterviewEntry.TABLE_NAME + END_SQL_STATEMENT;

    public InterviewDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
