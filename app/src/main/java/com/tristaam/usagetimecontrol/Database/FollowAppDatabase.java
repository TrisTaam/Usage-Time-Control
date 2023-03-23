package com.tristaam.usagetimecontrol.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tristaam.usagetimecontrol.Model.FollowApp;

@Database(entities = {FollowApp.class}, version = 1)
public abstract class FollowAppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "follow_app.db";
    private static FollowAppDatabase instance;

    public static synchronized FollowAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FollowAppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract FollowAppDAO followAppDAO();
}
