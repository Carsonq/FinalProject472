package csc472.depaul.edu.finalproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    public static boolean ReadBoolean(Context context, String fileName, String k) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return sp.getBoolean(k, false);
    }

    public static void WriteBoolean(Context context, String fileName, String k, boolean v) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (editor != null) {
            editor.putBoolean(k, v);
            editor.commit();
        }
    }
}