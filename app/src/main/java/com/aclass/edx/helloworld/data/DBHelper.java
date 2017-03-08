package com.aclass.edx.helloworld.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

/**
 * Created by ertd on 2/21/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "t4.db";
    public static final int DATABASE_VERSION = 1;

    private static final String INT_PK_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String INT_NOT_NULL = " INTEGER NOT NULL";
    private static final String SQL_CREATE_DB =
            "CREATE TABLE " + MediaEntry.TABLE_NAME + " (" +
                    MediaEntry._ID + INT_PK_AUTOINCREMENT + ", " +
                    MediaEntry.COLUMN_NAME_TITLE + TEXT_NOT_NULL + ", " +
                    MediaEntry.COLUMN_NAME_FILENAME + TEXT_NOT_NULL + ", " +
                    MediaEntry.COLUMN_NAME_TYPE + INT_NOT_NULL + ");";

    private static final String SQL_DROP_TABLES =
            "DROP TABLE IF EXISTS " + MediaEntry.TABLE_NAME + ";";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_DB); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If data is replicated somewhere else
        db.execSQL(SQL_DROP_TABLES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
