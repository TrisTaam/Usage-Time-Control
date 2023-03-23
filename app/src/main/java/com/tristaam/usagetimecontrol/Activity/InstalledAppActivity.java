package com.tristaam.usagetimecontrol.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tristaam.usagetimecontrol.R;
import com.tristaam.usagetimecontrol.Adapter.InstalledAppAdapter;
import com.tristaam.usagetimecontrol.Model.App;

import java.util.ArrayList;
import java.util.List;

public class InstalledAppActivity extends AppCompatActivity {

    private RecyclerView installedAppView;
    private List<App> installedAppList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_app);
        Init();
    }

    public void Init() {
        installedAppList = getIntent().getParcelableArrayListExtra("InstalledAppList");
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

    public void BtnBackClick(View view) {
        finish();
    }

    public void finish(int position) {
        Intent intent = new Intent();
        intent.putExtra("ChooseApp", (Parcelable) installedAppList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}