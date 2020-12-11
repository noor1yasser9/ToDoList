package com.example.todolist.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.example.todolist.R;
import com.example.todolist.db.DBHelper;
import com.example.todolist.model.ToDoItem;
import com.example.todolist.adapter.ToDoListAdapter;

import static com.example.todolist.model.ToDoItem.Status.DONE;
import static com.example.todolist.model.ToDoItem.Status.NOT_DONE;

public class ToDoListActivity extends ListActivity {

    private static final String TAG = ToDoListActivity.class.getSimpleName();
    public static final String UPDATE_TO_DO = "updateToDo";
    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final int UPDATE_TODO_ITEM_REQUEST = 1;

    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    private ToDoListAdapter mAdapter;
    private ListView list;
    private DBHelper dbHelper;
    private ToDoItem updateToDo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        dbHelper = DBHelper.getInstance(getApplicationContext());
        // Create a new TodoListAdapter for this ListActivity's ListView

        mAdapter = new ToDoListAdapter(getApplicationContext(), dbHelper.getAllToDo());


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

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}