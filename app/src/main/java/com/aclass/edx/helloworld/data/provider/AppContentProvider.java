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
import android.util.Log;

import com.aclass.edx.helloworld.data.DBHelper;
import com.aclass.edx.helloworld.data.exceptions.UnsupportedURIException;
import com.aclass.edx.helloworld.data.contracts.AppContract;
import com.aclass.edx.helloworld.data.models.Interview;
import com.aclass.edx.helloworld.data.models.InterviewQuestion;
import com.aclass.edx.helloworld.data.models.Module;


import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewQuestionEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.TopicEntry;

public class AppContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AppContract.AUTHORITY, MediaEntry.TABLE_NAME, ProviderConstants.LIST_MEDIA);
        uriMatcher.addURI(AppContract.AUTHORITY, MediaEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_MEDIA);
        uriMatcher.addURI(AppContract.AUTHORITY, ModuleEntry.TABLE_NAME, ProviderConstants.LIST_MODULE);
        uriMatcher.addURI(AppContract.AUTHORITY, ModuleEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_MODULE);
        uriMatcher.addURI(AppContract.AUTHORITY, TopicEntry.TABLE_NAME, ProviderConstants.LIST_TOPIC);
        uriMatcher.addURI(AppContract.AUTHORITY, TopicEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_TOPIC);
        uriMatcher.addURI(AppContract.AUTHORITY, ContentEntry.TABLE_NAME, ProviderConstants.LIST_CONTENT);
        uriMatcher.addURI(AppContract.AUTHORITY, ContentEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_CONTENT);
        uriMatcher.addURI(AppContract.AUTHORITY, InterviewEntry.TABLE_NAME, ProviderConstants.LIST_INTERVIEW);
        uriMatcher.addURI(AppContract.AUTHORITY, InterviewEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_INTERVIEW);
        uriMatcher.addURI(AppContract.AUTHORITY, InterviewQuestionEntry.TABLE_NAME, ProviderConstants.LIST_INTERVIEW_QUESTION);
        uriMatcher.addURI(AppContract.AUTHORITY, InterviewQuestionEntry.TABLE_NAME + "/#", ProviderConstants.ITEM_INTERVIEW_QUESTION);
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
            case ProviderConstants.LIST_TOPIC:
                queryBuilder.setTables(TopicEntry.TABLE_NAME);
                break;
            case ProviderConstants.LIST_CONTENT:
                queryBuilder.setTables(ContentEntry.TABLE_NAME);
                break;
            case ProviderConstants.LIST_INTERVIEW:
                queryBuilder.setTables(InterviewEntry.TABLE_NAME);
                break;
            case ProviderConstants.LIST_INTERVIEW_QUESTION:
                queryBuilder.setTables(InterviewQuestionEntry.TABLE_NAME);
                break;
            case ProviderConstants.ITEM_MEDIA:
                queryBuilder.setTables(MediaEntry.TABLE_NAME);
                queryBuilder.appendWhere(MediaEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_MODULE:
                queryBuilder.setTables(ModuleEntry.TABLE_NAME);
                queryBuilder.appendWhere(ModuleEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_TOPIC:
                queryBuilder.setTables(TopicEntry.TABLE_NAME);
                queryBuilder.appendWhere(TopicEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_CONTENT:
                queryBuilder.setTables(ContentEntry.TABLE_NAME);
                queryBuilder.appendWhere(ContentEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_INTERVIEW:
                queryBuilder.setTables(InterviewEntry.TABLE_NAME);
                queryBuilder.appendWhere(InterviewEntry._ID + " = " + uri.getLastPathSegment());
                break;
            case ProviderConstants.ITEM_INTERVIEW_QUESTION:
                queryBuilder.setTables(InterviewQuestionEntry.TABLE_NAME);
                queryBuilder.appendWhere(InterviewQuestionEntry._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedURIException(uri);
        }

        SQLiteDatabase db = DBHelper.getInstance(getContext()).getReadableDatabase();
        Log.d("Provider", "Selection: " + selection);
        Log.d("Provider", "Selection args; " + (selectionArgs != null && selectionArgs.length > 0 ? selectionArgs[0] : " no args"));
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case ProviderConstants.LIST_MODULE:
                return ProviderConstants.CONTENT_TYPE_MODULE;
            case ProviderConstants.LIST_TOPIC:
                return ProviderConstants.CONTENT_TYPE_TOPIC;
            case ProviderConstants.LIST_CONTENT:
                return ProviderConstants.CONTENT_TYPE_CONTENT;
            case ProviderConstants.LIST_MEDIA:
                return ProviderConstants.CONTENT_TYPE_MEDIA;
            case ProviderConstants.LIST_INTERVIEW:
                return ProviderConstants.CONTENT_TYPE_INTERVIEW;
            case ProviderConstants.LIST_INTERVIEW_QUESTION:
                return ProviderConstants.CONTENT_TYPE_INTERVIEW_QUESTION;
            case ProviderConstants.ITEM_MODULE:
                return ProviderConstants.CONTENT_ITEM_TYPE_MODULE;
            case ProviderConstants.ITEM_TOPIC:
                return ProviderConstants.CONTENT_ITEM_TYPE_TOPIC;
            case ProviderConstants.ITEM_CONTENT:
                return ProviderConstants.CONTENT_ITEM_TYPE_CONTENT;
            case ProviderConstants.ITEM_MEDIA:
                return ProviderConstants.CONTENT_ITEM_TYPE_MEDIA;
            case ProviderConstants.ITEM_INTERVIEW:
                return ProviderConstants.CONTENT_ITEM_TYPE_INTERVIEW;
            case ProviderConstants.ITEM_INTERVIEW_QUESTION:
                return ProviderConstants.CONTENT_ITEM_TYPE_INTERVIEW_QUESTION;
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
            case ProviderConstants.LIST_MODULE:
                id = db.insertOrThrow(ModuleEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_TOPIC:
                id = db.insertOrThrow(TopicEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_CONTENT:
                id = db.insertOrThrow(ContentEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_MEDIA:
                id = db.insertOrThrow(MediaEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_INTERVIEW:
                id = db.insertOrThrow(InterviewEntry.TABLE_NAME, null, values);
                break;
            case ProviderConstants.LIST_INTERVIEW_QUESTION:
                id = db.insertOrThrow(InterviewQuestionEntry.TABLE_NAME, null, values);
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
        String id;

        switch (uriType) {
            case ProviderConstants.LIST_MODULE:
                rowsDeleted = db.delete(ModuleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_TOPIC:
                rowsDeleted = db.delete(TopicEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_CONTENT:
                rowsDeleted = db.delete(ContentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_MEDIA:
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_INTERVIEW:
                rowsDeleted = db.delete(InterviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.LIST_INTERVIEW_QUESTION:
                rowsDeleted = db.delete(InterviewQuestionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ProviderConstants.ITEM_MODULE:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(ModuleEntry.TABLE_NAME, ModuleEntry._ID + " = ? ", new String[]{id});
                break;
            case ProviderConstants.ITEM_TOPIC:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(TopicEntry.TABLE_NAME, TopicEntry._ID + " = ?", new String[]{id});
                break;
            case ProviderConstants.ITEM_CONTENT:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(ContentEntry.TABLE_NAME, ContentEntry._ID + " = ? ", new String[]{id});
                break;
            case ProviderConstants.ITEM_MEDIA:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, MediaEntry._ID + " = ? ", new String[]{id});
                break;
            case ProviderConstants.ITEM_INTERVIEW:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(InterviewEntry.TABLE_NAME, InterviewEntry._ID + " = ? ", new String[]{id});
                break;
            case ProviderConstants.ITEM_INTERVIEW_QUESTION:
                id = uri.getLastPathSegment();
                rowsDeleted = db.delete(InterviewQuestionEntry.TABLE_NAME, InterviewQuestionEntry._ID + " = ? ", new String[]{id});
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
