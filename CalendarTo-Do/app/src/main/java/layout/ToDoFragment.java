package layout;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Date;

import edu.uw.rgrambo.calendarto_do.AddTodoFragment;
import edu.uw.rgrambo.calendarto_do.R;
import edu.uw.rgrambo.calendarto_do.TodoDatabase;

public class ToDoFragment extends Fragment {
    private SimpleCursorAdapter adapter;

    private onToDoFragmentInteractionListener mListener;

    public ToDoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_to_do, container, false);

        final String[] cols = new String[]{TodoDatabase.TodoDB.COL_TITLE};
        final int[] ids = new int[]{R.id.todoItem};

        adapter = new SimpleCursorAdapter(
                getActivity(), R.layout.todo,
                TodoDatabase.queryDatabase(getActivity()),
                cols, ids, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        final AdapterView todoView = (AdapterView)rootView.findViewById(R.id.todoList);
        todoView.setAdapter(adapter);


        Button addTodo = (Button)rootView.findViewById(R.id.addTodo);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment addTodo = new AddTodoFragment();
                addTodo.show(getFragmentManager(), "ADD_TODO_FRAGMENT");
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onToDoFragmentInteractionListener) {
            mListener = (onToDoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onToDoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
