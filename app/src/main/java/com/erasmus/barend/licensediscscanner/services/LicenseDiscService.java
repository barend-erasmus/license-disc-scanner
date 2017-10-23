package com.erasmus.barend.licensediscscanner.services;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erasmus.barend.licensediscscanner.MainActivity;
import com.erasmus.barend.licensediscscanner.ServiceActivity;
import com.erasmus.barend.licensediscscanner.models.LicenseDisc;
import com.erasmus.barend.licensediscscanner.repositories.BaseRepository;
import com.erasmus.barend.licensediscscanner.repositories.LicenseDiscRepository;
import com.erasmus.barend.licensediscscanner.utilities.FileHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Barend Erasmus on 10/23/2017.
 */

public class LicenseDiscService {

    private ServiceActivity _serviceActivity;
    private Context _context;
    private Button _btnScan;
    private Button _btnExportDatabase;
    private Button _btnUpload;
    private TextView _txtRegistrationNumber;
    private TextView _txtMake;
    private TextView _txtModel;
    private TextView _txtStatisticsNumberOfScans;

    private LicenseDiscRepository _licenseDiscRepository;

    public LicenseDiscService(
            ServiceActivity serviceActivity,
            Context context,
            LicenseDiscRepository licenseDiscRepository,
            Button btnScan,
            Button btnExportDatabase,
            Button btnUpload,
            TextView txtRegistrationNumber,
            TextView txtMake,
            TextView txtModel,
            TextView txtStatisticsNumberOfScans
    ) {
        _serviceActivity = serviceActivity;
        _context = context;
        _licenseDiscRepository = licenseDiscRepository;
        _btnScan = btnScan;
        _btnExportDatabase = btnExportDatabase;
        _btnUpload = btnUpload;
        _txtMake = txtMake;
        _txtModel = txtModel;
        _txtRegistrationNumber = txtRegistrationNumber;
        _txtStatisticsNumberOfScans = txtStatisticsNumberOfScans;

        ConfigureOnClickListeners();

        UpdateNumberOfScans();
    }

    public void ProcessScan(String contents) {
        LicenseDisc licenseDisc = new LicenseDisc(contents);

        _txtRegistrationNumber.setText(String.format("Registration Number: %s", licenseDisc.registrationNumber));
        _txtMake.setText(String.format("Make: %s", licenseDisc.make));
        _txtModel.setText(String.format("Model: %s", licenseDisc.model));

        if (_licenseDiscRepository.Exist(licenseDisc.hash)) {
            Toast.makeText(_context, "License Disc already exists.",
                    Toast.LENGTH_LONG).show();
        } else {
            _licenseDiscRepository.Insert(licenseDisc);
            UpdateNumberOfScans();
        }
    }

    public void OnHTTPResponse(String content, int resultCode) {

    }

    public void CloseDatabase() {
        _licenseDiscRepository.Close();
    }

    private void UpdateNumberOfScans() {
        long count = _licenseDiscRepository.NumberOfScans();

        _txtStatisticsNumberOfScans.setText("Number of Scans: " + count);
    }

    private void ExportDatabase() {
        File src = _context.getDatabasePath(BaseRepository.DATABASE_NAME);
        File dest = new File(FileHelper.GetExternalStoragePath(String.format("license-disc-scanner-%s.db", new Date().getTime())));

        FileHelper.Copy(src, dest);

        Toast.makeText(_context, "Successfully exported database.",
                Toast.LENGTH_LONG).show();
    }

    private void UploadDatabase() {

        TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        Gson gson = new Gson();

        List<LicenseDisc> licenseDiscs = _licenseDiscRepository.List(deviceId);


        String json = gson.toJson(licenseDiscs, new TypeToken<List<LicenseDisc>>() {
        }.getType());
        _serviceActivity.Post(json, "http://192.168.1.74:3000/licenseDiscs/create", 100);
    }

    private void ConfigureOnClickListeners() {

        _btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator.initiateScan(_serviceActivity);
            }
        });

        _btnExportDatabase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExportDatabase();
            }
        });

        _btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadDatabase();
            }
        });
    }
}
