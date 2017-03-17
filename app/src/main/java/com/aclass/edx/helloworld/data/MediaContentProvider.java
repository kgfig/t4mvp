package com.aclass.edx.helloworld.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.aclass.edx.helloworld.data.exceptions.UnsupportedURIException;
import com.aclass.edx.helloworld.data.contracts.MediaContract;

import static com.aclass.edx.helloworld.data.contracts.MediaContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.MediaContract.ModuleEntry;

public class MediaContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MediaContract.AUTHORITY, MediaEntry.TABLE_NAME, MediaEntry.LIST);
        uriMatcher.addURI(MediaContract.AUTHORITY, MediaEntry.TABLE_NAME + "/#", MediaEntry.ITEM);
        uriMatcher.addURI(MediaContract.AUTHORITY, ModuleEntry.TABLE_NAME, ModuleEntry.LIST);
        uriMatcher.addURI(MediaContract.AUTHORITY, ModuleEntry.TABLE_NAME + "/#", ModuleEntry.ITEM);
    }

    @Override
    public boolean onCreate() {
        DBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = uriMatcher.match(uri);
        switch(uriType) {
            case MediaEntry.LIST:
                queryBuilder.setTables(MediaEntry.TABLE_NAME);
                break;
            case MediaEntry.ITEM:
                queryBuilder.setTables(MediaEntry.TABLE_NAME);
                queryBuilder.appendWhere(MediaEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ModuleEntry.LIST:
                queryBuilder.setTables(ModuleEntry.TABLE_NAME);
                break;
            case ModuleEntry.ITEM:
                queryBuilder.setTables(ModuleEntry.TABLE_NAME);
                queryBuilder.appendWhere(ModuleEntry._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedURIException(uri);
        }

        SQLiteDatabase db = DBHelper.getInstance(getContext()).getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = uriMatcher.match(uri);
        switch(uriType) {
            case MediaEntry.LIST:
                return MediaEntry.CONTENT_TYPE;
            case MediaEntry.ITEM:
                return MediaEntry.CONTENT_ITEM_TYPE;
            case ModuleEntry.LIST:
                return ModuleEntry.CONTENT_TYPE;
            case ModuleEntry.ITEM:
                return ModuleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedURIException(uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBHelper.getInstance(getContext()).getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        long id = 0;

        switch(uriType) {
            case MediaEntry.LIST:
                id = db.insertOrThrow(MediaEntry.TABLE_NAME, null, values);
                break;
            case ModuleEntry.LIST:
                id = db.insertOrThrow(ModuleEntry.TABLE_NAME, null, values);
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
        SQLiteDatabase db = DBHelper.getInstance(getContext()).getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;
        String id = "";

        switch(uriType) {
            case MediaEntry.LIST:
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MediaEntry.ITEM:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, MediaEntry._ID + " = ? ", new String[]{id});
                break;
            case ModuleEntry.LIST:
                rowsDeleted = db.delete(ModuleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ModuleEntry.ITEM:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(ModuleEntry.TABLE_NAME, ModuleEntry._ID + " = ? ", new String[]{id});
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
