package com.ex.Pocket_Data;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class MainPage extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        permission();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter((getSupportFragmentManager()));

        // Add Fragment
        adapter.AddFragment(new Document_Fragment(),"Document");
        adapter.AddFragment(new Profile_Fragment(),"Profile");
        // adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void permission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if((ActivityCompat.shouldShowRequestPermissionRationale(MainPage.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))){
                    Toast.makeText(this, "There is a problem", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(MainPage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , REQUEST_CODE);
            }
        }
        else{
            Toast.makeText(this, "Welcome!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Accepted permission", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}