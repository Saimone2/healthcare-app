package com.saimone.healthcare;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class CustomTimePickerDialog extends TimePickerDialog {

    private int minHour, minMinute, maxHour, maxMinute;

    public CustomTimePickerDialog(Context context, int style, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, style, listener, hourOfDay, minute, is24HourView);
    }

    public void setMinTime(int minHour, int minMinute) {
        this.minHour = minHour;
        this.minMinute = minMinute;
    }

    public void setMaxTime(int maxHour, int maxMinute) {
        this.maxHour = maxHour;
        this.maxMinute = maxMinute;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
        if (view.isShown()) {
            if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
                view.setCurrentHour(minHour);
                view.setCurrentMinute(minMinute);
            } else if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
                view.setCurrentHour(maxHour);
                view.setCurrentMinute(maxMinute);
            }
        }
    }
}
