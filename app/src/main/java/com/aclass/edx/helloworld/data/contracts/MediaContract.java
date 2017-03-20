package com.aclass.edx.helloworld.data.contracts;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ertd on 2/21/2017.
 * Comments based on Android UserDictionary class
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.1_r1/android/provider/UserDictionary.java
 */

public final class MediaContract {

    // Authority identifier for MediaContentProvider
    public static final String AUTHORITY = "com.aclass.edx.helloworld.data.provider";
    // Content URL for MediaContentProvider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // App identifier for mime type
    private static final String APP_PACKAGE = "vnd.helloworld";

    private MediaContract() {}

    public static class MediaEntry implements BaseColumns {
        // Schema
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FILENAME = "filename";
        public static final String COLUMN_NAME_TYPE = "type";
        // Types of media instances
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;

        // TODO discuss where to include these constants
        /**
         * Adding these in the Contract to avoid clutter in MediaContentProvider
         * because there are too many table constants to put in just one class
         */

        // Content URL for this table
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        // MIME types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + APP_PACKAGE + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + APP_PACKAGE + "." + TABLE_NAME;
        // URI types
        public static final int LIST = 100;
        public static final int ITEM = 101;
    }

    public static class ModuleEntry implements BaseColumns {
        // Schema
        public static final String TABLE_NAME = "module";
        public static final String COLUMN_NAME_TITLE = "title";
        // Content URL for this table
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        // MIME types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + APP_PACKAGE + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + APP_PACKAGE + "." + TABLE_NAME;
        // URI types
        public static final int LIST = 200;
        public static final int ITEM = 201;
    }

    public static class ContentEntry implements BaseColumns {
        // Schema
        public static final String TABLE_NAME = "content";
        public static final String COLUMN_NAME_MODULE_ID = "module_id";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_CONTENT_ID = "content_id";
        // Types
        public static final int TYPE_LESSON_MEDIA = 1;
        // Content URL for this table
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        // MIME types
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + APP_PACKAGE + "." + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + APP_PACKAGE + "." + TABLE_NAME;
        // URI types
        public static final int LIST = 300;
        public static final int ITEM = 301;
    }
}
