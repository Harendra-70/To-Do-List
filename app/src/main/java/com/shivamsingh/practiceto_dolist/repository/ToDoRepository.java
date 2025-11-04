package com.shivamsingh.practiceto_dolist.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.shivamsingh.practiceto_dolist.database.ToDoDatabase;
import com.shivamsingh.practiceto_dolist.database.ToDoEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToDoRepository {

    private final ToDoDatabase myDB;
    private final ExecutorService executor;

    public ToDoRepository(Context context){
        myDB=ToDoDatabase.getInstance(context);
        executor= Executors.newSingleThreadExecutor();

    }

    public void insert(ToDoEntity task, Callback<Long> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long id = myDB.toDoDao().insertTask(task);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult(id);
                    }
                });
            }
        });
    }

    public void updateTask(int id,String newTask){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                myDB.toDoDao().updateTask(id,newTask);
            }
        });
    }

    public void updateStatus(int id, int status) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                myDB.toDoDao().updateStatus(id, status);
            }
        });
    }

    public void deleteTask(int id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                myDB.toDoDao().deleteTask(id);
            }
        });
    }

    public void getAllTasks(Callback<List<ToDoEntity>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<ToDoEntity> list = myDB.toDoDao().getAllTasks();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult(list);
                    }
                });
            }
        });
    }


    // Callback interface to send data back to UI
    public interface Callback<T> {
        void onResult(T result);
    }

    public void shutdownExecutor() {
        executor.shutdown();
    }

}
