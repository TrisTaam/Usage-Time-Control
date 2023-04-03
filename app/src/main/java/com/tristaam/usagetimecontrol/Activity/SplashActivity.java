package com.tristaam.usagetimecontrol.Activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.tristaam.usagetimecontrol.Controller.Util.ImageProcessing;
import com.tristaam.usagetimecontrol.Controller.Util.ScreenFunc;
import com.tristaam.usagetimecontrol.Model.App;
import com.tristaam.usagetimecontrol.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    public static List<App> appList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (isUsageAccessGranted()) {
            navigate();
        } else {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, 900);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 900 && isUsageAccessGranted()) {
            navigate();
        } else {
            finish();
        }
    }

    public void navigate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isUsageAccessGranted()) {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                appList = ScreenFunc.getUserApp(SplashActivity.this);
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

    public boolean isUsageAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}