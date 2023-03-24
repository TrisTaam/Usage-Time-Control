package com.tristaam.usagetimecontrol.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tristaam.usagetimecontrol.Adapter.FollowAppAdapter;
import com.tristaam.usagetimecontrol.Controller.Listener.FollowAppListener;
import com.tristaam.usagetimecontrol.Controller.Util.ImageProcessing;
import com.tristaam.usagetimecontrol.Database.FollowAppDatabase;
import com.tristaam.usagetimecontrol.Model.App;
import com.tristaam.usagetimecontrol.Model.FollowApp;
import com.tristaam.usagetimecontrol.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<FollowApp> followAppList = new ArrayList<>();
    private List<App> installedAppList = new ArrayList<>();
    private RecyclerView followAppView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        installedAppList = getUserApp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void btnAddClick(View view) {
        Intent intent = new Intent(this, InstalledAppActivity.class);
        intent.putParcelableArrayListExtra("InstalledAppList", (ArrayList<? extends Parcelable>) installedAppList);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            App tmp = data.getParcelableExtra("ChooseApp");
            FollowApp followApp = new FollowApp(tmp.getName(), tmp.getPackageName(), tmp.getByteArray(), false);
            if (isExistFollowApp(followApp)) {
                Toast.makeText(this, followApp.getName() + " đã được thêm vào trước đó", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Đã thêm " + followApp.getName(), Toast.LENGTH_SHORT).show();
            FollowAppDatabase.getInstance(this).followAppDAO().insert(followApp);
        }
    }

    public void init() {
        followAppList = new ArrayList<>();
        installedAppList = new ArrayList<>();
        followAppView = findViewById(R.id.followAppView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        followAppView.setLayoutManager(layoutManager);
        followAppView.setHasFixedSize(true);
    }

    public void loadData() {
        followAppList = FollowAppDatabase.getInstance(this).followAppDAO().getFollowAppList();
        followAppView.setAdapter(new FollowAppAdapter(this, followAppList, new FollowAppListener.OnClickListener() {
            @Override
            public void onClick(int position) {
                deleteFollowApp(followAppList.get(position));
            }
        }));
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

    public boolean isExistFollowApp(FollowApp followApp) {
        List<FollowApp> tmp = FollowAppDatabase.getInstance(this).followAppDAO().checkFollowApp(followApp.getPackageName());
        return tmp != null && !tmp.isEmpty();
    }

    public void deleteFollowApp(final FollowApp followApp) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá " + followApp.getName() + " khỏi danh sách không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FollowAppDatabase.getInstance(MainActivity.this).followAppDAO().delete(followApp);
                        Toast.makeText(MainActivity.this, "Đã xoá " + followApp.getName() + " khỏi danh sách", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}