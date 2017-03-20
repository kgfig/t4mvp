package com.aclass.edx.helloworld.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.aclass.edx.helloworld.GetNameActivity;
import com.aclass.edx.helloworld.R;

/**
 * Created by tictocproject on 20/03/2017.
 */

public class AppUtils {

    /**
     * Redirects to GetNameActivity if the user has not yet entered his nickname or decided to skip it.
     * @param source
     * @return
     */
    public static boolean resumeContext(Context source) {
        SharedPreferences sharedPrefs = source.getSharedPreferences(source.getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        String savedName = sharedPrefs.getString(source.getString(R.string.prefs_nickname), "");
        boolean nameSkipped = sharedPrefs.getBoolean(source.getString(R.string.prefs_skip_nickname), false);

        if (TextUtils.isEmpty(savedName) && !nameSkipped) {
            Intent intent = new Intent(source, GetNameActivity.class);
            source.startActivity(intent);
            return false;
        }

        return true;
    }

}
