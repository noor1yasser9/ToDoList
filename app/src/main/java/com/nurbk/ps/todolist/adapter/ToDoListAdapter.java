package com.nurbk.ps.todolist.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nurbk.ps.todolist.databinding.TodoItemBinding;
import com.nurbk.ps.todolist.model.ToDoItem;
import java.util.ArrayList;
import java.util.List;


public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoViewHolder> {


    private ArrayList<ToDoItem> toDoItemsList ;

    public ToDoListAdapter(ArrayList<ToDoItem> toDoItemsList) {
        this.toDoItemsList = toDoItemsList;
    }

    public void clear() {
        toDoItemsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ToDoItem> toDoItems) {
        toDoItemsList.addAll(toDoItems);
        notifyDataSetChanged();
    }

    public ToDoItem getItem(int i) {
        return toDoItemsList.get(i);
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        TodoItemBinding mBinding;
        public ToDoViewHolder(TodoItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding=mBinding;
        }

        public void bind(ToDoItem item) {
            mBinding.setTodoItem(item);
            mBinding.executePendingBindings();

            mBinding.btnDelete.setOnClickListener(v -> {
                onCheckListener.deleteToDo(item);
            });
            mBinding.btnEdit.setOnClickListener(v -> {
                onCheckListener.updateToDo(item);
            });
            mBinding.statusCheckBox.setOnClickListener(view -> {
                onCheckListener.checked(item);
                item.setStatus(mBinding.statusCheckBox.isChecked() ? ToDoItem.Status.DONE : ToDoItem.Status.NOT_DONE);
            });

        }
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TodoItemBinding itemBinding = TodoItemBinding.inflate(layoutInflater, parent, false);
        return new ToDoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.bind(toDoItemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return toDoItemsList.size();
    }

    public interface OnCheckListener {
        void updateToDo(ToDoItem toDoItem);

        void checked(ToDoItem toDoItem);

        void deleteToDo(ToDoItem toDoItem);
    }

    OnCheckListener onCheckListener;


    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }


}
