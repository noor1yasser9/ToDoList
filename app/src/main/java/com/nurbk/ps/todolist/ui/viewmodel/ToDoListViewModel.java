package com.nurbk.ps.todolist.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.nurbk.ps.todolist.repository.ToDoRepository;

public class ToDoListViewModel extends AndroidViewModel {

    private ToDoRepository repository;

    public ToDoListViewModel(@NonNull Application application) {
        super(application);
        repository = ToDoRepository.getInstance(application.getApplicationContext());
    }

    public ToDoRepository getRepository() {
        return repository;
    }



}
