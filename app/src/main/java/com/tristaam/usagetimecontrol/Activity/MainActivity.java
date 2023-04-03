package com.tristaam.usagetimecontrol.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tristaam.usagetimecontrol.Adapter.FollowAppAdapter;
import com.tristaam.usagetimecontrol.Controller.Listener.FollowAppListener;
import com.tristaam.usagetimecontrol.Controller.Util.CONSTANT;
import com.tristaam.usagetimecontrol.Database.FollowAppDatabase;
import com.tristaam.usagetimecontrol.Model.App;
import com.tristaam.usagetimecontrol.Model.FollowApp;
import com.tristaam.usagetimecontrol.R;
import com.tristaam.usagetimecontrol.Service.TrackUsageTimeService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<FollowApp> followAppList = new ArrayList<>();
    private List<App> installedAppList = new ArrayList<>();
    private RecyclerView followAppView;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, TrackUsageTimeService.class);
                startService(intent);
            }
        }).start();
        init();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefresh.setRefreshing(false);
            }
        });
        installedAppList = getIntent().getParcelableArrayListExtra(CONSTANT.INTENT_1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void btnAddClick(View view) {
        Intent intent = new Intent(this, InstalledAppActivity.class);
        intent.putParcelableArrayListExtra(CONSTANT.INTENT_2, (ArrayList<? extends Parcelable>) installedAppList);
        startActivityForResult(intent, CONSTANT.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONSTANT.REQUEST_CODE && resultCode == RESULT_OK) {
            App tmp = data.getParcelableExtra("ChooseApp");
            FollowApp followApp = new FollowApp(tmp.getName(), tmp.getPackageName(), tmp.getByteArray());
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
        swipeRefresh = findViewById(R.id.swipeRefresh);
        followAppView = findViewById(R.id.followAppView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        followAppView.setLayoutManager(layoutManager);
        followAppView.setHasFixedSize(true);
    }

    public void loadData() {
        followAppList = FollowAppDatabase.getInstance(this).followAppDAO().getFollowAppList();
        followAppView.setAdapter(new FollowAppAdapter(this, followAppList, new FollowAppListener.OnClickListener() {
            @Override
            public void onClickDelete(int position) {
                deleteFollowApp(followAppList.get(position));
            }

            @Override
            public void onClickSave(int position, long limitTime) {
                updateFollowApp(followAppList.get(position), limitTime);
            }

            @Override
            public void onChangeSwitch(int position, boolean isTurnOn) {
                updateFollowApp(followAppList.get(position), isTurnOn);
            }
        }));
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

    public void updateFollowApp(FollowApp followApp, long limitTime) {
        followApp.setLimitTime(limitTime);
        FollowAppDatabase.getInstance(this).followAppDAO().update(followApp);
        Toast.makeText(this, "Đã giới hạn thời gian của " + followApp.getName(), Toast.LENGTH_SHORT).show();
        loadData();
    }

    public void updateFollowApp(FollowApp followApp, boolean isTurnOn) {
        if (followApp.isTurnOn() == isTurnOn) {
            return;
        }
        followApp.setTurnOn(isTurnOn);
        FollowAppDatabase.getInstance(this).followAppDAO().update(followApp);
        Toast.makeText(this, "Đã " + (isTurnOn ? "bật" : "tắt") + " giới hạn thời gian của " + followApp.getName(), Toast.LENGTH_SHORT).show();
        loadData();
    }
}