package com.example.ToDoList.Model;

public class Model {                                            //Custom java class that acts as a structure for holding the information for every item of the RecyclerView.

    private int id, status;                                     //Defines the variables id and status
    private String task;                                        //Defines the variable task


    //Getters and setters are used to protect data, particularly when creating classes.
    //Getter method returns its value while a setter method sets or updates its value.

    //Getter
    public int getId() {
        return id;
    }

    //Setter
    public void setId(int id) {
        this.id = id;
    }

    //Getter
    public int getStatus() {
        return status;
    }

    //Setter
    public void setStatus(int status) {
        this.status = status;
    }

    //Getter
    public String getTask() {
        return task;
    }

    //setter
    public void setTask(String task) {
        this.task = task;
    }
}
