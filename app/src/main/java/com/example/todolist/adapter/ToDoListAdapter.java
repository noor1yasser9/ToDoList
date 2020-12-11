package com.example.todolist.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.db.DBHelper;
import com.example.todolist.model.ToDoItem;

import static com.example.todolist.model.ToDoItem.Status.DONE;
import static com.example.todolist.model.ToDoItem.Status.NOT_DONE;

public class ToDoListAdapter extends BaseAdapter {

    private static final String TAG = ToDoListAdapter.class.getSimpleName();
    private List<ToDoItem> mItems = new ArrayList<>();
    private final Context mContext;
    private DBHelper dbHelper;

    public ToDoListAdapter(Context context, List<ToDoItem> mItems) {
        mContext = context;
        this.mItems = mItems;
        dbHelper = DBHelper.getInstance(context);
    }

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed
    public void add(ToDoItem item) {
        dbHelper.insertToDo(item);
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void update(ToDoItem oldItem, ToDoItem newItem) {
        dbHelper.updateToDo(newItem);
        mItems.add(mItems.indexOf(oldItem), newItem);
        mItems.remove(oldItem);
        notifyDataSetChanged();
    }

    public void remove(ToDoItem item) {
        dbHelper.deleteToDo(item.getmId());
        mItems.remove(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear() {
        mItems.clear();
        dbHelper.deleteAllData();
        notifyDataSetChanged();
    }

    // Returns the number of ToDoItems
    @Override
    public int getCount() {
        return mItems.size();
    }

    // Retrieve the number of ToDoItems
    @Override
    public ToDoItem getItem(int pos) {
        return mItems.get(pos);
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    // Create a View for the ToDoItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See:
    // http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    public interface OnCheckListener {
        void updateToDo(ToDoItem toDoItem);

        void checked(ToDoItem toDoItem);

        void deleteToDo(ToDoItem toDoItem);
    }

    OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO - Get the current ToDoItem //Done
        final ToDoItem toDoItem = getItem(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            // TODO - Inflate the View for this ToDoItem from todo_item.xml //Done
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);


            // TODO - Fill in specific ToDoItem data //Done
            // Remember that the data that goes in this View
            // corresponds to the user interface elements defined
            // in the layout file

            viewHolder = new ViewHolder();
            viewHolder.titleView = convertView.findViewById(R.id.titleView);
            viewHolder.statusView = convertView.findViewById(R.id.statusCheckBox);
            viewHolder.priorityView = convertView.findViewById(R.id.priorityView);
            viewHolder.dateView = convertView.findViewById(R.id.dateView);
            viewHolder.btnDelete = convertView.findViewById(R.id.btnDelete);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pois = (int) viewHolder.dateView.getTag();
                    onCheckListener.deleteToDo(mItems.get(pois));
                }
            });
            viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pois = (int) viewHolder.dateView.getTag();
                    onCheckListener.updateToDo(mItems.get(pois));
                }
            });
            viewHolder.statusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pois = (int) viewHolder.dateView.getTag();
                    onCheckListener.checked(mItems.get(pois));
                    getItem(pois).setStatus(viewHolder.statusView.isChecked() ? DONE : NOT_DONE);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // TODO - Display Title in TextView //Done
        viewHolder.titleView.setText(toDoItem.getTitle());
        // TODO - Set up Status CheckBox //Done
        viewHolder.statusView.setChecked(toDoItem.getStatus() == DONE);

        // TODO - Display Priority in a TextView //Done
        viewHolder.priorityView.setText(toDoItem.getPriority().toString());

        // TODO - Display Time and Date.//Done
        // Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and
        // time String
        Date date = toDoItem.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        viewHolder.dateView.setText(dateFormat.format(date));
        viewHolder.dateView.setTag(position);


        // Return the View you just created
        return convertView;

    }

    static class ViewHolder {
        TextView titleView;
        CheckBox statusView;
        TextView priorityView;
        TextView dateView;
        ImageButton btnDelete;
        ImageButton btnEdit;
    }


}
