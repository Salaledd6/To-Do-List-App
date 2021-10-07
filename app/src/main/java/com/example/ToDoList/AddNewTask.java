package com.example.ToDoList;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.ToDoList.Model.Model;
import com.example.ToDoList.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//With AddNewTask java class we can add new tasks to our recyclerview by pressing the floating action button

public class AddNewTask extends BottomSheetDialogFragment {                      //DialogFragment is a fragment that displays a dialog window, floating on top of its activity's window
                                                                                //We will be using BottomSheetDialogFragment instead.
                                                                                //This is a version of DialogFragment that shows a bottom sheet using BottomSheetDialog instead of a floating dialog
    //Creates some variables
    public static final String TAG = "ActionBottomDialog";                      //TAG uniquely identifies DialogFragment or any fragment in a application. ActionBottomDialog will be the name for the TAG
    private EditText newTaskText;                                               //defines the EditText user interface
    private Button newTaskSaveButton;                                           //Defines task save button

    private DatabaseHandler db;                                                 //Defines our database

    public static AddNewTask newInstance() {                                    //First defined function, newInstance. newInstance is used to return the object of AddNewTask
                                                                                // class so it can be used in MainActivity and will be able to call the functions of this AddNewTaskClass from there
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {                           //onCreate is used to start an activity. savedInstanceState variable checks if the instance of this fragment exist in the memory
                                                                                // or in the previous things itself
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);                            //Sets the style DialogStyle which was created in themes.xml
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);                          //Adds our view new_task.xml
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);          //This will allow help the bottom BottomSheetDialogFragment to readjust and move upwards
                                                                                                                //whenever we actually type something

        return view;                                                                                            //Will return the view. After the view is returned we will be able to create the view
                                                                                                                //and we will write onViewCreated function
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {                       //OnViewCreated we will define all the java code which is necessary to execute the functions in our dialog fragment
        super.onViewCreated(view, savedInstanceState);                                      //Initializes the super variables onViewCreated and pass the view as well as the bundle to our superclass

        //Defines the variables which we created before
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        db = new  DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;                                                           //This variable checks whether we are trying to update a task or trying to create a new one
                                                                                            //And depending on that different functions in the database gets executed

        final Bundle bundle = getArguments();                                               //getArguments is used to pass any data to a fragment.
                                                                                            //We will be getting the data from our adapter and we will be passing it to our this BottomSheetDialogFragment AddNewTask.java
                                                                                            //so suppose if I have to update a task i'll be passing the text of that task as well as status or something
                                                                                            //and then I can change it here and then click on save button to update the task

        if (bundle != null) {                                                               //Checks if bundle is not null.
            isUpdate = true;                                                                //Updates the task
            String task = bundle.getString("task");                                    //Retrieves the task
            newTaskText.setText(task);
            if(task.length()>0);                                                            //Checks if the task length which we have got is greater than zero. if it's not empty the save button activates
            newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));        //Color for the save button text after it's activated

        }
        newTaskText.addTextChangedListener(new TextWatcher() {                              //addTextChangedListener will check if something we are writing on and it will listen for the event
                                                                                            //This function is required, because  we want to change the color of our save button according to length of the task
                                                                                            //if there is nothing present on that task the save button will stay grey
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                                                            //Not required
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){                                               //if the task is empty ("" = empty)
                    newTaskSaveButton.setEnabled(false);                                   //Save button not active
                    newTaskSaveButton.setTextColor(Color.GRAY);                            //and the text color of it is gray
                }
                else{                                                                      //if the task is not empty
                    newTaskSaveButton.setEnabled(true);                                    //Save button activates
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)); //and sets the text color of it as blue
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                                                                                            //Not required
            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener(){                    //setOnClickListener registers a callback when the button is clicked
        @Override
        public void onClick(View v) {                                                       //Checks whether we are trying to update an already existing task or create a new task
            String text = newTaskText.getText().toString();                                 //Gets the text which needs to be passed on to the save button and the database
            if(finalIsUpdate){                                                              //if the task is updated
                db.updateTask(bundle.getInt("id"), text);                              //a different function will be called named as db.update
            }
            else{                                                                           //New task is being created
                Model task = new Model();                                                   //Defines the Model
                task.setTask(text);                                                         //Adds the task to our recyclerview and the database
                task.setStatus(0);
                db.insertTask(task);
            }
            dismiss();                                                                      //Leaves from the bottom sheet dialog after clicking on save button or pressing back
        }
        });
    }
    @Override
    public void onDismiss(DialogInterface dialog){                                          //Recyclerview automatically updates on dismiss and not done manually by the user or after restarting the application
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){                                        //DialogCloseListener reads, refreshes and updates our RecyclerView
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}