package com.example.ToDoList.Utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ToDoList.Model.Model;

import java.util.ArrayList;
import java.util.List;
//DatabaseHandler will handle all the adding, deleting or updating tasks of this project

public class DatabaseHandler extends SQLiteOpenHelper {                             //SQLiteOpenHelper is a helper class to manage database creation and version management.

    private static final int VERSION = 1;                                           //This line of code defines the version of the database. 1 is the default one
    private static final String NAME = "ListDatabase";                              //Defines the name for the database
    private static final String TABLE = "db";                                       //Defines the table name for the database. Database table is where all the data in a database is stored
    private static final String ID = "id";                                          //Defines the column names which is id
    private static final String TASK = "task";                                      //The actual text which will be stored
    private static final String STATUS = "status";                                  //Status of the database

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK + " TEXT, " + STATUS + " INTEGER)";  //Creates the table for database

    private SQLiteDatabase db;                                                      //Reference of the SQLiteDatabase which will be made


    public DatabaseHandler(Context context) {                                      //Constructor for Database helper class
        super(context, NAME, null, VERSION);                                //Initializes the super class with context
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);                                                   //Executes the CREATE_TABLE

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {             //onUpgrade method is responsible for upgrading the database when changes are made in schema.
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        //Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();                                                   //Opens the database
    }

    public void insertTask(Model task) {                                                   //inserts tasks and passes the Model.java
        ContentValues cv = new ContentValues();                                            //ContentValues is used to load variables on to the db
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TABLE, null, cv);                                         //inserts the table to database
    }

    public List<Model> getAllTasks() {                                                      //Will get all the tasks from database and store it in arrayList
        List<Model> taskList = new ArrayList<>();                                           //Initializes the new empty ArrayList
        Cursor cur = null;                                                                  //cursor is a mouse for database. Selects items from db
        db.beginTransaction();                                                              //opens the db to be accessed
        try {
            cur = db.query(TABLE, null, null, null, null, null, null, null);    //Returns all rows from the database without any criteria

            if(cur != null){                                                                //Checks if cursor is not null
              if(cur.moveToFirst()){                                                        //Move the cursor to first row. This method will return false if the cursor is empty
                do{
                    Model task = new Model();                                               //New Model object
                    task.setId(cur.getInt(cur.getColumnIndex(ID)));                         //Gets the ID from cursor
                    task.setTask(cur.getString(cur.getColumnIndex(TASK)));                  //Gets the TASK from cursor
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));                 //Gets the STATUS from cursor
                    taskList.add(task);                                                     //Adds this task to taskList
                 }while(cur.moveToNext());                                                  //While the cursor is empty code runs and we will have our task
              }
            }
        }
        finally {                                                                           //The finally block in java is used to put important codes such as cleanup code e.g. Closing the file or closing the connection
            db.endTransaction();                                                            //Closes the db
            cur.close();                                                                    //Closes the cursor, releasing all of its resources and making it completely invalid
        }
        return taskList;                                                                    //Returns the taskList
    }
    public void updateStatus(int id, int status){                                           //Updates the status of the task. (if the checkbox is checked or not)
        ContentValues cv = new ContentValues();                                             //ContentValues is used to load variables on to the db
        cv.put(STATUS, status);
        db.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});    //Updates the database
    }

    public void updateTask(int id, String task){                                            //Updates the task (if the name of the task is changed)
        ContentValues cv = new ContentValues();                                             //ContentValues is used to load variables on to the db
        cv.put(TASK, task);
        db.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});    //updates the database
    }
    public void deleteTask(int id){                                                         //Deletes the task from database
        db.delete(TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

    public boolean deleteAll() {                                                            //This function is used to delete all the tasks from the database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);                                        //Deletes/drops the table
        onCreate(db);                                                                       //Ce

        return true;
    }


}