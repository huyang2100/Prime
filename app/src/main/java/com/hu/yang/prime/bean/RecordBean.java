package com.hu.yang.prime.bean;

import android.provider.BaseColumns;

/**
 * Created by yanghu on 2017/11/5.
 */

public class RecordBean implements BaseColumns {

    private RecordBean() {
    }

    public static final String TABLE_NAME = "record";
    public static final String COLUMN_NAME_NAME = "name";
}
