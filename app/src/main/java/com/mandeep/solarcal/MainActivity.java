package com.mandeep.solarcal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final int MY_PERMISSION_REQUEST_LOCATION = 2;
    private static final int MY_PERMISSION_REQUEST_STORAGE = 3;
    private static final int REQUEST_PERMISSION_SETTING = 4;
    boolean permanentDenied;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

    }

    private void checkPermissions() {
        final String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "All Permissions Granted", Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // No permission granted
            // show rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perms[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, perms[1])) {
                permanentDenied = false;

                new AlertDialog.Builder(this)
                        .setTitle("INFO")
                        .setMessage("Location and Storage Permissions are necessary.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        perms, MY_PERMISSIONS_REQUEST);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        perms, MY_PERMISSIONS_REQUEST);
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // only location permission is not granted

            // show rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perms[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, perms[1])) {
                permanentDenied = false;

                new AlertDialog.Builder(this)
                        .setTitle("INFO")
                        .setMessage("Location Permission is necessary.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }

        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // only location permission is not granted

            // show rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perms[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, perms[1])) {
                permanentDenied = false;

                new AlertDialog.Builder(this)
                        .setTitle("INFO")
                        .setMessage("Storage Permission is necessary.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0) {
                    boolean locationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storagePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (locationPermission && storagePermission) {
                        // hey permission granted successfully
                        Toast.makeText(this, "Enjoy Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        // no permission is granted
                        if (!permanentDenied) {
                            permanentDenied = true;
                            checkPermissions();
                        } else {
                            // open setting page for permissions
                            openSettingPage();
                        }
                    }
                }
                break;
            }
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // location permission granted
                } else {
                    // location permission is not granted
                    if (!permanentDenied) {
                        permanentDenied = true;
                        checkPermissions();
                    } else {
                        // open setting page for permissions
                        openSettingPage();
                    }
                }
                break;
            }
            case MY_PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // storage permission granted
                } else {
                    // storage permission is not granted
                    if (!permanentDenied) {
                        permanentDenied = true;
                        checkPermissions();
                    } else {
                        // open setting page for permissions
                        openSettingPage();
                    }
                }
                break;
            }
        }

    }

    private void openSettingPage() {
        new AlertDialog.Builder(this)
                .setTitle("INFO")
                .setCancelable(false)
                .setMessage("Give the permission from settings page otherwise close the app.")
                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // open settings page
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                })
                .setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            checkPermissions();
        }
    }
}
