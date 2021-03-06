package com.erasmus.barend.licensediscscanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.erasmus.barend.licensediscscanner.models.LicenseDisc;
import com.erasmus.barend.licensediscscanner.repositories.models.LicenseDiscEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        if (_writableDatabase == null) {
            _writableDatabase = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(LicenseDiscEntry.COLUMN_NAME_A, licenseDisc.a);
        values.put(LicenseDiscEntry.COLUMN_NAME_B, licenseDisc.b);
        values.put(LicenseDiscEntry.COLUMN_NAME_C, licenseDisc.c);
        values.put(LicenseDiscEntry.COLUMN_NAME_D, licenseDisc.d);
        values.put(LicenseDiscEntry.COLUMN_NAME_CONTROL_NUMBER, licenseDisc.controlNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_REGISTRATION_NUMBER, licenseDisc.registrationNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_REGISTER_NUMBER, licenseDisc.registerNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_TYPE, licenseDisc.type);
        values.put(LicenseDiscEntry.COLUMN_NAME_MAKE, licenseDisc.make);
        values.put(LicenseDiscEntry.COLUMN_NAME_MODEL, licenseDisc.model);
        values.put(LicenseDiscEntry.COLUMN_NAME_COLOR, licenseDisc.color);
        values.put(LicenseDiscEntry.COLUMN_NAME_VIN_NUMBER, licenseDisc.vinNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_ENGINE_NUMBER, licenseDisc.engineNumber);
        values.put(LicenseDiscEntry.COLUMN_NAME_EXPIRY_DATE, licenseDisc.expiryDate.getTime());
        values.put(LicenseDiscEntry.COLUMN_NAME_HASH, licenseDisc.hash);
        values.put(LicenseDiscEntry.COLUMN_NAME_UPLOADED, false);
        values.put(LicenseDiscEntry.COLUMN_NAME_TIMESTAMP, new Date().getTime());

        long rowId = _writableDatabase.insert(LicenseDiscEntry.TABLE_NAME, null, values);

    }

    public void MarkAsUploaded(String hash) {

        if (_writableDatabase == null) {
            _writableDatabase = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(LicenseDiscEntry.COLUMN_NAME_UPLOADED, true);

        String selection = LicenseDiscEntry.COLUMN_NAME_HASH + " = ?";
        String[] selectionArgs = { hash };

        int count = _writableDatabase.update(
                LicenseDiscEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public LicenseDisc FindLast() {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        Cursor cursor = _readableDatabase.query(LicenseDiscEntry.TABLE_NAME, null, null, null, null, null, "timestamp DESC", "1");

        int count = cursor.getCount();

        if (cursor.moveToFirst()) {
            LicenseDisc licenseDisc = new LicenseDisc(
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_A)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_B)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_C)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_D)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_CONTROL_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_REGISTRATION_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_REGISTER_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_TYPE)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_MAKE)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_MODEL)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_COLOR)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_VIN_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_ENGINE_NUMBER)),
                    new Date(cursor.getLong(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_EXPIRY_DATE))),
                    cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_HASH)),
                    new Date(cursor.getLong(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_TIMESTAMP)))
            );

            return licenseDisc;
        }else {
            return null;
        }

    }

    public List<LicenseDisc> List(String deviceId) {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        String selection = LicenseDiscEntry.COLUMN_NAME_UPLOADED + " = ?";
        String[] selectionArgs = { "0" };

        Cursor cursor = _readableDatabase.query(LicenseDiscEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        List<LicenseDisc> result = new ArrayList<LicenseDisc>();

        if (cursor.moveToFirst()) {
            do {
                LicenseDisc licenseDisc = new LicenseDisc(
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_A)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_B)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_C)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_D)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_CONTROL_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_REGISTRATION_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_REGISTER_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_TYPE)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_MAKE)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_MODEL)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_COLOR)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_VIN_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_ENGINE_NUMBER)),
                        new Date(cursor.getLong(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_EXPIRY_DATE))),
                        cursor.getString(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_HASH)),
                        new Date(cursor.getLong(cursor.getColumnIndex(LicenseDiscEntry.COLUMN_NAME_TIMESTAMP)))
                );

                licenseDisc.deviceId = deviceId;

                result.add(licenseDisc);
            } while (cursor.moveToNext());
        }

        return result;
    }

    public long NumberOfScans() {
        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        return DatabaseUtils.queryNumEntries(_readableDatabase, LicenseDiscEntry.TABLE_NAME);
    }

    public void Close() {
        if (_writableDatabase != null) {
            _writableDatabase.close();
            _writableDatabase = null;
        }

        if (_readableDatabase != null) {
            _readableDatabase.close();
            _readableDatabase = null;
        }
    }
}
