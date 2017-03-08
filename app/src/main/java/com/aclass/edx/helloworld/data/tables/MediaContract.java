package com.aclass.edx.helloworld.data.tables;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ertd on 2/21/2017.
 */

public final class MediaContract {

    private MediaContract() {}

    public static final String BASE_PATH = "media";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" + BASE_PATH;
    public static final int LIST_URI_CODE = 100;
    public static final int ID_URI_CODE = 101;

    public static class MediaEntry implements BaseColumns {
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FILENAME = "filename";
        public static final String COLUMN_NAME_TYPE = "type";

        // Types
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;

    }

}
