package com.example.dartero.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreferencesUtils is a class to access and store variables in device storage.
 */
public class SharedPreferencesUtils {
    private static final String NAME_PREFERENCE = "name_saved";
    private static final String NAME_KEY = "name";

    public static void saveName (Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME_KEY, name);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME_KEY, null);
    }
}
