package com.example.ToDoList.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDoList.AddNewTask;
import com.example.ToDoList.MainActivity;
import com.example.ToDoList.Model.Model;
import com.example.ToDoList.R;
import com.example.ToDoList.Utils.DatabaseHandler;

import java.util.List;

                                                                                  //An Adapter is a logic and main code for RecyclerView that renders and processes everything related to recyclerview and is responsible for it.
                                                                                  //Adapter connects our data to RecyclerView

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {           //Extends the RecyclerView.Adapter class

    private List<Model> list;
    private MainActivity activity;
    private DatabaseHandler db;

    public Adapter(DatabaseHandler db,MainActivity activity){                     //Defines the constructor named Adapter and passes the context of MainActivity
                                                                                  //Passes the database
        this.db = db;
        this.activity = activity;
    }

    //The ViewHolder is a java class, part of the Adapter that stores the reference to the card layout views
    //that have to be dynamically modified during the execution of the program by a list of data obtained either by online databases or added in some other way.
    //ViewHolder design pattern is used to speed up rendering of your ListView actually to make it work smoothly,

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){        //Defines the ViewHolder function and passes the ViewGroup parent and int ViewType
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);      //task_layout will be inflated in the RecyclerView
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){              //This method internally calls to update the RecyclerView.ViewHolder contents with the item at the given position
        db.openDatabase();
        Model item = list.get(position);
        holder.task.setText(item.getTask());                                    //Sets the text to be displayed
        holder.task.setChecked(toBoolean(item.getStatus()));                    //Changes the checked state of this button.
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {       //Registers a callback to be invoked when the checked state of this button changes.
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){                                                              //if the task is checked
                    db.updateStatus(item.getId(), 1);                                //status is 1
                }
                else{                                                                       //if the task is not checked
                    db.updateStatus(item.getId(), 0);                                //status is 0
                }
            }
        });
    }
    @Override
    public int getItemCount(){                                                  //returns The number of items currently available in adapter
        return list.size();
    }

    private boolean toBoolean(int n){                                           //returns true or false depending on whether n is 0 or not
        return n!=0;
    }
    public Context getContext(){
        return activity;
    }
    public void setTasks(List<Model> list){                                     //RecyclerView can show tasks
        this.list = list;
        notifyDataSetChanged();                                                 //Notify any registered observers that the data set has changed.
    }

    public void deleteItem(int postition){
        Model item = list.get(postition);                                       //Item which will be deleted
        db.deleteTask(item.getId());                                            //Id will be passed to database and then the databaseHandler will delete the task
        list.remove(postition);
        notifyItemRemoved(postition);                                           //Notifies our RecyclerView that something has been removed. It will automatically update the view whenever the item is deleted

    }

    public void editItem(int position){
        Model item = list.get(position);                                        //gets the item which you want to update
        Bundle bundle = new Bundle();                                           //passes it to bottom sheet fragment
        bundle.putInt("id", item.getId());                                      //Inserts an int value into the mapping of this Bundle
        bundle.putString("task",item.getTask());                                //Inserts a String value into the mapping of this Bundle

        AddNewTask fragment = new AddNewTask();                                 //Adds a fragment. Fragment represents a reusable portion of your app's UI.A fragment defines and manages its own layout,  has its own
                                                                                //lifecycle, and can handle its own input events. Fragments cannot live on their own--they must be hosted by an activity or another fragment.

        fragment.setArguments(bundle);                                          //SetArguments is used to pass a bundle
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);    //Will show the fragment

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;                                                          //Imports the CheckBox

        ViewHolder(View view){
        super(view);
        task = view.findViewById(R.id.CheckBox);
        }
    }
}