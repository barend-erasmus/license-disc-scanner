package com.erasmus.barend.licensediscscanner;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
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
import com.erasmus.barend.licensediscscanner.services.LicenseDiscService;
import com.erasmus.barend.licensediscscanner.utilities.FileHelper;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends ServiceActivity {

    private LicenseDiscService _licenseDiscService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckPermissions();

        Button btnScan = (Button) findViewById(R.id.btn_scan);
        Button btnExportDatabase = (Button) findViewById(R.id.btn_export_database);
        Button btnUpload = (Button) findViewById(R.id.btn_upload);
        TextView txtRegistrationNumber = (TextView) findViewById(R.id.txt_registration_number);
        TextView txtMake = (TextView) findViewById(R.id.txt_make);
        TextView txtModel = (TextView) findViewById(R.id.txt_model);
        TextView txtStatisticsNumberOfScans = (TextView) findViewById(R.id.txt_statistics_number_of_scans);

        LicenseDiscRepository licenseDiscRepository = new LicenseDiscRepository(MainActivity.this);

        _licenseDiscService = new LicenseDiscService(MainActivity.this, MainActivity.this, licenseDiscRepository, btnScan,
                btnExportDatabase, btnUpload, txtRegistrationNumber, txtMake, txtModel, txtStatisticsNumberOfScans);
    }

    @Override
    protected void onDestroy() {
        _licenseDiscService.CloseDatabase();

        super.onDestroy();
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

        _licenseDiscService.ProcessScan(contents.toString());

        super.onActivityResult(requestCode, resultCode, intent);
    }


    private void CheckPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 0);
            }
        }
    }

    @Override
    public void onSuccess(String content, int resultCode) {
    _licenseDiscService.OnHTTPResponse(content, resultCode);
    }
}
