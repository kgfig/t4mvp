package com.aclass.edx.helloworld.data.asynctasks;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.aclass.edx.helloworld.data.contracts.MediaContract;
import com.aclass.edx.helloworld.data.models.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tictocproject on 17/03/2017.
 */

public abstract class AsyncInsertModule extends AsyncTask<Module, Void, List> {

    private ContentResolver contentResolver;

    public AsyncInsertModule(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    protected List doInBackground(Module... params) {
        List<Uri> uris = new ArrayList<Uri>(params.length);

        for (Module param : params) {
            Uri resultUri = contentResolver.insert(
                    MediaContract.ModuleEntry.CONTENT_URI,
                    param.toContentValues()
            );

            uris.add(resultUri);
        }

        return uris;
    }
}
