package com.hu.yang.prime.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hu.yang.prime.bean.RecordBean;

import java.util.ArrayList;

/**
 * Created by yanghu on 2017/11/5.
 */

public class SearchDB {

    private static final String TAG = SearchDB.class.getSimpleName();
    private static final int MAX_NUM = 10;
    private final SearchDBHelper searchDBHelper;

    public SearchDB(Context context) {
        searchDBHelper = new SearchDBHelper(context);
    }

    public void add(String name) {
        int count = getCount();
        int del = del(name);
        if (count == MAX_NUM && del == 0) {
            //删除最后一条
            SQLiteDatabase writableDatabase = searchDBHelper.getWritableDatabase();
            writableDatabase.execSQL("delete from " + RecordBean.TABLE_NAME + " where " + RecordBean._ID + " in (select " + RecordBean._ID + " from " + RecordBean.TABLE_NAME + " order by " + RecordBean._ID + " limit 0,1);");
        }
        SQLiteDatabase writableDatabase = searchDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordBean.COLUMN_NAME_NAME, name);
        writableDatabase.insert(RecordBean.TABLE_NAME, null, values);
    }

    public ArrayList<String> get() {
        ArrayList<String> records = new ArrayList<>();
        SQLiteDatabase readableDatabase = searchDBHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(RecordBean.TABLE_NAME, null, null, null, null, null, RecordBean._ID + " desc");
        while (cursor.moveToNext()) {
            String columnName = cursor.getString(cursor.getColumnIndex(RecordBean.COLUMN_NAME_NAME));
            records.add(columnName);
        }
        return records;
    }

    public int del(String name) {
        SQLiteDatabase writableDatabase = searchDBHelper.getWritableDatabase();
        return writableDatabase.delete(RecordBean.TABLE_NAME, RecordBean.COLUMN_NAME_NAME + "=?", new String[]{name});
    }

    public int getCount() {
        SQLiteDatabase readableDatabase = searchDBHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(RecordBean.TABLE_NAME, null, null, null, null, null, null);
        return cursor.getCount();
    }
}
