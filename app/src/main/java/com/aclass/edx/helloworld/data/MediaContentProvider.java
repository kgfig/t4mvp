package com.aclass.edx.helloworld.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.aclass.edx.helloworld.data.exceptions.UnsupportedURIException;
import com.aclass.edx.helloworld.data.tables.MediaContract;

import static com.aclass.edx.helloworld.data.tables.MediaContract.MediaEntry;

public class MediaContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.aclass.edx.helloworld.data.provider";

    // TODO discuss where to include these constants (see comments in Contract)
    // Adding these in the Contract to avoid clutter in MediaContentProvider
    // because there are so many table constants to put in just one class
    private static final String APP_PACKAGE = "helloworld";
    private static final String LIST_TYPE_MEDIA = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + APP_PACKAGE + "/" + MediaContract.BASE_PATH;
    private static final String ITEM_TYPE_MEDIA = ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + APP_PACKAGE + "/" + MediaContract.BASE_PATH;

    public static final Uri MEDIA_URI = Uri.parse("content://" + AUTHORITY + "/" + MediaContract.BASE_PATH);

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MediaContract.BASE_PATH, MediaContract.LIST_URI_CODE);
        uriMatcher.addURI(AUTHORITY, MediaContract.BASE_PATH + "/#", MediaContract.ID_URI_CODE);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MediaEntry.TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch(uriType) {
            case MediaContract.LIST_URI_CODE:
                break;
            case MediaContract.ID_URI_CODE:
                queryBuilder.appendWhere(MediaEntry._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedURIException(uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = uriMatcher.match(uri);
        switch(uriType) {
            case MediaContract.LIST_URI_CODE:
                return LIST_TYPE_MEDIA;
            case MediaContract.ID_URI_CODE:
                return ITEM_TYPE_MEDIA;
            default:
                throw new UnsupportedURIException(uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        long id = 0;

        switch(uriType) {
            case MediaContract.LIST_URI_CODE:
                id = db.insertOrThrow(MediaEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedURIException(uri);
        }

        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        }

        throw new SQLException("Error inserting into table " + MediaEntry.TABLE_NAME);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch(uriType) {
            case MediaContract.LIST_URI_CODE:
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MediaContract.ID_URI_CODE:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(MediaEntry.TABLE_NAME, MediaEntry._ID + " = " + id, null);
                } else {
                    rowsDeleted = db.delete(MediaEntry.TABLE_NAME, MediaEntry._ID + " = " + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new UnsupportedURIException(uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
