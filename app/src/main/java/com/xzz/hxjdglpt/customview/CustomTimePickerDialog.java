package com.xzz.hxjdglpt.customview;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * Created by dbz on 2017/6/5.
 */

public class CustomTimePickerDialog extends TimePickerDialog {
    public CustomTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }

    public CustomTimePickerDialog(Context context, int theme, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme, callBack, hourOfDay, minute, is24HourView);
    }

    @Override
    protected void onStop() {

    }
}
