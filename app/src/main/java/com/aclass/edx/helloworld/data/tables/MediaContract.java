package com.aclass.edx.helloworld.data.tables;

import android.provider.BaseColumns;

/**
 * Created by ertd on 2/21/2017.
 */

public final class MediaContract {

    private MediaContract() {}

    public static class MediaEntry implements BaseColumns {
        public static final String TABLE_NAME = "Video";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FILENAME = "filename";
        public static final String COLUMN_NAME_TYPE = "type";

        // Types
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
    }

}
