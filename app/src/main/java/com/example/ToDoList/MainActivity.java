package com.example.ToDoList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ToDoList.Adapter.Adapter;
import com.example.ToDoList.Model.Model;
import com.example.ToDoList.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {    //Extends AppCompatActivity  tells Java that a MainActivity is an example of an Android AppCompatActivity
                                                                                        //When you extend an existing Java class (such as the AppCompatActivity class),
                                                                                        //you create a new class with the existing class’s functionality.
                                                                                        //Also implements the DialogCloseListener.
    private RecyclerView tasksRecyclerView;
    private Adapter tasksAdapter;
    private List<Model> taskList;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    ImageView  DeleteAllTasksButton;

    private static final int TIME_INTERVAL = 2000;
    private long backPressed;


                                                                                          //Overriding is a feature that allows a subclass or child class to provide
        @Override                                                                         // a specific implementation of a method that is already provided by one of its super-classes or parent
        public void onCreate(Bundle savedInstanceState) {                                 //onCreate is used to start an activity

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                                           // Sets the XML file activity_main as your main layout when the app starts
        Objects.requireNonNull(getSupportActionBar()).hide();                             // hides the action bar from the app
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);            //Forces the app to use portrait orientation

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);       //Using the dark theme on device has no effect on app

            db = new DatabaseHandler(this);
            db.openDatabase();


        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);                         //Finds a view that was identified by the id attribute from the XML
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new Adapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);                                       // tells the recyclerview which adapter to use which in this case is tasksAdapter

            fab = findViewById(R.id.fab);                                                 //Finds a Floating Action Button that was identified by id XML attribute in activity_main.xml

            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
            itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.getAllTasks();                                                      //Gets the taskList from database
        Collections.reverse(taskList);                                                    //Reverses the taskList
            tasksAdapter.setTasks(taskList);                                              //calls the setTasks function to show the tasks in RecyclerView

            fab.setOnClickListener(new View.OnClickListener() {                           //Registers a callback to be invoked when the fab is clicked
                @Override
                public void onClick(View v) {
                    AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG); //Displays the dialog when floating action button is pressed
                }
            });

            DeleteAllTasksButton= (ImageView) findViewById(R.id.DeleteAllTasksButton);      //Finds the DeleteAllTasksButton that was identified by the android:id XML attribute
            DeleteAllTasksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(tasksAdapter.getContext());

                    builder.setTitle("Delete Tasks");                                       //Title displayed on AlertDialog after pressing the icon
                    builder.setMessage("Are you sure you want to delete all the tasks?");   //Message displayed on AlertDialog after pressing the icon
                    builder.setPositiveButton("Confirm",                               //Positive button for the AlertDialog

                            new DialogInterface.OnClickListener() {                         //Interface used to allow the creator of a dialog to run some code when an item on the dialog is clicked.

                                @Override
                                public void onClick(DialogInterface dialog, int which) {                    //When Confirm button is pressed

                                    taskList.clear();                                                      //Removes all the elements from the list
                                    tasksAdapter.notifyDataSetChanged();                                   //notifies the adapter that data is changed and refreshes the RecyclerView
                                    db.deleteAll();                                                        //Defined in DatabaseHandler. Deletes all the task from the database

                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {  //Cancel button for the AlertDialog.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //leaves from the AlertDialog view

                        }
                    });
                    AlertDialog dialog = builder.create();                              //Creates an AlertDialog with the arguments supplied to this builder.
                    dialog.show();                                                      //Displays the dialog on screen
                }
            });
        }

    @Override
    public void handleDialogClose(DialogInterface dialog){
            taskList = db.getAllTasks();                                                    //Gets the taskList from database
            Collections.reverse(taskList);                                                  //By reversing the taskList, recently created task will appear on top
            tasksAdapter.setTasks(taskList);                                                //Sets the taskList to Adapter
            tasksAdapter.notifyDataSetChanged();                                            //Notifies the adapter that the data has changed

    }

    @Override
    public void onBackPressed(){                                                            //This function defines the press back again to exit app functionality. Used by many apps and
                                                                                            //helps to prevent the accidental exit of the user from the app.
        if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(getBaseContext(), "Press Back Again to exit app", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();                                           //Allow us to exit from the app after pressing the back button 2 times
    }

    
}