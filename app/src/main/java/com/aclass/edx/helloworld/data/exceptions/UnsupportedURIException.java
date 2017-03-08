package com.aclass.edx.helloworld.data.exceptions;

import android.net.Uri;

/**
 * Created by ertd on 3/8/2017.
 */

public class UnsupportedURIException extends IllegalArgumentException {

    public UnsupportedURIException(Uri uri) {
        super("Unsupported URI : " + uri);
    }
}
