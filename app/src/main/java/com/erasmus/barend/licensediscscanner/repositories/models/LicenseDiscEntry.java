package com.erasmus.barend.licensediscscanner.repositories.models;

import android.provider.BaseColumns;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public final class LicenseDiscEntry implements BaseColumns {

    public static final String CREATE_TABLE =
            "CREATE TABLE " + LicenseDiscEntry.TABLE_NAME + " (" +
                    LicenseDiscEntry._ID + " INTEGER PRIMARY KEY," +
                    LicenseDiscEntry.COLUMN_NAME_A + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_B + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_C + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_D + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_CONTROL_NUMBER + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_REGISTRATION_NUMBER + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_REGISTER_NUMBER + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_TYPE + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_MAKE + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_MODEL + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_COLOR + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_VIN_NUMBER + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_ENGINE_NUMBER + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_EXPIRY_DATE + " NUMERIC," +
                    LicenseDiscEntry.COLUMN_NAME_HASH + " TEXT," +
                    LicenseDiscEntry.COLUMN_NAME_TIMESTAMP + " NUMERIC)";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + LicenseDiscEntry.TABLE_NAME;

    public static final String TABLE_NAME = "licenseDisc";
    public static final String COLUMN_NAME_A = "a";
    public static final String COLUMN_NAME_B = "b";
    public static final String COLUMN_NAME_C = "c";
    public static final String COLUMN_NAME_D = "d";
    public static final String COLUMN_NAME_CONTROL_NUMBER = "controlNumber";
    public static final String COLUMN_NAME_REGISTRATION_NUMBER = "registrationNumber";
    public static final String COLUMN_NAME_REGISTER_NUMBER = "registerNumer";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_MAKE = "make";
    public static final String COLUMN_NAME_MODEL = "model";
    public static final String COLUMN_NAME_COLOR = "color";
    public static final String COLUMN_NAME_VIN_NUMBER = "vinNumber";
    public static final String COLUMN_NAME_ENGINE_NUMBER = "engineNumber";
    public static final String COLUMN_NAME_EXPIRY_DATE = "expiryDate";
    public static final String COLUMN_NAME_HASH = "hash";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
}
