package com.example.todolist2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TodoItem> todoItems;
    private TodoAdapter todoAdapter;
    private EditText editTextTodo;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        todoItems = new ArrayList<>();
        editTextTodo = findViewById(R.id.editTextTodo);
        buttonAdd = findViewById(R.id.buttonAdd);

        // RecyclerView 설정
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(todoItems, new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 항목 클릭 시 완료 상태 토글
                TodoItem item = todoItems.get(position);
                item.setCompleted(!item.isCompleted());
                todoAdapter.notifyItemChanged(position);
            }

            @Override
            public void onDeleteClick(int position) {
                // 삭제 버튼 클릭 시 항목 제거
                todoItems.remove(position);
                todoAdapter.notifyItemRemoved(position);
            }
        });
        recyclerView.setAdapter(todoAdapter);

        // 추가 버튼 클릭 리스너
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = editTextTodo.getText().toString().trim();
                if (!todoText.isEmpty()) {
                    todoItems.add(new TodoItem(todoText, false));
                    todoAdapter.notifyItemInserted(todoItems.size() - 1);
                    editTextTodo.setText("");
                }
            }
        });
    }
}