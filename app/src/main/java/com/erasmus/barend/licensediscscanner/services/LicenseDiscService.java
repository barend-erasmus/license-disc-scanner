package com.erasmus.barend.licensediscscanner.services;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erasmus.barend.licensediscscanner.ServiceActivity;
import com.erasmus.barend.licensediscscanner.models.LicenseDisc;
import com.erasmus.barend.licensediscscanner.repositories.HashRepository;
import com.erasmus.barend.licensediscscanner.repositories.LicenseDiscRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

/**
 * Created by Barend Erasmus on 10/23/2017.
 */

public class LicenseDiscService {

    private ServiceActivity _serviceActivity;
    private Context _context;
    private Button _btnScan;
    private Button _btnUploadLicenseDiscs;
    private Button _btnDownloadHashes;
    private TextView _txtDeviceId;
    private TextView _txtRegistrationNumber;
    private TextView _txtMake;
    private TextView _txtModel;
    private TextView _txtStatisticsNumberOfScans;

    private LicenseDiscRepository _licenseDiscRepository;
    private HashRepository _hashRepository;

    private final int UPLOAD_LICENSE_DISCS_RESULT_CODE = 6650;
    private final int DOWNLOAD_HASHES_RESULT_CODE = 5675;

    public LicenseDiscService(
            ServiceActivity serviceActivity,
            Context context,
            LicenseDiscRepository licenseDiscRepository,
            HashRepository hashRepository,
            Button btnScan,
            Button btnUploadLicenseDiscs,
            Button btnDownloadHashes,
            TextView txtDeviceId,
            TextView txtRegistrationNumber,
            TextView txtMake,
            TextView txtModel,
            TextView txtStatisticsNumberOfScans
    ) {
        _serviceActivity = serviceActivity;
        _context = context;
        _licenseDiscRepository = licenseDiscRepository;
        _hashRepository = hashRepository;
        _btnScan = btnScan;
        _btnUploadLicenseDiscs = btnUploadLicenseDiscs;
        _btnDownloadHashes = btnDownloadHashes;
        _txtDeviceId = txtDeviceId;
        _txtMake = txtMake;
        _txtModel = txtModel;
        _txtRegistrationNumber = txtRegistrationNumber;
        _txtStatisticsNumberOfScans = txtStatisticsNumberOfScans;

        ConfigureOnClickListeners();

        UpdateNumberOfScans();

        LicenseDisc licenseDisc = _licenseDiscRepository.FindLast();
        if (licenseDisc != null) {
            _txtRegistrationNumber.setText(String.format("Registration Number: %s", licenseDisc.registrationNumber));
            _txtMake.setText(String.format("Make: %s", licenseDisc.make));
            _txtModel.setText(String.format("Model: %s", licenseDisc.model));
        }

        TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        _txtDeviceId.setText(String.format("Device Id: %s", deviceId));
    }

    public void ProcessScan(String contents) {
        LicenseDisc licenseDisc = new LicenseDisc(contents);

        _txtRegistrationNumber.setText(String.format("Registration Number: %s", licenseDisc.registrationNumber));
        _txtMake.setText(String.format("Make: %s", licenseDisc.make));
        _txtModel.setText(String.format("Model: %s", licenseDisc.model));

        if (_hashRepository.Exist(licenseDisc.hash)) {
            Toast.makeText(_context, "License Disc already exists.",
                    Toast.LENGTH_LONG).show();
        } else {
            _licenseDiscRepository.Insert(licenseDisc);
            _hashRepository.Insert(licenseDisc.hash);
            UpdateNumberOfScans();
        }
    }

    public void OnHTTPResponse(String content, int resultCode) {
        if (resultCode == UPLOAD_LICENSE_DISCS_RESULT_CODE) {

            List<LicenseDisc> licenseDiscs = _licenseDiscRepository.List(null);

            for (LicenseDisc licenseDisc : licenseDiscs) {
                _licenseDiscRepository.MarkAsUploaded(licenseDisc.hash);
            }

            Toast.makeText(_context, "Successfully uploaded license discs.",
                    Toast.LENGTH_LONG).show();
        }

        if (resultCode == DOWNLOAD_HASHES_RESULT_CODE) {
            Gson g = new Gson();

            String[] hashes = g.fromJson(content, String[].class);

            for (String hash : hashes) {
                if (!_hashRepository.Exist(hash)) {
                    _hashRepository.Insert(hash);
                }
            }

            Toast.makeText(_context, "Successfully downloaded hashes.",
                    Toast.LENGTH_LONG).show();

            UpdateNumberOfScans();
        }
    }

    public void CloseDatabase() {
        _licenseDiscRepository.Close();
        _hashRepository.Close();
    }

    private void UpdateNumberOfScans() {
        long count = _licenseDiscRepository.NumberOfScans();
        long totalCount = _hashRepository.NumberOfHashes();

        _txtStatisticsNumberOfScans.setText("Number of Scans: " + count + " (" + totalCount + ")");
    }

    private void UploadLicenseDiscs() {

        Gson gson = new Gson();

        TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        List<LicenseDisc> licenseDiscs = _licenseDiscRepository.List(deviceId);

        String json = gson.toJson(licenseDiscs, new TypeToken<List<LicenseDisc>>() {
        }.getType());

        _serviceActivity.Post(json, "https://license-disc-scanner.openservices.co.za/licenseDiscs/create", UPLOAD_LICENSE_DISCS_RESULT_CODE);
    }

    private void DownloadHashes() {
        _serviceActivity.Get("https://license-disc-scanner.openservices.co.za/licenseDiscs/listHashes", DOWNLOAD_HASHES_RESULT_CODE);
    }

    private void ConfigureOnClickListeners() {

        _btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator.initiateScan(_serviceActivity);
            }
        });

        _btnUploadLicenseDiscs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadLicenseDiscs();
            }
        });

        _btnDownloadHashes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DownloadHashes();
            }
        });
    }
}
