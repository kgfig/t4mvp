package com.aclass.edx.helloworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.aclass.edx.helloworld.R;

/**
 * Created by tictocproject on 20/03/2017.
 */

public class PrefUtils {

    public static void saveBoolean(Context context, int stringId, boolean value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(context.getString(stringId), value);
        editor.commit();
    }

    public static void saveString(Context context, int stringId, String value) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(context.getString(R.string.prefs_nickname), value);
        editor.commit();
    }

    public static String getNickname(Context context) {
        return getStringOrEmpty(context, R.string.prefs_nickname);
    }

    public static boolean skippedNickname(Context context) {
        return getBooleanOrFalse(context, R.string.prefs_skip_nickname);
    }


    public static String getStringOrEmpty(Context context, int stringId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        return sharedPrefs.getString(context.getString(stringId), "");
    }

    public static boolean getBooleanOrFalse(Context context, int stringId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(context.getString(stringId), false);
    }

    public static boolean skippedOrSavedNickname(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.all_prefs_file), Context.MODE_PRIVATE);
        String nickname = sharedPrefs.getString(context.getString(R.string.prefs_nickname), "");
        boolean skipped = sharedPrefs.getBoolean(context.getString(R.string.prefs_skip_nickname), false);

        return (!TextUtils.isEmpty(nickname) || skipped);
    }
}
