package com.tristaam.usagetimecontrol.Controller.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.tristaam.usagetimecontrol.Model.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScreenFunc {
    public static float getDensityRatio(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static List<App> getUserApp(Context context) {
        List<App> installedAppList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        for (ApplicationInfo x : packages) {
            if ((x.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !x.packageName.equals(context.getApplicationContext().getPackageName())) {
                installedAppList.add(new App((String) pm.getApplicationLabel(x), x.packageName,
                        // To reduce parcel size
                        ImageProcessing.bitmapToByteArray(ImageProcessing.drawableToBitmap(pm.getApplicationIcon(x)))));
            }
        }
        Collections.sort(installedAppList);
        return installedAppList;
    }
}
