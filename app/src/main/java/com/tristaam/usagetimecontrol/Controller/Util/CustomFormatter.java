package com.tristaam.usagetimecontrol.Controller.Util;

import android.annotation.SuppressLint;
import android.widget.NumberPicker;

public class CustomFormatter {
    public static class NumberPickerCustomFormatter implements NumberPicker.Formatter {
        @Override
        public String format(int value) {
            return String.format("%02d", value);
        }
    }

    @SuppressLint("DefaultLocale")
    public static String MilliSecToHHMM(long milliSec) {
        long hour = milliSec / (60 * 60 * 1000);
        long minute = (milliSec % (60 * 60 * 1000)) / (60 * 1000);
        return String.format("%02dh %02dm", hour, minute);
    }
}
