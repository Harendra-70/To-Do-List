package com.shivamsingh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shivamsingh.practiceto_dolist.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> myList;
    private Context context;
    private DataBaseHelper myDB;

    public ToDoAdapter(Context context, List<ToDoModel> myList, DataBaseHelper myDB) {
        this.context = context;
        this.myList = myList;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.checkBox.setOnCheckedChangeListener(null); // 👈 clear any existing listener
        holder.checkBox.setText(myList.get(position).getTask());
        holder.checkBox.setChecked(toBoolean(myList.get(position).getStatus()));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int currentPosition = holder.getAdapterPosition(); // ✅ always up-to-date
                if (currentPosition == RecyclerView.NO_POSITION) return;

                if (buttonView.isChecked()) {
                    myDB.updateStatus(myList.get(currentPosition).getId(), 1);
                } else {
                    myDB.updateStatus(myList.get(currentPosition).getId(), 0);
                }
            }
        });

        holder.rowlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getAdapterPosition(); // ✅ always up-to-date
                if (currentPosition == RecyclerView.NO_POSITION) return false;

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete");
                dialog.setMessage("Are you sure you want to delete?");
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int id = myList.get(currentPosition).getId();
                        myList.remove(currentPosition);
                        notifyItemRemoved(currentPosition); // Notify adapter
                        myDB.deleteTask(id);

                    }
                });
                dialog.show();
                return true;
            }
        });

        holder.rowlayout.setOnClickListener(new View.OnClickListener() {
            EditText editTask;
            Button savebtn;

            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition(); // ✅ always up-to-date
                if (currentPosition == RecyclerView.NO_POSITION) return;

                //Dialog dialog=new Dialog(context);
                BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(R.layout.add_new_task_dialogbox);
                String oldTask = myList.get(currentPosition).getTask();

                editTask = dialog.findViewById(R.id.editText);
                editTask.setText(oldTask);
                savebtn = dialog.findViewById(R.id.savebtn);

                savebtn.setEnabled(!editTask.getText().toString().trim().isEmpty());
                savebtn.setAlpha(0.5f); // visually dim

                editTask.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Boolean flag = !editTask.getText().toString().trim().isEmpty();
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
                        String newTask = editTask.getText().toString().trim();

                        if (newTask.isEmpty()) {
                            Toast.makeText(context, "Please enter the task", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int id = myList.get(currentPosition).getId();
                        myList.get(currentPosition).setTask(newTask);

                        notifyItemChanged(currentPosition);
                        myDB.updateTask(id, newTask);

                        dialog.dismiss();

                    }

                });
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            }
        });

    }

    public Boolean toBoolean(int num) {
        return num != 0;
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        LinearLayout rowlayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            rowlayout = itemView.findViewById(R.id.rowlayout);
        }
    }
}

