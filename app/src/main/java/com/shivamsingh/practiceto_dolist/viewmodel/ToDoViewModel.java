package com.shivamsingh.practiceto_dolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shivamsingh.practiceto_dolist.database.ToDoEntity;
import com.shivamsingh.practiceto_dolist.repository.ToDoRepository;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private final ToDoRepository repository;
    private final MutableLiveData<List<ToDoEntity>> tasksLiveData = new MutableLiveData<>();

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        repository = new ToDoRepository(application);
        loadAllTasks(); // load tasks initially
    }

    public LiveData<List<ToDoEntity>> getTasks() {
        return tasksLiveData;
    }

    private void loadAllTasks() {
        repository.getAllTasks(new ToDoRepository.Callback<List<ToDoEntity>>() {
            @Override
            public void onResult(List<ToDoEntity> result) {
                tasksLiveData.postValue(result);
            }
        });
    }

    public void insertTask(ToDoEntity task) {
        repository.insert(task, new ToDoRepository.Callback<Long>() {
            @Override
            public void onResult(Long id) {
                loadAllTasks(); // refresh after insertion
            }
        });
    }

    public void updateTask(int id, String newTask) {
        repository.updateTask(id, newTask);
        loadAllTasks();
    }

    public void updateStatus(int id, int status) {
        repository.updateStatus(id, status);
        loadAllTasks();
    }

    public void deleteTask(int id) {
        repository.deleteTask(id);
        loadAllTasks();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdownExecutor();
    }
}
