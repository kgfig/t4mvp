package com.aclass.edx.helloworld.data.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ertd on 2/21/2017.
 * Comments based on Android UserDictionary class
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.1_r1/android/provider/UserDictionary.java
 */

public final class AppContract {

    // Authority identifier for AppContentProvider
    public static final String AUTHORITY = "com.aclass.edx.helloworld";
    // Content URL for AppContentProvider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private AppContract() {
    }

    public static class MediaEntry implements BaseColumns {
        // Schema
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FILENAME = "filename";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String[] ALL_COLUMN_NAMES = {
                MediaEntry._ID,
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_FILENAME,
                MediaEntry.COLUMN_NAME_TYPE
        };
        // Types of media instances
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
        // Content URL for this table
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    }

    public static class ModuleEntry implements BaseColumns {
        // Schema
        public static final String TABLE_NAME = "module";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String[] ALL_COLUMN_NAMES = {
                ModuleEntry._ID,
                ModuleEntry.COLUMN_NAME_TITLE
        };
        // Content URL for this table
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    }

    public static class ContentEntry implements BaseColumns {
        // Schema
        public static final String TABLE_NAME = "content";
        public static final String COLUMN_NAME_MODULE_ID = "moduleId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_CONTENT_ID = "contentId";
        public static final String COLUMN_NAME_SEQ_NUM = "seqNum";
        public static final String[] ALL_COLUMN_NAMES = {
                ContentEntry._ID,
                ContentEntry.COLUMN_NAME_MODULE_ID,
                ContentEntry.COLUMN_NAME_TITLE,
                ContentEntry.COLUMN_NAME_TYPE,
                ContentEntry.COLUMN_NAME_CONTENT_ID,
                ContentEntry.COLUMN_NAME_SEQ_NUM
        };
        public static final String FOREIGN_KEY_MODULE_ID = "FOREIGN KEY (" +
                ContentEntry.COLUMN_NAME_MODULE_ID + ") REFERENCES " +
                ModuleEntry.TABLE_NAME + " (" + ModuleEntry._ID + ")";
        public static final String UNIQUE_MODULE_ID_CONTENT_ID = "UNIQUE (" +
                ContentEntry.COLUMN_NAME_MODULE_ID + "," + ContentEntry.COLUMN_NAME_CONTENT_ID + ")";
        // Types
        public static final int TYPE_LESSON_MEDIA = 1;
        // Content URL for this table
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    }
}
