package com.nurbk.ps.todolist.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;


import com.nurbk.ps.todolist.R;
import com.nurbk.ps.todolist.model.ToDoItem;

import java.util.Calendar;
import java.util.Date;

public class AddToDoActivity extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = AddToDoActivity.class.getSimpleName();

    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;

    private static String timeString;
    private static String dateString;
    private TextView dateView;
    private TextView timeView;

    private Date mDate;
    private RadioGroup mPriorityRadioGroup;
    private RadioGroup mStatusRadioGroup;
    private EditText mTitleText;
    private RadioButton mDefaultStatusButton;
    private RadioButton mDefaultStatusDoneButton;
    private RadioButton mDefaultPriorityButton;
    private RadioButton mHighPriorityButton;
    private RadioButton mLowPriorityButton;
    private ToDoItem toDoItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        mTitleText = findViewById(R.id.title);
        mDefaultStatusButton = findViewById(R.id.statusNotDone);
        mDefaultStatusDoneButton = findViewById(R.id.statusDone);
        mDefaultPriorityButton = findViewById(R.id.medPriority);
        mHighPriorityButton = findViewById(R.id.highPriority);
        mLowPriorityButton = findViewById(R.id.lowPriority);
        mPriorityRadioGroup = findViewById(R.id.priorityGroup);
        mStatusRadioGroup = findViewById(R.id.statusGroup);
        dateView = findViewById(R.id.date);
        timeView = findViewById(R.id.time);

        // Set the default date and time
        setDefaultDateTime();
        // Set up OnClickListener for the Submit Button
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered submitButton.OnClickListener.onClick()");

                // Gather ToDoItem data

                // TODO - Get the current Priority
                ToDoItem.Priority priority = getPriority();

                ToDoItem.Status status = getStatus();

                String titleString = mTitleText.getText().toString();

                // Construct the Date string
                String fullDate = dateString + " " + timeString;

                // Package ToDoItem data into an Intent
                if (titleString.isEmpty()) {
                    mTitleText.setError(getString(R.string.error));
                    mTitleText.requestFocus();
                    return;
                }
                Intent data = new Intent();
                ToDoItem.packageIntent(data, toDoItem != null ? toDoItem.getmId() : 0
                        , titleString, priority, status, fullDate);

                setResult(RESULT_OK, data);
                finish();

            }
        });

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(ToDoListActivity.UPDATE_TO_DO)) {
                toDoItem = (ToDoItem) intent.getSerializableExtra(ToDoListActivity.UPDATE_TO_DO);
                mTitleText.setText(toDoItem.getTitle());
                getPriorityUpdate();
                getStatusUpdate();
                String[] date = toDoItem.getDate().toString().split(" ");
                dateView.setText(date[1] + " " + date[2] + " " + date[5]);
                timeView.setText(date[3]);
                submitButton.setText("Update");
            }
        }

        // OnClickListener for the Date button, calls showDatePickerDialog() to show the Date dialog
        final Button datePickerButton = findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // OnClickListener for the Time button, calls showTimePickerDialog() to
        // show the Time Dialog
        final Button timePickerButton = findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // OnClickListener for the Cancel Button,
        final Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()");
                // TODO - Indicate result and finish// //Done

                finish();


            }
        });

        // TODO - Set up OnClickListener for the Reset Button
        final Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered resetButton.OnClickListener.onClick()");
                // TODO - Reset data to default values
                setDefaultDateTime();
                mTitleText.getText().clear();
                dateView.setText(dateString);
                mDefaultPriorityButton.setChecked(true);
                mDefaultStatusButton.setChecked(true);

            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDateString(year, monthOfYear, dayOfMonth);
        dateView.setText(dateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setTimeString(hourOfDay, minute, 0);
        timeView.setText(timeString);
    }

    /**
     * Show DatePickerDialogFragment
     */
    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Show TimePickerDialogFragment
     */
    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Do not modify below this point.
     */
    private void setDefaultDateTime() {

        // Default is current time + 7 days
        mDate = new Date();
        mDate = new Date(mDate.getTime() + SEVEN_DAYS);

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.MILLISECOND));

        timeView.setText(timeString);

    }

    /**
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = year + "-" + mon + "-" + day;

    }

    /**
     * @param hourOfDay
     * @param minute
     * @param mili
     */
    private static void setTimeString(int hourOfDay, int minute, int mili) {

        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;

        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";

    }

    /**
     * @return
     */
    @SuppressLint("NonConstantResourceId")
    private ToDoItem.Priority getPriority() {

        switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
            case R.id.lowPriority: {
                return ToDoItem.Priority.LOW;
            }
            case R.id.highPriority: {
                return ToDoItem.Priority.HIGH;
            }
            default: {
                return ToDoItem.Priority.MED;
            }
        }

    }

    /**
     * @return
     */
    private ToDoItem.Status getStatus() {
        if (mStatusRadioGroup.getCheckedRadioButtonId() == R.id.statusDone) {
            return ToDoItem.Status.DONE;
        }
        return ToDoItem.Status.NOT_DONE;
    }

    /**
     * @return
     */
    private String getToDoTitle() {
        return mTitleText.getText().toString();
    }


    private void getPriorityUpdate() {
        if (toDoItem.getPriority().equals(ToDoItem.Priority.HIGH)) {
            mHighPriorityButton.setChecked(true);
        } else if (toDoItem.getPriority().equals(ToDoItem.Priority.MED)) {
            mDefaultPriorityButton.setChecked(true);
        } else {
            mLowPriorityButton.setChecked(true);
        }
    }

    private void getStatusUpdate() {
        if (toDoItem.getStatus().equals(ToDoItem.Status.NOT_DONE)) {
            mDefaultStatusButton.setChecked(true);
        } else {
            mDefaultStatusDoneButton.setChecked(true);
        }
    }
}
