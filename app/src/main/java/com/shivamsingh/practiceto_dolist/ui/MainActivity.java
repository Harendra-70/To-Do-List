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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shivamsingh.practiceto_dolist.R;
import com.shivamsingh.practiceto_dolist.adapter.ToDoAdapter;
import com.shivamsingh.practiceto_dolist.database.ToDoDatabase;
import com.shivamsingh.practiceto_dolist.database.ToDoEntity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ToDoEntity> toDoList;
    RecyclerView recyclerView;
    ToDoDatabase myDB;
    ToDoAdapter adapter;
    FloatingActionButton addbtn;

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

        // Delay splash screen removal for 2 seconds
        long splashStartTime = System.currentTimeMillis();
        splashScreen.setKeepOnScreenCondition(() -> {
            return System.currentTimeMillis() - splashStartTime < 2000;
        });

        recyclerView = findViewById(R.id.recyclerView);
        myDB = ToDoDatabase.getInstance(this);

        addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            EditText editText;
            Button savebtn;

            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
                dialog.setContentView(R.layout.add_new_task_dialogbox);
                editText = dialog.findViewById(R.id.editText);
                savebtn = dialog.findViewById(R.id.savebtn);

                // Initially disable the button
                if (savebtn != null) {
                    savebtn.setEnabled(false);
                    savebtn.setAlpha(0.5f);
                }


                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        boolean flag = !editText.getText().toString().trim().isEmpty();
                        savebtn.setEnabled(flag);
                        savebtn.setAlpha(flag ? 1.0f : 0.5f);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task = editText.getText().toString().trim();

                        if (!task.isEmpty()) {
                            ToDoEntity entity = new ToDoEntity(task, 0);
                            long id = myDB.toDoDao().insertTask(entity);

                            entity.setId((int) id);
                            toDoList.add(0, entity);
                            adapter.notifyItemInserted(0);
                            recyclerView.scrollToPosition(0);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toDoList = (ArrayList<ToDoEntity>) myDB.toDoDao().getAllTasks();
        adapter = new ToDoAdapter(MainActivity.this, toDoList, myDB);
        recyclerView.setAdapter(adapter);
    }
}
