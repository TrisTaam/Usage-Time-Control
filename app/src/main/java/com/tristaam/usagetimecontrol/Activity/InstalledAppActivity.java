package com.tristaam.usagetimecontrol.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tristaam.usagetimecontrol.Adapter.InstalledAppAdapter;
import com.tristaam.usagetimecontrol.Controller.Util.CONSTANT;
import com.tristaam.usagetimecontrol.Model.App;
import com.tristaam.usagetimecontrol.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InstalledAppActivity extends AppCompatActivity {

    private RecyclerView installedAppView;
    private List<App> installedAppList = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_app);
        Init();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    public void Init() {
        searchView = findViewById(R.id.searchView);
        installedAppList = SplashActivity.appList;
        installedAppView = findViewById(R.id.installedAppView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        installedAppView.setLayoutManager(layoutManager);
        installedAppView.setHasFixedSize(true);
        installedAppView.setAdapter(new InstalledAppAdapter(this, installedAppList, new InstalledAppAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {
                finish(position);
            }
        }));
    }

    public void filter(String s) {
        if (s.isEmpty()) {
            installedAppList = SplashActivity.appList;
        } else {
            List<App> tmp = new ArrayList<>();
            for (App x : installedAppList) {
                if (x.getName().toLowerCase().startsWith(s.toLowerCase())) {
                    tmp.add(x);
                }
            }
            installedAppList = tmp;
        }
        installedAppView.setAdapter(new InstalledAppAdapter(this, installedAppList, new InstalledAppAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {
                finish(position);
            }
        }));
    }

    public void BtnBackClick(View view) {
        finish();
    }

    public void finish(int position) {
        Intent intent = new Intent();
        intent.putExtra(CONSTANT.INTENT_3, (Parcelable) installedAppList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}