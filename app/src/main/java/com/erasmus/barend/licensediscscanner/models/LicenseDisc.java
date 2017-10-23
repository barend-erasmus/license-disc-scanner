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

    public String a;
    public String b;
    public String c;
    public String d;
    public String controlNumber;
    public String registrationNumber;
    public String registerNumber;
    public String type;
    public String make;
    public String model;
    public String color;
    public String vinNumber;
    public String engineNumber;
    public Date expiryDate;
    public String hash;

    public Date timestamp;
    public String deviceId;

    public LicenseDisc(
            String a,
            String b,
            String c,
            String d,
            String controlNumber,
            String registrationNumber,
            String registerNumber,
            String type,
            String make,
            String model,
            String color,
            String vinNumber,
            String engineNumber,
            Date expiryDate,
            String hash,
            Date timestamp
    ) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.controlNumber = controlNumber;
        this.registrationNumber = registrationNumber;
        this.registerNumber = registerNumber;
        this.type = type;
        this.make = make;
        this.model = model;
        this.color = color;
        this.vinNumber = vinNumber;
        this.engineNumber = engineNumber;
        this.expiryDate = expiryDate;
        this.hash = hash;
        this.timestamp = timestamp;
    }

    public LicenseDisc(String raw) {
        String[] splittedRaw = raw.split("%");

        a = splittedRaw[1];
        b = splittedRaw[2];
        c = splittedRaw[3];
        d = splittedRaw[4];
        controlNumber = splittedRaw[5];
        registrationNumber = splittedRaw[6];
        registerNumber = splittedRaw[7];
        type = splittedRaw[8];
        make = splittedRaw[9];
        model = splittedRaw[10];
        color = splittedRaw[11];
        vinNumber = splittedRaw[12];
        engineNumber = splittedRaw[13];
        expiryDate = ToDate(splittedRaw[14]);
        hash = CryptoHelper.SHA1(raw);
    }

    @Override
    public String toString() {
        return controlNumber + ";" + registrationNumber + ";" + registerNumber + ";" + type + ";" + make + ";" + model + ";" + color + ";" + vinNumber + ";" + engineNumber + ";" + expiryDate.toString();
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
