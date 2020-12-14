package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todolist.model.ToDoItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "ToDo.dp";
    private final static int VERSION = 1;
    private SQLiteDatabase dbWrite = null;

    private DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        dbWrite = this.getWritableDatabase();
    }

    private static DBHelper instance = null;

    public static DBHelper getInstance(Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ToDoItem.ToDoTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ToDoItem.ToDoTable.DROP_TABLE);
        onCreate(db);
    }

    public Boolean insertToDo(ToDoItem toDoItem) {
        ContentValues cv = new ContentValues();
        cv.put(ToDoItem.ToDoTable.TITLE, toDoItem.getTitle());
        cv.put(ToDoItem.ToDoTable.PRIORITY, toDoItem.getPriority().toString());
        cv.put(ToDoItem.ToDoTable.STATUS, toDoItem.getStatus().toString());
        cv.put(ToDoItem.ToDoTable.DATE, toDoItem.getDate().toString());
        return dbWrite.insert(ToDoItem.ToDoTable.TABLE_NAME, null, cv) > 0;
    }


    public List<ToDoItem> getAllToDo() {
        List<ToDoItem> toDoItemList = new ArrayList<>();
        Cursor cursor = dbWrite.query(ToDoItem.ToDoTable.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ToDoItem toDoItem = new ToDoItem(
                    cursor.getString(cursor.getColumnIndex(ToDoItem.ToDoTable.TITLE)),
                    ToDoItem.Priority.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItem.ToDoTable.PRIORITY))),
                    ToDoItem.Status.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItem.ToDoTable.STATUS))),
                    new Date(cursor.getString(cursor.getColumnIndex(ToDoItem.ToDoTable.DATE))));
            toDoItem.setmId(cursor.getInt(cursor.getColumnIndex(ToDoItem.ToDoTable._ID)));
            toDoItemList.add(toDoItem);
        }
        cursor.close();
        return toDoItemList;
    }

    public Boolean deleteToDo(int id) {
        return dbWrite.delete(ToDoItem.ToDoTable.TABLE_NAME,
                ToDoItem.ToDoTable._ID + "=?", new String[]{id + ""}) > 0;
    }

    public Boolean updateToDo(ToDoItem toDoItem) {
        ContentValues cv = new ContentValues();
        cv.put(ToDoItem.ToDoTable.TITLE, toDoItem.getTitle());
        cv.put(ToDoItem.ToDoTable.PRIORITY, toDoItem.getPriority().toString());
        cv.put(ToDoItem.ToDoTable.STATUS, toDoItem.getStatus().toString());
        cv.put(ToDoItem.ToDoTable.DATE, toDoItem.getDate().toString());
        return dbWrite.update(ToDoItem.ToDoTable.TABLE_NAME,
                cv, ToDoItem.ToDoTable._ID + "=?", new String[]{toDoItem.getmId() + ""}) > 0;
    }

    public void deleteAllData(){
        dbWrite.execSQL("delete  from "+ ToDoItem.ToDoTable.TABLE_NAME);
    }
}
