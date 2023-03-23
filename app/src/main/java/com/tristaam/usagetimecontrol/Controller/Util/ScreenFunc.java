package com.tristaam.usagetimecontrol.Controller.Util;

import android.content.Context;

public class ScreenFunc {
    public static float getDensityRatio(Context context){
        return context.getResources().getDisplayMetrics().density;
    }
}
