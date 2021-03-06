package com.aclass.edx.helloworld.data.asynctasks;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

import com.aclass.edx.helloworld.data.models.Media;
import com.aclass.edx.helloworld.data.contracts.AppContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ertd on 3/9/2017.
 */

public abstract class AsyncInsertMedia extends AsyncTask<Media, Void, List> {

    private ContentResolver contentResolver;

    public AsyncInsertMedia(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    protected List doInBackground(Media... params) {
        List<Uri> uris = new ArrayList<Uri>(params.length);

        for (Media param : params) {
            Uri resultUri = contentResolver.insert(
                    AppContract.MediaEntry.CONTENT_URI,
                    param.toContentValues()
            );

            uris.add(resultUri);
        }

        return uris;
    }

    @Override
    protected abstract void onPostExecute(List list);
}
