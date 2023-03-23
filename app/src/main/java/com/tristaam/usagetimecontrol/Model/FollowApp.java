package com.tristaam.usagetimecontrol.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "follow_app")
public class FollowApp {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String packageName;
    private byte[] byteArray;
    private boolean isChoose;

    public FollowApp(String name, String packageName, byte[] byteArray, boolean isChoose) {
        this.name = name;
        this.packageName = packageName;
        this.byteArray = byteArray;
        this.isChoose = false;
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

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
