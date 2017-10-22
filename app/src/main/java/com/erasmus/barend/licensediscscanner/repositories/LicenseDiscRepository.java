package com.erasmus.barend.licensediscscanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.erasmus.barend.licensediscscanner.models.LicenseDisc;
import com.erasmus.barend.licensediscscanner.repositories.models.LicenseDiscEntry;

import java.util.Date;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class LicenseDiscRepository extends BaseRepository {

    private SQLiteDatabase _writableDatabase;
    private SQLiteDatabase _readableDatabase;

    public LicenseDiscRepository(Context context) {

        super(context);

        _writableDatabase = getWritableDatabase();
        _readableDatabase = getReadableDatabase();
    }

    public void Insert(LicenseDisc licenseDisc) {
        ContentValues values = new ContentValues();
        values.put(LicenseDiscEntry.COLUMN_NAME_A, licenseDisc._a);
        values.put(LicenseDiscEntry.COLUMN_NAME_B, licenseDisc._b);
        values.put(LicenseDiscEntry.COLUMN_NAME_C, licenseDisc._c);
        values.put(LicenseDiscEntry.COLUMN_NAME_D, licenseDisc._d);
        values.put(LicenseDiscEntry.COLUMN_NAME_CONTROL_NUMBER, licenseDisc._controlNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_REGISTRATION_NUMBER, licenseDisc._registrationNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_REGISTER_NUMBER, licenseDisc._registerNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_TYPE, licenseDisc._type);
        values.put(LicenseDiscEntry.COLUMN_NAME_MAKE, licenseDisc._make);
        values.put(LicenseDiscEntry.COLUMN_NAME_MODEL, licenseDisc._model);
        values.put(LicenseDiscEntry.COLUMN_NAME_COLOR, licenseDisc._color);
        values.put(LicenseDiscEntry.COLUMN_NAME_VIN_NUMBER, licenseDisc._vinNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_ENGINE_NUMBER, licenseDisc._engineNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_EXPIRY_DATE, licenseDisc._expiryDate.getTime());
        values.put(LicenseDiscEntry.COLUMN_NAME_HASH, licenseDisc._hash);
        values.put(LicenseDiscEntry.COLUMN_NAME_TIMESTAMP, new Date().getTime());

        long rowId = _writableDatabase.insert(LicenseDiscEntry.TABLE_NAME, null, values);
    }

    public boolean Exist(String hash) {
        String[] projection = new String[] {
                LicenseDiscEntry.COLUMN_NAME_HASH
        };

        String filter = LicenseDiscEntry.COLUMN_NAME_HASH + " = ?";

        String[] filterArgs = new String[] {
                hash
        };

        Cursor cursor = _readableDatabase.query(LicenseDiscEntry.TABLE_NAME, projection, filter, filterArgs, null, null, null);

        int count = cursor.getCount();

        if (count == 0) {
            return false;
        }else {
            return  true;
        }
    }

    public long NumberOfScans() {
        return DatabaseUtils.queryNumEntries(_readableDatabase, LicenseDiscEntry.TABLE_NAME);
    }
}
