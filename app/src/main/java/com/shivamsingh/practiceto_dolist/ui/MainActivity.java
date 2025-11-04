package com.shivamsingh.practiceto_dolist.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shivamsingh.practiceto_dolist.R;
import com.shivamsingh.practiceto_dolist.adapter.ToDoAdapter;
import com.shivamsingh.practiceto_dolist.database.ToDoEntity;
import com.shivamsingh.practiceto_dolist.viewmodel.ToDoViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<ToDoEntity> toDoList;
    RecyclerView recyclerView;
    ToDoAdapter adapter;
    FloatingActionButton addBtn;

    ToDoViewModel toDoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Keep splash for 2 seconds
        long splashStartTime = System.currentTimeMillis();
        splashScreen.setKeepOnScreenCondition(() ->
                System.currentTimeMillis() - splashStartTime < 2000
        );

        // Initialize
        recyclerView = findViewById(R.id.recyclerView);
        addBtn = findViewById(R.id.addbtn);
        toDoViewModel=new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoList = new ArrayList<>();
        adapter = new ToDoAdapter(this, toDoList, toDoViewModel);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Observe LiveData
        toDoViewModel.getTasks().observe(this, new Observer<List<ToDoEntity>>() {
            @Override
            public void onChanged(List<ToDoEntity> result) {
                toDoList.clear();
                toDoList.addAll(result);
                adapter.notifyDataSetChanged();
            }
        });

        // Add button click
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });
    }

    private void showAddTaskDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
        dialog.setContentView(R.layout.add_new_task_dialogbox);

        final EditText editText = dialog.findViewById(R.id.editText);
        final Button saveBtn = dialog.findViewById(R.id.savebtn);

        if (editText == null || saveBtn == null) {
            dialog.dismiss();
            return;
        }

        // Initially disable save button
        saveBtn.setEnabled(false);
        saveBtn.setAlpha(0.5f);

        // Enable/disable button based on input
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = !editText.getText().toString().trim().isEmpty();
                saveBtn.setEnabled(hasText);
                saveBtn.setAlpha(hasText ? 1.0f : 0.5f);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Save button click
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = editText.getText().toString().trim();
                if (taskText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                    return;
                }

                final ToDoEntity task = new ToDoEntity(taskText, 0);
                toDoViewModel.insertTask(task);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
