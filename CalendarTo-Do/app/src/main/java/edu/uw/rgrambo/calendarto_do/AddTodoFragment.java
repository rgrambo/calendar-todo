package edu.uw.rgrambo.calendarto_do;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import layout.ToDoFragment;

/**
 * Created by holdenstegman on 3/8/16.
 */
public class AddTodoFragment extends android.support.v4.app.DialogFragment {

    String todoForText;


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


        final Bundle extras = getArguments();

        if (extras == null) {
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String title = ((TextView) rootView.findViewById(R.id.todoTitle)).getText().toString();
                    String tFor = ((TextView) rootView.findViewById(R.id.todoFor)).getText().toString();
                    if (!todoForText.equals("")) {
                        tFor = todoForText;
                    }
                    TodoItem todoItem = new TodoItem(title, tFor);
                    TodoDatabase.insertTodo(getActivity(), todoItem);

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
        } else {
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String title = ((TextView) rootView.findViewById(R.id.todoTitle)).getText().toString();
                    String tFor = ((TextView) rootView.findViewById(R.id.todoFor)).getText().toString();
                    if (!todoForText.equals("")) {
                        tFor = todoForText;
                    }
                    TodoItem todoItem = new TodoItem(title, tFor);
                    TodoDatabase.updateTodo(getActivity(), todoItem, extras.getInt("id"));

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

            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    TodoDatabase.deleteTodo(getActivity(), extras.getInt("id"));

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
        }

        todoForText = "";

        final AlertDialog addFragmentDialog = builder.create();

        final TextView todoTitle = (TextView) rootView.findViewById(R.id.todoTitle);
        final TextView todoFor = (TextView) rootView.findViewById(R.id.todoFor);
        final TextView todoHeader = (TextView) rootView.findViewById(R.id.addTodoTitle);
        final Spinner todoSpinner = (Spinner) rootView.findViewById(R.id.todoForSpinner);

        todoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todoForText = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing for now :)
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sharedPreferences.getBoolean("switch_enableTodoGroups", false)) {
            todoSpinner.setVisibility(View.VISIBLE);
            todoFor.setVisibility(View.INVISIBLE);
            //todoFor.setEnabled(false);

            // So we know temp isn't null so it's correctly retrieving stuff????
            String storedPeopleString = sharedPreferences.getString("test", null);
            if (storedPeopleString != null) {
                Gson gson = new Gson();
                ArrayList<String> values = gson.fromJson(storedPeopleString, ArrayList.class);
                Iterator<String> itr = values.iterator();
                while (itr.hasNext()) {
                    String toRemove = itr.next();
                    if (toRemove == null || toRemove.equals("")) {
                        itr.remove();
                    }
                }
                String[] container = new String[values.size()];
                values.toArray(container);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(),android.R.layout.simple_spinner_item,
                        container);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                todoSpinner.setAdapter(adapter);
            }

        } else {
            todoSpinner.setVisibility(View.GONE);
            todoFor.setVisibility(View.VISIBLE);
            //todoFor.setEnabled(true);
            todoForText = "";
        }

        if (extras != null) {
            // Extras not null so we popular our text fields
            todoHeader.setText("Editing a Todo");
            todoTitle.setText(extras.getString("title"));
            todoFor.setText(extras.getString("for"));
        }

        // Create the AlertDialog object and return it
        return addFragmentDialog;
    }
}