package com.greysonparrelli.permisodemo;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.greysonparrelli.permiso.Permiso;

/**
 * An activity that demonstrates the features of {@link Permiso}.
 */
public class MainActivity extends AppCompatActivity {


    // =====================================================================
    // Overrides
    // =====================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register the activity in Permiso. It is crucial that you do this in every one of your activities!
        Permiso.getInstance().setActivity(this);

        // Set click listeners
        findViewById(R.id.btn_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleClick();
            }
        });
        findViewById(R.id.btn_multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMultipleClick();
            }
        });
        findViewById(R.id.btn_duplicate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDuplicateClick();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // All we do here is forward the results of the method to Permiso for processing
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }


    // =====================================================================
    // Click Listeners
    // =====================================================================

    /**
     * Request a single permission and display whether or not it was granted or denied.
     */
    private void onSingleClick() {
        // A request for a single permission
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Permission Rationale", "Needed for demo purposes.", "Ok", callback);
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * Request multiple permissions and display how many were granted.
     */
    private void onMultipleClick() {
        // A request for two permissions
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                int numGranted = 0;
                if (resultSet.isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                    numGranted++;
                }
                if (resultSet.isPermissionGranted(Manifest.permission.READ_CALENDAR)) {
                    numGranted++;
                }
                Toast.makeText(MainActivity.this, numGranted + "/2 Permissions Granted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Permission Rationale", "Needed for demo purposes.", "Ok", callback);
            }
        }, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALENDAR);
    }

    /**
     * Make two simultaneous requests for the same permission. Only one dialog will pop up, and the results from that
     * one request will be given to both callbacks.
     */
    private void onDuplicateClick() {
        // First request
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    Toast.makeText(MainActivity.this, "Permission Granted! (1)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied. (1)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Permission Rationale", "Needed for demo purposes.", "Ok", callback);
            }
        }, Manifest.permission.CAMERA);

        // Second request for the same permission
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    Toast.makeText(MainActivity.this, "Permission Granted! (2)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied. (2)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Permission Rationale", "Needed for demo purposes.", "Ok", callback);
            }
        }, Manifest.permission.CAMERA);
    }
}
