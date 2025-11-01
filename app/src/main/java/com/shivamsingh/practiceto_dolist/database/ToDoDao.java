package com.shivamsingh.practiceto_dolist.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ToDoDao {

    @Insert
    long insertTask(ToDoEntity toDoEntity);

    @Query("UPDATE todo_table SET task = :task WHERE id = :id")
    void updateTask(int id, String task);

    @Query("UPDATE todo_table SET status = :status WHERE id = :id")
    void updateStatus(int id, int status);

    @Query("DELETE FROM todo_table WHERE id = :id")
    void deleteTask(int id);

    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    List<ToDoEntity> getAllTasks();
}