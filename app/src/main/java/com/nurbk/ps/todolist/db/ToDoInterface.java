package com.nurbk.ps.todolist.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nurbk.ps.todolist.model.ToDoItem;

import java.util.List;

@Dao
public interface ToDoInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ToDoItem toDoItem);


    @Query("SELECT * FROM TODOITEM")
    LiveData<List<ToDoItem>> getAllTODo();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateToDo(ToDoItem toDoItem);

    @Delete
    int delete(ToDoItem toDoItem);

    @Query("DELETE FROM TODOITEM")
    public void deleteAllToDo();

}
