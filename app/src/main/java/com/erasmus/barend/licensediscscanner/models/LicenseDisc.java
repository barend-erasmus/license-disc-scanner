package com.erasmus.barend.licensediscscanner.models;

import com.erasmus.barend.licensediscscanner.utilities.CryptoHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class LicenseDisc {

    public String _a;
    public String _b;
    public String _c;
    public String _d;
    public String _controlNumber;
    public String _registrationNumber;
    public String _registerNumber;
    public String _type;
    public String _make;
    public String _model;
    public String _color;
    public String _vinNumber;
    public String _engineNumber;
    public Date _expiryDate;
    public String _hash;

    public LicenseDisc(String raw) {
        String[] splittedRaw = raw.split("%");

        _a = splittedRaw[1];
        _b = splittedRaw[2];
        _c = splittedRaw[3];
        _d = splittedRaw[4];
        _controlNumber = splittedRaw[5];
        _registrationNumber = splittedRaw[6];
        _registerNumber = splittedRaw[7];
        _type = splittedRaw[8];
        _make = splittedRaw[9];
        _model = splittedRaw[10];
        _color = splittedRaw[11];
        _vinNumber = splittedRaw[12];
        _engineNumber = splittedRaw[13];
        _expiryDate = ToDate(splittedRaw[14]);
        _hash = CryptoHelper.SHA1(raw);
    }

    @Override
    public String toString() {
        return _controlNumber + ";" + _registrationNumber + ";" + _registerNumber + ";" + _type + ";" + _make + ";" + _model + ";" + _color + ";" + _vinNumber + ";" + _engineNumber + ";" + _expiryDate.toString();
    }

    private Date ToDate(String str) {
        DateFormat df = new SimpleDateFormat("yyyy-mm-DD");
        Date date;

        try {
            date = df.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
