package com.hap.baking.db.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hap.baking.db.DatabaseInfo;

/**
 * Created by luis on 12/16/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(final Context context) {
        super(context, DatabaseInfo.DB_NAME, null, DatabaseInfo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
