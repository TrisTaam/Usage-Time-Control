package com.tristaam.usagetimecontrol.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class App extends ArrayList<Parcelable> implements Comparable<App>, Parcelable {
    private String name;
    private String packageName;
    // byteArray is a compression of Bitmap
    private byte[] byteArray;

    public App(String name, String packageName, byte[] byteArray) {
        this.name = name;
        this.packageName = packageName;
        this.byteArray = byteArray;
    }

    protected App(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        byteArray = in.createByteArray();
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel in) {
            return new App(in);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

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

    @Override
    public int compareTo(App o) {
        if (!name.equals(o.getName())) {
            return name.compareTo(o.getName());
        }
        return packageName.compareTo(o.getPackageName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeByteArray(byteArray);
    }
}
