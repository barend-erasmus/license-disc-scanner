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
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private TextView _txtRegistrationNumber;
    private TextView _txtMake;
    private TextView _txtModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 0);
            } else {

            }
        } else {

        }

        Button button = (Button) findViewById(R.id.btn_scan);
        _txtRegistrationNumber = (TextView) findViewById(R.id.txt_registration_number);
        _txtMake = (TextView) findViewById(R.id.txt_make);
        _txtModel = (TextView) findViewById(R.id.txt_model);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenScanner();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        String contents = intentResult.getContents();

        if (contents == null) {
            return;
        }

        File sdCardPath = Environment.getExternalStorageDirectory();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(JoinPath(sdCardPath.toString(), "license-disc-scanner.txt"), true);

            LicenseDisc licenseDisc = new LicenseDisc(contents);

            fileOutputStream.write(licenseDisc.toString().getBytes());
            fileOutputStream.write(System.getProperty("line.separator").getBytes());

            fileOutputStream.flush();
            fileOutputStream.close();

            _txtRegistrationNumber.setText(String.format("Registration Number: %s", licenseDisc.RegistrationNumber()));
            _txtMake.setText(String.format("Make: %s", licenseDisc.Make()));
            _txtModel.setText(String.format("Model: %s", licenseDisc.Model()));

        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void OpenScanner() {
        IntentIntegrator.initiateScan(this);
    }

    private String JoinPath(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
}
