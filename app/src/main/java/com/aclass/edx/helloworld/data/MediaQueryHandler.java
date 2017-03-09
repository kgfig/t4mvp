package com.aclass.edx.helloworld.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by ertd on 3/9/2017.
 */

public class MediaQueryHandler extends AsyncQueryHandler {

    public MediaQueryHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);


    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
    }
}
