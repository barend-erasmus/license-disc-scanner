package com.erasmus.barend.licensediscscanner.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.erasmus.barend.licensediscscanner.repositories.models.HashEntry;
import com.erasmus.barend.licensediscscanner.repositories.models.LicenseDiscEntry;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class BaseRepository extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "license-disc-scanner.db";


    public BaseRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LicenseDiscEntry.CREATE_TABLE);
        db.execSQL(HashEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LicenseDiscEntry.DROP_TABLE);
        db.execSQL(LicenseDiscEntry.CREATE_TABLE);

        db.execSQL(HashEntry.DROP_TABLE);
        db.execSQL(HashEntry.CREATE_TABLE);
    }
}
