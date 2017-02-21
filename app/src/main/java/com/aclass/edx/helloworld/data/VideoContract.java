package com.aclass.edx.helloworld.data;

import android.provider.BaseColumns;

/**
 * Created by ertd on 2/18/2017.
 */

public final class VideoContract {

    public static final class VideoEntry implements BaseColumns {

        public static final String TABLE_NAME = "Video";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_FILENAME = "filename";
    }
}
