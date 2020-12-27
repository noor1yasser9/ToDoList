package com.nurbk.ps.todolist.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nurbk.ps.todolist.R;
import com.nurbk.ps.todolist.adapter.ToDoListAdapter;
import com.nurbk.ps.todolist.databinding.ActivityTodolistBinding;
import com.nurbk.ps.todolist.model.ToDoItem;
import com.nurbk.ps.todolist.ui.viewmodel.ToDoListViewModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ToDoListActivity extends AppCompatActivity {

    private static final String TAG = ToDoListActivity.class.getSimpleName();
    public static final String UPDATE_TO_DO = "updateToDo";
    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final int UPDATE_TODO_ITEM_REQUEST = 1;

    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_BACKUP = Menu.FIRST + 1;
    private static final int MENU_RESTORE = Menu.FIRST + 2;
    private static final int MENU_DUMP = Menu.FIRST + 3;

    private ToDoListAdapter mAdapter;

    private ToDoItem updateToDo;

    private SharedPreferences sharedPreferences;

    private ToDoListViewModel toDoListViewModel;
    private ActivityTodolistBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todolist);

        toDoListViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ToDoListViewModel.class);

        // Create a new TodoListAdapter for this ListActivity's ListView


        mAdapter = new ToDoListAdapter(new ArrayList<>());

        toDoListViewModel.getRepository().getAllToDo().observe(this, new Observer<List<ToDoItem>>() {
            @Override
            public void onChanged(List<ToDoItem> toDoItems) {
                Log.e("tttttttasdf",toDoItems.toString());
                mAdapter.clear();
                mAdapter.addAll(toDoItems);
            }
        });

        sharedPreferences = getSharedPreferences("Task", MODE_PRIVATE);

        mAdapter.setOnCheckListener(new ToDoListAdapter.OnCheckListener() {
            @Override
            public void checked(ToDoItem toDoItem) {
                toDoItem.setStatus(toDoItem.getStatus() == ToDoItem.Status.DONE ? ToDoItem.Status.NOT_DONE : ToDoItem.Status.DONE);
                toDoListViewModel.getRepository().update(toDoItem);

            }

            @Override
            public void updateToDo(ToDoItem toDoItem) {
                Intent intent = new Intent(getApplicationContext(),
                        AddToDoActivity.class);
                intent.putExtra(UPDATE_TO_DO, toDoItem);
                startActivityForResult(intent, UPDATE_TODO_ITEM_REQUEST);
                updateToDo = toDoItem;
            }

            @Override
            public void deleteToDo(ToDoItem toDoItem) {
                toDoListViewModel.getRepository().delete(toDoItem);
            }
        });


        mBinding.footerView.setOnClickListener(v -> {
            Log.i(TAG, "Entered footerView.OnClickListener.onClick()");
            startActivityForResult(new Intent(getApplicationContext(),
                    AddToDoActivity.class), ADD_TODO_ITEM_REQUEST);
        });

        mBinding.list.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Entered onActivityResult()");
        if (resultCode == RESULT_OK && requestCode == ADD_TODO_ITEM_REQUEST) {
            toDoListViewModel.getRepository().insert(new ToDoItem(data));
        } else if (resultCode == RESULT_OK && requestCode == UPDATE_TODO_ITEM_REQUEST) {
            toDoListViewModel.getRepository().update(new ToDoItem(data));
            mAdapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_OK && requestCode == CREATE_FILE) {
            writeFileContent(data.getData());
        } else if (resultCode == RESULT_OK && requestCode == PICK_PDF_FILE) {
            readFileContent(data.getData());
        }

    }

    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_BACKUP, Menu.NONE, "Backup");
        menu.add(Menu.NONE, MENU_RESTORE, Menu.NONE, "Restore");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                toDoListViewModel.getRepository().deleteAll();
                return true;
            case MENU_DUMP:
                return true;
            case MENU_BACKUP:
                createFile();
                return true;
            case MENU_RESTORE:
                openFile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_TITLE, "databases");
        startActivityForResult(intent, CREATE_FILE);
    }


    private static final int CREATE_FILE = 324;
    private static final int PICK_PDF_FILE = 2;

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_TITLE, "databases");
        startActivityForResult(intent, PICK_PDF_FILE);
    }


    private void readFileContent(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String id;
            String title;
            String priority;
            String status;
            mAdapter.clear();
            while ((id = reader.readLine()) != null) {
                title = reader.readLine();
                priority = reader.readLine();
                status = reader.readLine();
                ToDoItem toDoItem = new ToDoItem(title,
                        ToDoItem.Priority.valueOf(priority),
                        ToDoItem.Status.valueOf(status),
                        new Date());
                toDoItem.setmId(Integer.parseInt(id));
                toDoListViewModel.getRepository().insert(toDoItem);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeFileContent(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                fileOutputStream.write((mAdapter.getItem(i).getmId() + "\n").getBytes());
                fileOutputStream.write((mAdapter.getItem(i).getTitle() + "\n").getBytes());
                fileOutputStream.write((mAdapter.getItem(i).getPriority() + "\n").getBytes());
                fileOutputStream.write((mAdapter.getItem(i).getStatus() + "\n").getBytes());
            }
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}