package com.aclass.edx.helloworld.data.contracts;

import android.provider.BaseColumns;

/**
 * Created by ertd on 2/18/2017.
 */

public final class InterviewContract {

    public static final class InterviewEntry implements BaseColumns {

        public static final String TABLE_NAME = "Interview";

        public static final String COLUMN_TITLE = "title";
    }

    public static final class InterviewItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "Interview_Item";

        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_INTERVIEW_ID = "interview_id";
        public static final String COLUMN_SEQ_NUM = "seq_num";

    }

    public static final class InterviewResponseEntry implements BaseColumns {

        public static final String TABLE_NAME = "Interview_Response";

        // When first accessed
        // Creates on first access
        // getReadableDatabase()
        // getWritableDatabase()
        // connects
        // update
    }
}
