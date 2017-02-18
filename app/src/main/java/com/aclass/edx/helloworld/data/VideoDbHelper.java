package com.aclass.edx.helloworld.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.aclass.edx.helloworld.data.VideoContract.VideoEntry;

/**
 * Created by ertd on 2/18/2017.
 */

public class VideoDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "video.db";
    public static final int DATABASE_VERSION = 1;

    private static final String INT_PK_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String SQL_CREATE =
            "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +
                    VideoEntry._ID + INT_PK_AUTOINCREMENT + "," +
                    VideoEntry.COLUMN_TITLE + TEXT_NOT_NULL + ");";
    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME + ";";

    public VideoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(VideoDbHelper.class.getName(), SQL_CREATE);
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
