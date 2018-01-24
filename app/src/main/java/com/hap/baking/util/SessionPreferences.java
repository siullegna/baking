package com.hap.baking.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hap.baking.R;

/**
 * Created by luis on 12/18/17.
 */

public class SessionPreferences {
    private final SharedPreferences sharedPreferences;

    public SessionPreferences(final Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getString(R.string.sp_file_name), Context.MODE_PRIVATE);
    }

    public void putInt(final String key, final int value) {
        this.sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(final String key) {
        return this.sharedPreferences.getInt(key, -1);
    }

    public void deleteKey(final String key) {
        this.sharedPreferences.edit().remove(key).apply();
    }
}
