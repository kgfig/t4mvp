package com.aclass.edx.helloworld.data.provider;

import android.content.ContentResolver;

import com.aclass.edx.helloworld.data.models.InterviewQuestion;

import static com.aclass.edx.helloworld.data.contracts.AppContract.ContentEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.InterviewQuestionEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.MediaEntry;
import static com.aclass.edx.helloworld.data.contracts.AppContract.ModuleEntry;

/**
 * Created by tictocproject on 01/04/2017.
 */

final class ProviderConstants {

    private ProviderConstants() {
    }

    // App identifier for MIME types
    public static final String APP_PACKAGE = "vnd.tictoctechtalk";
    // MIME types
    public static final String CONTENT_TYPE_CONTENT = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + ContentEntry.TABLE_NAME;
    public static final String CONTENT_TYPE_MEDIA = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + MediaEntry.TABLE_NAME;
    public static final String CONTENT_TYPE_MODULE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + ModuleEntry.TABLE_NAME;
    public static final String CONTENT_TYPE_INTERVIEW = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + InterviewEntry.TABLE_NAME;
    public static final String CONTENT_TYPE_INTERVIEW_QUESTION = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + InterviewQuestionEntry.TABLE_NAME;
    // MIME types for items
    public static final String CONTENT_ITEM_TYPE_CONTENT = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + ContentEntry.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE_MEDIA = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + MediaEntry.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE_MODULE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + ModuleEntry.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE_INTERVIEW = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + InterviewEntry.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE_INTERVIEW_QUESTION = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ProviderConstants.APP_PACKAGE + "." + InterviewQuestionEntry.TABLE_NAME;
    // URI types
    public static final int LIST_CONTENT = 300;
    public static final int ITEM_CONTENT = 301;
    public static final int LIST_MEDIA = 100;
    public static final int ITEM_MEDIA = 101;
    public static final int LIST_MODULE = 200;
    public static final int ITEM_MODULE = 201;
    public static final int LIST_INTERVIEW = 400;
    public static final int ITEM_INTERVIEW = 401;
    public static final int LIST_INTERVIEW_QUESTION = 500;
    public static final int ITEM_INTERVIEW_QUESTION = 501;
}
