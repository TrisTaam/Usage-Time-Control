package com.tristaam.usagetimecontrol.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;

import com.tristaam.usagetimecontrol.Controller.Util.CONSTANT;
import com.tristaam.usagetimecontrol.Controller.Util.ImageProcessing;
import com.tristaam.usagetimecontrol.Model.App;
import com.tristaam.usagetimecontrol.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra(CONSTANT.INTENT_1, (ArrayList<? extends Parcelable>) getUserApp());
                startActivity(intent);
                finish();
            }
        }).start();
    }

    public List<App> getUserApp() {
        List<App> installedAppList = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        for (ApplicationInfo x : packages) {
            if ((x.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !x.packageName.equals(getApplicationContext().getPackageName())) {
                installedAppList.add(new App((String) pm.getApplicationLabel(x), x.packageName,
                        // To reduce parcel size
                        ImageProcessing.bitmapToByteArray(ImageProcessing.drawableToBitmap(pm.getApplicationIcon(x)))));
            }
        }
        Collections.sort(installedAppList);
        return installedAppList;
    }
}