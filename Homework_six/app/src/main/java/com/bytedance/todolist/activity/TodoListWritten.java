package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;

import java.util.Date;

public class TodoListWritten extends AppCompatActivity {
    private EditText content;
    private Button button_con;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_item_new);
        button_con = (Button) findViewById(R.id.btn_confirm);
        button_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = (EditText) findViewById(R.id.edit_text);
                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListWritten.this).todoListDao();
                        //dao.deleteAll();
                        dao.addTodo(new TodoListEntity(content.getText().toString(), new Date(System.currentTimeMillis()),false));
                    }
                }.start();
                Intent i = new Intent(TodoListWritten.this, TodoListActivity.class);
                startActivity(i);
            }
        });

    }
}