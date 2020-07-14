package com.bytedance.todolist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface TodoListOperator {
    void deleteTodoList(TodoListEntity entity);
    void updateTodoList(TodoListEntity entity);

}
