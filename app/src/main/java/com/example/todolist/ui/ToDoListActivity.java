package com.example.todolist.ui;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.todolist.R;
import com.example.todolist.db.DBHelper;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.adapter.ToDoListAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.todolist.model.ToDoItem.Status.DONE;
import static com.example.todolist.model.ToDoItem.Status.NOT_DONE;

public class ToDoListActivity extends ListActivity {

    private static final String TAG = ToDoListActivity.class.getSimpleName();
    public static final String UPDATE_TO_DO = "updateToDo";
    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final int UPDATE_TODO_ITEM_REQUEST = 1;

    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_BACKUP = Menu.FIRST + 1;
    private static final int MENU_RESTORE = Menu.FIRST + 2;
    private static final int MENU_DUMP = Menu.FIRST + 3;

    private ToDoListAdapter mAdapter;
    private ListView list;
    private DBHelper dbHelper;
    private ToDoItem updateToDo;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        // Create a new TodoListAdapter for this ListActivity's ListView

        mAdapter = new ToDoListAdapter(getApplicationContext(), dbHelper.getAllToDo());
        sharedPreferences = getSharedPreferences("Task", MODE_PRIVATE);

        mAdapter.setOnCheckListener(new ToDoListAdapter.OnCheckListener() {
            @Override
            public void checked(ToDoItem toDoItem) {
                toDoItem.setStatus(toDoItem.getStatus() == DONE ? NOT_DONE : DONE);
                dbHelper.updateToDo(toDoItem);

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
                mAdapter.remove(toDoItem);
                mAdapter.notifyDataSetChanged();
            }
        });

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        TextView footerView = (TextView) ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view, null, false);

        list = findViewById(android.R.id.list);
        list.addFooterView(footerView);

        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered footerView.OnClickListener.onClick()");
                startActivityForResult(new Intent(getApplicationContext(),
                        AddToDoActivity.class), ADD_TODO_ITEM_REQUEST);
            }
        });

        list.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "Entered onActivityResult()");
        if (resultCode == RESULT_OK && requestCode == ADD_TODO_ITEM_REQUEST) {
            mAdapter.add(new ToDoItem(data));
        } else if (resultCode == RESULT_OK && requestCode == UPDATE_TODO_ITEM_REQUEST) {
            mAdapter.update(updateToDo, new ToDoItem(data));
        } else if (resultCode == RESULT_OK && requestCode == UPDATE_TODO_ITEM_REQUEST) {
            mAdapter.update(updateToDo, new ToDoItem(data));
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
                mAdapter.clear();
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
                mAdapter.add(toDoItem);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeFileContent(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            for (int i = 0; i < mAdapter.getCount(); i++) {
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