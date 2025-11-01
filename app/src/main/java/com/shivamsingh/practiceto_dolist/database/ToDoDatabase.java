package com.shivamsingh.practiceto_dolist.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ToDoEntity.class}, version = 1, exportSchema = false)
public abstract class ToDoDatabase extends RoomDatabase {
    private static final String DB_NAME = "todo_database";
    private static ToDoDatabase instance;

    public abstract ToDoDao toDoDao();

    public static synchronized ToDoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ToDoDatabase.class,
                            DB_NAME)
                    .allowMainThreadQueries() // Only for learning - not recommended in production
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}