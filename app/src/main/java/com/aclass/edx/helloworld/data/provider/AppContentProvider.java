package com.aclass.edx.helloworld.data.provider;

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

import com.aclass.edx.helloworld.data.DBHelper;
import com.aclass.edx.helloworld.data.exceptions.UnsupportedURIException;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Module;


import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;

public class AppContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AppContract.AUTHORITY, MediaEntry.TABLE_NAME, ProviderConstants.LIST_MEDIA);
        uriMatcher.addURI(AppContract.AUTHORITY, MediaEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_MEDIA);
        uriMatcher.addURI(AppContract.AUTHORITY, ModuleEntry.TABLE_NAME, ProviderConstants.LIST_MODULE);
        uriMatcher.addURI(AppContract.AUTHORITY, ModuleEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_MODULE);
        uriMatcher.addURI(AppContract.AUTHORITY, ContentEntry.TABLE_NAME, ProviderConstants.LIST_CONTENT);
        uriMatcher.addURI(AppContract.AUTHORITY, ContentEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_CONTENT);
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
        switch (uriType) {
            case ProviderConstants.LIST_MEDIA:
                queryBuilder.setTables(MediaEntry.TABLE_NAME);
                break;
            case ProviderConstants.LIST_MODULE:
                queryBuilder.setTables(ModuleEntry.TABLE_NAME);
                break;
            case ProviderConstants.LIST_CONTENT:
                queryBuilder.setTables(ContentEntry.TABLE_NAME);
                break;
            case ProviderConstants.ITEM_MEDIA:
                queryBuilder.setTables(MediaEntry.TABLE_NAME);
                queryBuilder.appendWhere(MediaEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_MODULE:
                queryBuilder.setTables(ModuleEntry.TABLE_NAME);
                queryBuilder.appendWhere(ModuleEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_CONTENT:
                queryBuilder.setTables(ContentEntry.TABLE_NAME);
                queryBuilder.appendWhere(ContentEntry._ID + " = " + uri.getLastPathSegment());
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
        switch (uriType) {
            case ProviderConstants.LIST_CONTENT:
                return ProviderConstants.CONTENT_TYPE_CONTENT;
            case ProviderConstants.LIST_MEDIA:
                return ProviderConstants.CONTENT_TYPE_MEDIA;
            case ProviderConstants.LIST_MODULE:
                return ProviderConstants.CONTENT_TYPE_MODULE;
            case ProviderConstants.ITEM_CONTENT:
                return ProviderConstants.CONTENT_ITEM_TYPE_CONTENT;
            case ProviderConstants.ITEM_MEDIA:
                return ProviderConstants.CONTENT_ITEM_TYPE_MEDIA;
            case ProviderConstants.ITEM_MODULE:
                return ProviderConstants.CONTENT_ITEM_TYPE_MODULE;
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

        switch (uriType) {
            case ProviderConstants.LIST_MEDIA:
                id = db.insertOrThrow(MediaEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_MODULE:
                id = db.insertOrThrow(ModuleEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_CONTENT:
                id = db.insertOrThrow(ContentEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedURIException(uri);
        }

        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        }

        throw new SQLException("Error inserting values into database");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getInstance(getContext()).getWritableDatabase();
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;
        String id = "";

        switch (uriType) {
            case ProviderConstants.LIST_CONTENT:
                rowsDeleted = db.delete(ContentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_MEDIA:
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_MODULE:
                rowsDeleted = db.delete(ModuleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.ITEM_CONTENT:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(ContentEntry.TABLE_NAME, ContentEntry._ID + " = ? ", new String[]{id});
                break;
            case ProviderConstants.ITEM_MEDIA:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, MediaEntry._ID + " = ? ", new String[]{id});
                break;
            case ProviderConstants.ITEM_MODULE:
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
