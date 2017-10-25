package com.erasmus.barend.licensediscscanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.erasmus.barend.licensediscscanner.repositories.models.HashEntry;
import com.erasmus.barend.licensediscscanner.repositories.models.LicenseDiscEntry;

/**
 * Created by Barend.Erasmus on 10/25/2017.
 */

public class HashRepository extends BaseRepository {

    private SQLiteDatabase _writableDatabase;
    private SQLiteDatabase _readableDatabase;

    public HashRepository(Context context) {

        super(context);

        _writableDatabase = getWritableDatabase();
        _readableDatabase = getReadableDatabase();
    }

    public void Insert(String hash) {

        if (_writableDatabase == null) {
            _writableDatabase = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(HashEntry.COLUMN_NAME_HASH, hash);

        long rowId = _writableDatabase.insert(HashEntry.TABLE_NAME, null, values);
    }

    public boolean Exist(String hash) {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        String[] projection = new String[]{
                HashEntry.COLUMN_NAME_HASH
        };

        String filter = HashEntry.COLUMN_NAME_HASH + " = ?";

        String[] filterArgs = { hash };

        Cursor cursor = _readableDatabase.query(HashEntry.TABLE_NAME, projection, filter, filterArgs, null, null, null);

        int count = cursor.getCount();

        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    public long NumberOfHashes() {
        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        return DatabaseUtils.queryNumEntries(_readableDatabase, HashEntry.TABLE_NAME);
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
