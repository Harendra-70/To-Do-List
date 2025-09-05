package com.shivamsingh.practiceto_dolist;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
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

import com.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shivamsingh.DataBaseHelper;
import com.shivamsingh.ToDoAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ToDoModel>myList;
    RecyclerView recyclerView;
    DataBaseHelper myDB;
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
            // Keep on screen until 2 seconds have passed
            return System.currentTimeMillis() - splashStartTime < 2000;
        });
        recyclerView=findViewById(R.id.recyclerView);
        myDB=new DataBaseHelper(this);


        addbtn=findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            EditText editText;
            Button savebtn;
            @Override
            public void onClick(View v) {

               // Dialog dialog=new Dialog(MainActivity.this);
                BottomSheetDialog dialog =new BottomSheetDialog(MainActivity.this);
                dialog.setContentView(R.layout.add_new_task_dialogbox);
                editText =dialog.findViewById(R.id.editText);
                savebtn=dialog.findViewById(R.id.savebtn);

                // Initially disable the button
                savebtn.setEnabled(false);
                savebtn.setAlpha(0.5f); // visually dim
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                           Boolean flag= !editText.getText().toString().trim().isEmpty();
                           savebtn.setEnabled(flag);
                           savebtn.setAlpha(flag ? 1.0f : 0.5f); // Bright if enabled
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                savebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task = editText.getText().toString().trim();

                        if(!task.isEmpty()) {

                            task = editText.getText().toString();
                            ToDoModel item = new ToDoModel();
                            item.setTask(task);
                            long id= myDB.insertTask(item);

                          /*  myList.clear();
                            myList.addAll(myDB.getAllTask());
                            Collections.reverse(myList);*/
                            item.setId((int) id);
                            myList.add(0, item); // Add at top
                            adapter.notifyItemInserted(0);
                            recyclerView.scrollToPosition(0);
                            // myList=myDB.getAllTask();  if you call this after adapter is set, the adapter still holds the old list reference — and won’t reflect new data properly unless reset.
                           /* adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(0);*/
                            dialog.dismiss();

                        }else {
                            Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                dialog.show();
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myList = new ArrayList<>();
        myList.addAll(myDB.getAllTask());
        adapter=new ToDoAdapter(MainActivity.this,myList,myDB);
        recyclerView.setAdapter(adapter);

    }
}