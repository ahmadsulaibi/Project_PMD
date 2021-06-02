package com.ex.Pocket_Data;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private Button Log,Reg;
    private ImageView Logg_in;
    private TextView Texting, Test;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();

        Log = findViewById(R.id.Login);
        Log.setOnClickListener(v -> openLogin());
        Log.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fad_in_slow));

        Reg = findViewById(R.id.Register);
        Reg.setOnClickListener(v -> openReg());
        Reg.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fad_in_slow));

        Logg_in = findViewById(R.id.Logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fad_in_quickly);
        Logg_in.setAnimation(animation);
        Logg_in.animate().setStartDelay(2000).setDuration(2000).translationYBy(-300);

        Texting = findViewById(R.id.Text);
        Texting.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fad_in_quickly));
        Texting.animate().setStartDelay(2000).setDuration(2000).translationYBy(-300);

        Test = findViewById(R.id.TestBeautiful);
        Test.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fad_in_medium));

        mAuth = FirebaseAuth.getInstance();
    }

    public void openLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openReg(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()){
                            // show alert dialog navigating to Settings
                            requestStoragePermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}