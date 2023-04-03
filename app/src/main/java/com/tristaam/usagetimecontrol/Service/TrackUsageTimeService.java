package com.tristaam.usagetimecontrol.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.tristaam.usagetimecontrol.Database.FollowAppDatabase;
import com.tristaam.usagetimecontrol.Model.FollowApp;
import com.tristaam.usagetimecontrol.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TrackUsageTimeService extends Service {
    private List<FollowApp> appList;
    private Calendar calendar;
    private long startTime;
    private UsageStatsManager usageStatsManager;

    public TrackUsageTimeService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startTime = calendar.getTimeInMillis();
        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        trackUsageTime();
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        updateForegroundApp();
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Notification notification = new NotificationCompat.Builder(this, "Background")
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setSmallIcon(R.drawable.hourglass)
                .setPriority(NotificationCompat.PRIORITY_MIN).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    public void trackUsageTime() {
        long endTime = System.currentTimeMillis();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        appList = FollowAppDatabase.getInstance(this).followAppDAO().getFollowAppList();
        for (FollowApp x : appList) {
            for (UsageStats usageStats : usageStatsList) {
                if (usageStats.getPackageName().equals(x.getPackageName())) {
                    long totalTimeInForeground = usageStats.getTotalTimeInForeground();
                    if (x.isForeground() && x.isTurnOn()) {
                        if (totalTimeInForeground > x.getLimitTime()) {
                            sendNotification(x.getName());
                        }
                    }
                    x.setUsageTime(totalTimeInForeground);
                    FollowAppDatabase.getInstance(this).followAppDAO().update(x);
                    break;
                }
            }
        }
    }

    public void updateForegroundApp() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - (10 * 1000);
        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);
        UsageEvents.Event event = new UsageEvents.Event();
        appList = FollowAppDatabase.getInstance(this).followAppDAO().getFollowAppList();
        for (FollowApp x : appList) {
            x.setForeground(false);
            FollowAppDatabase.getInstance(this).followAppDAO().update(x);
        }
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            List<FollowApp> tmp1 = FollowAppDatabase.getInstance(this).followAppDAO().checkFollowApp(event.getPackageName());
            if (tmp1 == null || tmp1.isEmpty()) continue;
            FollowApp tmp2 = tmp1.get(0);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                tmp2.setForeground(true);
                FollowAppDatabase.getInstance(this).followAppDAO().update(tmp2);
            } else if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                tmp2.setForeground(false);
                FollowAppDatabase.getInstance(this).followAppDAO().update(tmp2);
            }
        }
    }

    public void sendNotification(String name) {
        Notification notification = new NotificationCompat.Builder(this, "Track Usage Time")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.hourglass)
                .setContentTitle("Cảnh báo")
                .setFullScreenIntent(null, true)
                .setContentText(name + " đã sử dụng quá thời gian trong ngày")
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(), notification);
        }
    }

    public int getNotificationId() {
        return (int) new Date().getTime();
    }
}