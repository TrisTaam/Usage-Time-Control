package com.tristaam.usagetimecontrol.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usagetimecontrol.R;
import com.tristaam.usagetimecontrol.Adapter.FollowAppAdapter;
import com.tristaam.usagetimecontrol.Controller.ImageProcessing;
import com.tristaam.usagetimecontrol.Model.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<App> followAppList = new ArrayList<>();
    private List<App> installedAppList = new ArrayList<>();
    private RecyclerView followAppView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        installedAppList = getUserApp();
    }

    @Override
    protected void onResume() {
        Init();
        super.onResume();
    }

    public void BtnAddClick(View view) {
        Intent intent = new Intent(this, InstalledAppActivity.class);
        intent.putParcelableArrayListExtra("InstalledAppList", (ArrayList<? extends Parcelable>) installedAppList);
        Bundle bundle = intent.getExtras();
        Parcel parcel = Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            followAppList.add(data.getParcelableExtra("ChooseApp"));
            Collections.sort(followAppList);
        }
    }

    public void Init() {
        followAppView = findViewById(R.id.followAppView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        followAppView.setLayoutManager(layoutManager);
        followAppView.setHasFixedSize(true);
        followAppView.setAdapter(new FollowAppAdapter(this, followAppList));
    }

    public List<App> getUserApp() {
        List<App> installedAppList = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        for (ApplicationInfo x : packages) {
            if ((x.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !x.packageName.equals(getApplicationContext().getPackageName())) {
                installedAppList.add(new App((String) pm.getApplicationLabel(x), x.packageName,
                        // To reduce parcel size
                        ImageProcessing.BitmapToByteArray(ImageProcessing.DrawableToBitmap(pm.getApplicationIcon(x)))));
            }
        }
        Collections.sort(installedAppList);
        return installedAppList;
    }
}