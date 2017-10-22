package com.erasmus.barend.licensediscscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erasmus.barend.licensediscscanner.models.LicenseDisc;
import com.erasmus.barend.licensediscscanner.repositories.BaseRepository;
import com.erasmus.barend.licensediscscanner.repositories.LicenseDiscRepository;
import com.erasmus.barend.licensediscscanner.utilities.FileHelper;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends Activity {

    private Button _btnScan;
    private Button _btnExportDatabase;
    private TextView _txtRegistrationNumber;
    private TextView _txtMake;
    private TextView _txtModel;
    private TextView _txtStatisticsNumberOfScans;

    private LicenseDiscRepository _licenseDiscRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckPermissions();

        _btnScan = (Button) findViewById(R.id.btn_scan);
        _btnExportDatabase = (Button) findViewById(R.id.btn_export_database);
        _txtRegistrationNumber = (TextView) findViewById(R.id.txt_registration_number);
        _txtMake = (TextView) findViewById(R.id.txt_make);
        _txtModel = (TextView) findViewById(R.id.txt_model);
        _txtStatisticsNumberOfScans = (TextView) findViewById(R.id.txt_statistics_number_of_scans);

        _licenseDiscRepository = new LicenseDiscRepository(MainActivity.this);

        ConfigureOnClickListeners();

        UpdateNumberOfScans();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        String contents = intentResult.getContents();

        if (contents == null) {
            return;
        }

        LicenseDisc licenseDisc = new LicenseDisc(contents);

        _txtRegistrationNumber.setText(String.format("Registration Number: %s", licenseDisc._registrationNumber));
        _txtMake.setText(String.format("Make: %s", licenseDisc._make));
        _txtModel.setText(String.format("Model: %s", licenseDisc._model));

        if (_licenseDiscRepository.Exist(licenseDisc._hash)) {
            Toast.makeText(MainActivity.this, "License Disc already exists.",
                    Toast.LENGTH_LONG).show();
        } else {
            _licenseDiscRepository.Insert(licenseDisc);
            UpdateNumberOfScans();
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void UpdateNumberOfScans() {
        long count = _licenseDiscRepository.NumberOfScans();

        _txtStatisticsNumberOfScans.setText("Number of Scans: " + count);
    }

    private void OpenScanner() {
        IntentIntegrator.initiateScan(this);
    }

    private void ConfigureOnClickListeners() {

        _btnScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenScanner();
            }
        });

        _btnExportDatabase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExportDatabase();
            }
        });
    }

    private void ExportDatabase() {
        File src = getDatabasePath(BaseRepository.DATABASE_NAME);
        File dest = new File(FileHelper.GetExternalStoragePath(String.format("license-disc-scanner-%s.db", new Date().getTime())));

        FileHelper.Copy(src, dest);

        Toast.makeText(MainActivity.this, "Successfully exported database.",
                Toast.LENGTH_LONG).show();
    }

    private void CheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 0);
            }
        }
    }
}
