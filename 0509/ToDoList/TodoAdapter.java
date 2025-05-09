package com.example.todolist2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private ArrayList<TodoItem> todoItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public TodoAdapter(ArrayList<TodoItem> todoItems, OnItemClickListener listener) {
        this.todoItems = todoItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem currentItem = todoItems.get(position);

        holder.textViewTodo.setText(currentItem.getText());

        // 완료 상태에 따라 텍스트 스타일 변경
        if (currentItem.isCompleted()) {
            holder.textViewTodo.setAlpha(0.5f);
            holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTodo.setAlpha(1.0f);
            holder.textViewTodo.setPaintFlags(holder.textViewTodo.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTodo;
        public Button buttonDelete;

        public TodoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTodo = itemView.findViewById(R.id.textViewTodo);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            // 항목 클릭 리스너 (완료 상태 토글)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // 삭제 버튼 클릭 리스너
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}