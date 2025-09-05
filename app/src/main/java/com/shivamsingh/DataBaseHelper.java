package com.shivamsingh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="TODO_DATBASE";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME ="TODO_TABLE";
    private static final String COL_1="ID";
    private static final String COL_2="TASK";
    private static final String COL_3="STATUS";

    public DataBaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public long insertTask(ToDoModel model){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,model.getTask());
        contentValues.put(COL_3,0);

        return db.insert(TABLE_NAME,null,contentValues);
    }

    public  void  updateTask(int id,String task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(COL_2,task);

        db.update(TABLE_NAME,contentValues,COL_1 + " = " + id,null);
        /*db.update(TABLE_NAME,contentValues,COL_1 + " =? ", new String[]{String.valueOf(id) });*/
        /*db.update(TABLE_NAME,contentValues," ID=? ", new String[]{String.valueOf(id) });*/
    }

    public  void updateStatus(int id, int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(COL_3,status);

        /* db.update(TABLE_NAME,contentValues,COL_1 +" = " + id,null);*/
        db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{String.valueOf(id)});

    }

    public  void deleteTask(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,COL_1 +" = " +id,null);
    }

    public List<ToDoModel> getAllTask(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=null;
        List<ToDoModel> modelList=new ArrayList<>();

        try{
            cursor=db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC " ,null);
            if(cursor !=null){
                while(cursor.moveToNext()){
                    ToDoModel toDoModel=new ToDoModel();
                    toDoModel.setId(cursor.getInt(0));
                    toDoModel.setTask(cursor.getString(1));
                    toDoModel.setStatus(cursor.getInt(2));
                    modelList.add(toDoModel);
                }
            }
        }finally {
            if(cursor!=null){
                cursor.close();
            }
           db.close();
        }
        return  modelList;
    }
}
