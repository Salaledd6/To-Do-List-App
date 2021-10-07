package com.example.ToDoList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ToDoList.Adapter.Adapter;


                                                                                     //itemTouchHelper is a utility class to add swipe to dismiss and drag & drop support to RecyclerView.
                                                                                     //We can delete the tasks by swiping left and edit a task by swiping right
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private Adapter adapter;                                                         //This adapter is used to see which element we are trying to work upon

    public RecyclerItemTouchHelper(Adapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);    //itemTouchHelper support class will now support both left and right swipe functions
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){       //onMove is default method. Actually not required, but it's to be defined
    return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){                                              //Contains all the code for swipe functions

        final int position = viewHolder.getAdapterPosition();                                                                   //Returns the Adapter position of the item represented by this ViewHolder

        if(direction == ItemTouchHelper.LEFT){                                                                                  //If the task is being swiped to left
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());                                        //Defines the AlertDialog Dialog
            builder.setTitle("Delete Task");                                                                                    //Title displayed on AlertDialog after swiping to left
            builder.setMessage("Are you sure you want to delete this task?");                                                   //Message displayed on AlertDialog after swiping to left
             builder.setPositiveButton("Confirm",                                                                          //Confirm button for the dialog
                    new DialogInterface.OnClickListener() {                                                         //Interface used to allow the creator of a dialog to run some code when an item on the dialog is clicked.
                                                                                                                    //This is for the positive button
                        @Override
                        public void onClick(DialogInterface dialog, int which) {                                                //When Confirm button is pressed
                            adapter.deleteItem(position);                                                                       //Deletes the task
                        }
                    });
             builder.setNegativeButton(android.R.string.cancel,                                                                 //Cancel button for the dialog
                     new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {                                                       //When button is pressed
                     adapter.notifyItemChanged(viewHolder.getAdapterPosition());                             //Notifies the adapter that the item at position has changed.Returns the adapter position of the item
                                                                                                                    //leaves from the AlertDialog view
                 }
             });
             AlertDialog dialog = builder.create();
             dialog.show();
        }
        else{                                                                                                       //if the task is being swiped to right instead
            adapter.editItem(position);                                                                             //Edits the name of the task. editItem is defined in Adapter.java class
        }
    }

      @Override
      //This function is used to display the delete and edit icons when swiping to left or right
      public void onChildDraw( Canvas c,  RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            Drawable icon;
            ColorDrawable background;

            View itemView = viewHolder.itemView;                                                                         //Defines the view itemView

            int backgroundCornerOffset = 20;                                                                             //Helps to declare the offset background

          if(dX > 0){                                                                                                    //Checks if the X is greater than zero when swiping to right
              icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);                       //Shows the edit icon while swiping to right
              background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark));    // Gets the background color for when the task is being swiped to right
          }
          else{
              icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);                     //Shows the delete icon while swiping to left
              background = new ColorDrawable(Color.RED);                                                                 // Gets the background color for when the task is being swiped to left
          }

          int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
          int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
          int iconBottom = iconTop + icon.getIntrinsicHeight();

           if (dX > 0) {     // Swiping to the right

               //Positions of the icon
              int iconLeft = itemView.getLeft() + iconMargin;
              int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
              icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

              background.setBounds(itemView.getLeft(), itemView.getTop(),
                      itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
          } else if (dX < 0) { // Swiping to the left

               //Positions of the icon
              int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
              int iconRight = itemView.getRight() - iconMargin;
              icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

              background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                      itemView.getTop(), itemView.getRight(), itemView.getBottom());
          } else { // view is unSwiped
              background.setBounds(0, 0, 0, 0);
          }

          background.draw(c);
          icon.draw(c);
    }
}