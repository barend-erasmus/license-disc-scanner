package com.erasmus.barend.licensediscscanner.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class LicenseDisc {

    private String _a;
    private String _b;
    private String _c;
    private String _d;
    private String _controlNumber;
    private String _registrationNumber;
    private String _registerNumber;
    private String _type;
    private String _make;
    private String _model;
    private String _color;
    private String _vinNumber;
    private String _engineNumber;
    private Date _expiryDate;

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
    }

    public String RegistrationNumber() {
        return _registrationNumber;
    }

    public String Make() {
        return _make;
    }

    public String Model() {
        return _model;
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
