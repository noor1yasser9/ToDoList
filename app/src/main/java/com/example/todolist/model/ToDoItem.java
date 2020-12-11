package com.example.todolist.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.provider.BaseColumns;
import android.util.Log;

public class ToDoItem implements Serializable {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public enum Priority {
        LOW, MED, HIGH
    }

    ;

    public enum Status {
        NOT_DONE, DONE
    }

    ;

    public final static String TITLE = "title";
    public final static String PRIORITY = "priority";
    public final static String STATUS = "status";
    public final static String DATE = "date";
    public final static String _ID = "id";
    public final static String FILENAME = "filename";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);

    private String mTitle = new String();
    private int mId;
    private Priority mPriority = Priority.LOW;
    private Status mStatus = Status.NOT_DONE;
    private Date mDate = new Date();

    public ToDoItem(String title, Priority priority, Status status, Date date) {
        this.mTitle = title;
        this.mPriority = priority;
        this.mStatus = status;
        this.mDate = date;
    }

    // Create a new ToDoItem from data packaged in an Intent
    public ToDoItem(Intent intent) {
        mTitle = intent.getStringExtra(ToDoItem.TITLE);
        mId = intent.getIntExtra(ToDoItem._ID, -1);
        Log.e("tttttttt", mId + "");
        mPriority = Priority.valueOf(intent.getStringExtra(ToDoItem.PRIORITY));
        mStatus = Status.valueOf(intent.getStringExtra(ToDoItem.STATUS));
        try {
            mDate = ToDoItem.FORMAT.parse(intent.getStringExtra(ToDoItem.DATE));
        } catch (ParseException e) {
            mDate = new Date();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    // Take a set of String data values and
    // package them for transport in an Intent
    public static void packageIntent(Intent intent, int id, String title, Priority priority, Status status, String date) {
        intent.putExtra(ToDoItem.TITLE, title);
        intent.putExtra(ToDoItem._ID, id);
        intent.putExtra(ToDoItem.PRIORITY, priority.toString());
        intent.putExtra(ToDoItem.STATUS, status.toString());
        intent.putExtra(ToDoItem.DATE, date);
    }

    public String toString() {
        return mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP
                + FORMAT.format(mDate);
    }

    public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
                + ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
                + FORMAT.format(mDate);
    }


    public static class ToDoTable implements BaseColumns {
        public final static String TABLE_NAME = "toDo";
        public final static String TITLE = "title";
        public final static String PRIORITY = "priority";
        public final static String STATUS = "status";
        public final static String DATE = "date";
        public final static String FILENAME = "filename";

        public final static String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE + " TEXT , " +
                        PRIORITY + " TEXT , " +
                        STATUS + " TEXT , " +
                        DATE + " STRING , " +
                        FILENAME + " TEXT )";
        public final static String DROP_TABLE =
                "DROP TABLE IF EXIST " + TABLE_NAME;

    }

}
