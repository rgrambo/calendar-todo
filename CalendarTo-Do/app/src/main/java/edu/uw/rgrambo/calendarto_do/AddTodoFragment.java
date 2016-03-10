package edu.uw.rgrambo.calendarto_do;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import layout.ToDoFragment;

/**
 * Created by holdenstegman on 3/8/16.
 */
public class AddTodoFragment extends android.support.v4.app.DialogFragment {


    public AddTodoFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.fragment_add_todo, null);
        final AdapterView todoView = (AdapterView) getActivity().findViewById(R.id.todoList);

        builder.setView(rootView);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        final AlertDialog addFragmentDialog = builder.create();

        final TextView todoTitle = (TextView) rootView.findViewById(R.id.todoTitle);
        final TextView todoFor = (TextView) rootView.findViewById(R.id.todoFor);
        final TextView todoHeader = (TextView) rootView.findViewById(R.id.addTodoTitle);
        final Button recordTodo = (Button) rootView.findViewById(R.id.recordTodo);
        final Button updateTodo = (Button) rootView.findViewById(R.id.updateTodo);
        final Button deleteTodo = (Button) rootView.findViewById(R.id.deleteTodo);

        final Bundle extras = getArguments();

        if (extras == null) {
            updateTodo.setVisibility(View.INVISIBLE);
            deleteTodo.setVisibility(View.INVISIBLE);

        } else {
            recordTodo.setVisibility(View.INVISIBLE);
            // Extras not null so we popular our text fields
            todoHeader.setText("Editing a Todo");
            todoTitle.setText(extras.getString("title"));
            todoFor.setText(extras.getString("for"));
        }

        recordTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = todoTitle.getText().toString();
                String tFor = todoFor.getText().toString();
                TodoItem todoItem = new TodoItem(title, tFor);
                TodoDatabase.insertTodo(getActivity(), todoItem);
                addFragmentDialog.dismiss();

                final String[] cols = new String[]{TodoDatabase.TodoDB.COL_TITLE, TodoDatabase.TodoDB.COL_TODOFOR};
                final int[] ids = new int[]{R.id.todoItem, R.id.todoFor};

                todoView.setAdapter(new SimpleCursorAdapter(
                        getActivity(), R.layout.todo,
                        TodoDatabase.queryDatabase(getActivity()),
                        cols, ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                ));

                Toast.makeText(getActivity(), "Added a new Todo!", Toast.LENGTH_LONG).show();
            }
        });

        updateTodo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = todoTitle.getText().toString();
                String tFor = todoFor.getText().toString();
                TodoItem todoItem = new TodoItem(title, tFor);
                TodoDatabase.updateTodo(getActivity(), todoItem, extras.getInt("id"));
                addFragmentDialog.dismiss();

                final String[] cols = new String[]{TodoDatabase.TodoDB.COL_TITLE, TodoDatabase.TodoDB.COL_TODOFOR};
                final int[] ids = new int[]{R.id.todoItem, R.id.todoFor};

                todoView.setAdapter(new SimpleCursorAdapter(
                        getActivity(), R.layout.todo,
                        TodoDatabase.queryDatabase(getActivity()),
                        cols, ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                ));

                Toast.makeText(getActivity(), "Updated your todo!", Toast.LENGTH_LONG).show();
            }
        });

        deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoDatabase.deleteTodo(getActivity(), extras.getInt("id"));
                addFragmentDialog.dismiss();

                final String[] cols = new String[]{TodoDatabase.TodoDB.COL_TITLE, TodoDatabase.TodoDB.COL_TODOFOR};
                final int[] ids = new int[]{R.id.todoItem, R.id.todoFor};

                todoView.setAdapter(new SimpleCursorAdapter(
                        getActivity(), R.layout.todo,
                        TodoDatabase.queryDatabase(getActivity()),
                        cols, ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                ));

                Toast.makeText(getActivity(), "Deleted your todo!", Toast.LENGTH_LONG).show();
            }
        });
        // Create the AlertDialog object and return it
        return addFragmentDialog;
    }

}