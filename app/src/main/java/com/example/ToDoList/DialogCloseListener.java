package com.example.ToDoList;

import android.content.DialogInterface;

public interface DialogCloseListener {                      //DialogCloseListener interface contains the function which is going to do all the database and RecyclerView tasks such as reading,
                                                            //Refreshing and updating our RecyclerView
    public void handleDialogClose(DialogInterface dialog);
}
