package com.tristaam.usagetimecontrol.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel("Background", "Chạy ngầm", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("");
            NotificationChannel channel2 = new NotificationChannel("Track Usage Time", "Theo dõi thời gian chạy", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel1);
                notificationManager.createNotificationChannel(channel2);
            }
        }
    }
}
