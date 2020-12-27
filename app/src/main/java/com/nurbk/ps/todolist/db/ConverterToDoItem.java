package com.nurbk.ps.todolist.db;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.nurbk.ps.todolist.model.ToDoItem;

import java.util.Date;

public class ConverterToDoItem {

    @TypeConverter
    public String fromPriority(ToDoItem.Priority priority) {
        return new Gson().toJson(priority);
    }

    @TypeConverter
    public ToDoItem.Priority formString(String value) {
        return new Gson().fromJson(value, ToDoItem.Priority.class);
    }

    @TypeConverter
    public String fromStatus(ToDoItem.Status status) {
        return new Gson().toJson(status);
    }

    @TypeConverter
    public ToDoItem.Status fromString(String value) {
        return new Gson().fromJson(value, ToDoItem.Status.class);
    }

    @TypeConverter
    public String fromDate(Date date) {
        return new Gson().toJson(date);
    }

    @TypeConverter
    public Date fromStringDate(String value) {
       return new Gson().fromJson(value, Date.class);
    }
}
