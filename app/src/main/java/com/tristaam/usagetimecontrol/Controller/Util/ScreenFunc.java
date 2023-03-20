package com.tristaam.usagetimecontrol.Controller.Util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenFunc {
    public static float GetDensityRatio(Context context){
        return context.getResources().getDisplayMetrics().density;
    }
}
