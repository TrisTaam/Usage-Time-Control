package com.tristaam.usagetimecontrol.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tristaam.usagetimecontrol.Model.FollowApp;

import java.util.List;

@Dao
public interface FollowAppDAO {
    @Insert
    void insert(FollowApp followApp);

    @Query("SELECT * FROM follow_app")
    List<FollowApp> getFollowAppList();

    @Query("SELECT * FROM follow_app WHERE packageName=:packageName")
    List<FollowApp> checkFollowApp(String packageName);
}
