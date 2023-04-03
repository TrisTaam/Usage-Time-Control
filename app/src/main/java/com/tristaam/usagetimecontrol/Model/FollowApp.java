package com.tristaam.usagetimecontrol.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.tristaam.usagetimecontrol.Controller.Util.CONSTANT;

@Entity(tableName = "follow_app")
public class FollowApp {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String packageName;
    private byte[] byteArray;
    private long usageTime;
    private long limitTime;
    private boolean isForeground;
    private boolean isTurnOn;

    public FollowApp(String name, String packageName, byte[] byteArray) {
        this.name = name;
        this.packageName = packageName;
        this.byteArray = byteArray;
        this.usageTime = (long) 0;
        this.limitTime = ((long) (CONSTANT.MAX_HOUR - 1) * 60 * 60 + (long) (CONSTANT.MAX_MINUTE - 1) * 60) * 1000;
        this.isForeground = false;
        this.isTurnOn = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public long getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(long usageTime) {
        this.usageTime = usageTime;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(long limitTime) {
        this.limitTime = limitTime;
    }

    public boolean isForeground() {
        return isForeground;
    }

    public void setForeground(boolean foreground) {
        isForeground = foreground;
    }

    public boolean isTurnOn() {
        return isTurnOn;
    }

    public void setTurnOn(boolean turnOn) {
        isTurnOn = turnOn;
    }
}
