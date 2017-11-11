package com.hu.yang.prime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hu.yang.prime.bean.RecordBean;

/**
 * Created by yanghu on 2017/11/5.
 */

public class SearchDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SearchHistory.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE =
            "CREATE TABLE " + RecordBean.TABLE_NAME + " (" +
                    RecordBean._ID + " INTEGER PRIMARY KEY," +
                    RecordBean.COLUMN_NAME_NAME + TEXT_TYPE + " )";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + RecordBean.TABLE_NAME;
    private static final String TAG = SearchDBHelper.class.getSimpleName();

    public SearchDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        Log.i(TAG, "db created: " + getDatabaseName());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
}
