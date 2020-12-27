package com.nurbk.ps.todolist.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.nurbk.ps.todolist.model.ToDoItem;

@Database(entities = {ToDoItem.class}, version = 1)
@TypeConverters(value = {ConverterToDoItem.class})
public abstract class ToDoDatabase extends RoomDatabase {

    public abstract ToDoInterface getDao();

    private static final String NAME_DATABASE = "ToDoItem.db";

    private static ToDoDatabase instance;

    public static ToDoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ToDoDatabase.class, NAME_DATABASE)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
}
