package com.erasmus.barend.licensediscscanner.repositories.models;

import android.provider.BaseColumns;

/**
 * Created by Barend.Erasmus on 10/25/2017.
 */

public class HashEntry implements BaseColumns {

    public static final String CREATE_TABLE =
            "CREATE TABLE " + HashEntry.TABLE_NAME + " (" +
                    HashEntry._ID + " INTEGER PRIMARY KEY," +
                    HashEntry.COLUMN_NAME_HASH + " TEXT)";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + HashEntry.TABLE_NAME;

    public static final String TABLE_NAME = "hashEntry";
    public static final String COLUMN_NAME_HASH = "hash";
}
