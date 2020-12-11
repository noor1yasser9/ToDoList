package com.example.todolist.ui;    // DialogFragment used to pick a ToDoItem deadline time

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import java.util.Calendar;

/**
 * DialogFragment used to pick a ToDoItem deadline time
 */
public class TimePickerFragment extends DialogFragment {

    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTimeSetListener = (TimePickerDialog.OnTimeSetListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);

    }

}