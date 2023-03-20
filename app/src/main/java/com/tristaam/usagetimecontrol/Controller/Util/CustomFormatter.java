package com.tristaam.usagetimecontrol.Controller.Util;

import android.widget.NumberPicker;

public class CustomFormatter implements NumberPicker.Formatter {

    @Override
    public String format(int value) {
        return String.format("%02d", value);
    }
}
