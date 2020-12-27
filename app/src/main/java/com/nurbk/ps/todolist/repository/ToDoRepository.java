package com.nurbk.ps.todolist.repository;

import android.content.Context;


import androidx.lifecycle.LiveData;

import com.nurbk.ps.todolist.db.ToDoDatabase;
import com.nurbk.ps.todolist.model.ToDoItem;

import java.util.List;


public class ToDoRepository {

    private ToDoDatabase toDoDatabase;
    private static  ToDoRepository instance;


    public static  ToDoRepository getInstance(Context context) {
        if (instance == null)
            instance = new  ToDoRepository(context);
        return instance;
    }

    private ToDoRepository(Context context) {
        toDoDatabase = ToDoDatabase.getInstance(context);
    }


    public void insert(ToDoItem toDoItem) {
        toDoDatabase.getDao().insert(toDoItem);
    }


    public void delete(ToDoItem toDoItem) {
    toDoDatabase.getDao().delete(toDoItem);
    }

    public void deleteAll() {
        toDoDatabase.getDao().deleteAllToDo();
    }

    public void update(ToDoItem toDoItem) {
        toDoDatabase.getDao().updateToDo(toDoItem);

    }

    public LiveData<List<ToDoItem>> getAllToDo() {
        return  toDoDatabase.getDao().getAllTODo();
    }

}
