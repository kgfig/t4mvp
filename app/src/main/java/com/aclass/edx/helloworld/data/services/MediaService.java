package com.aclass.edx.helloworld.data.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aclass.edx.helloworld.data.exceptions.InconsistentDataException;
import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.tables.MediaContract;

import java.util.ArrayList;
import java.util.List;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

/**
 * Created by ertd on 2/21/2017.
 */

public class MediaService {
    private SQLiteDatabase db;

    public MediaService(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertSingleRow(String title, String filename, int type) {
        if (db.isReadOnly()) return -1;
        ContentValues values = new ContentValues();
        values.put(MediaEntry.COLUMN_NAME_TITLE, title);
        values.put(MediaEntry.COLUMN_NAME_FILENAME, filename);
        values.put(MediaEntry.COLUMN_NAME_TYPE, type);

        return db.insert(MediaEntry.TABLE_NAME, null, values);
    }

    private String columnsToSelection(String[] columns) {
        if (columns == null || columns.length == 0) {
            return null;
        } else {
            StringBuilder selection = new StringBuilder();
            for (int i = 0; i < columns.length - 1; i++) {
                selection.append(columns[i]);
                selection.append(" = ? ,");
            }
            selection.append(columns[columns.length - 1]);
            selection.append(" = ?");
            return selection.toString();
        }
    }

    public List<String> getAllMediaTitles() {
        List<String> titles = new ArrayList<String>();
        Cursor cursor = db.query(
                MediaEntry.TABLE_NAME,
                new String[]{MediaEntry.COLUMN_NAME_TITLE},
                null,
                null,
                null,
                null,
                MediaEntry.COLUMN_NAME_TITLE);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaEntry.COLUMN_NAME_TITLE));
            titles.add(title);
        }

        cursor.close();

        return titles;
    }

    public List<Media> getAllVideoOrderById() {
        String[] projection = {
                MediaEntry._ID,
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_FILENAME,
                MediaEntry.COLUMN_NAME_TYPE
        };

        String[] column = {
                MediaEntry.COLUMN_NAME_TYPE
        };

        String[] requiredType = { MediaEntry.TYPE_VIDEO + "" };

        return getMedia(projection, column, requiredType, MediaEntry._ID);
    }

    public Media getMediaById(long id) throws InconsistentDataException {
        String[] projection = {
                MediaEntry._ID,
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_FILENAME,
                MediaEntry.COLUMN_NAME_TYPE
        };

        List<Media> results = getMedia(
                projection,
                new String[]{MediaEntry._ID},
                new String[]{id + ""},
                MediaEntry._ID
        );

        if (results.isEmpty()) {
            return new Media();
        } else if (results.size() > 1) {
            throw new InconsistentDataException("Multiple rows returned for ID " + id);
        } else {
            return results.remove(0);
        }
    }

    public List<Media> getMedia(String[] projection, String[] selectionColumns, String[] selectionArgs, String sortOrder) {
        List<Media> mediaFiles = new ArrayList<Media>();
        String selection = columnsToSelection(selectionColumns);

        Cursor cursor = db.query(
                MediaEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaEntry.COLUMN_NAME_TITLE));
            String filename = cursor.getString(cursor.getColumnIndexOrThrow(MediaEntry.COLUMN_NAME_FILENAME));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(MediaEntry.COLUMN_NAME_TYPE));

            mediaFiles.add(new Media(id, title, filename, type));
        }

        cursor.close();
        return mediaFiles;
    }

    public void deleteMedia(String[] selectionColumns, String[] selectionArgs) {
        String selection = columnsToSelection(selectionColumns);
        db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
    }
}

