package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Iterator;

import edu.uw.rgrambo.calendarto_do.Event;
import edu.uw.rgrambo.calendarto_do.R;
import edu.uw.rgrambo.calendarto_do.TodoDatabase;

public class AddEventDialogFragment extends DialogFragment {

    String Owner = "";
    View view;

    private DateTime date = DateTime.now();

    public AddEventDialogFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_add_event_dialog, null);

        Bundle bundle = this.getArguments();
        date = DateTime.parse(bundle.getString("date"));
        final int offset = bundle.getInt("offset");

        Spinner eventSpinner = (Spinner) view.findViewById(R.id.eventSpinner);
        final EditText eventFor = (EditText) view.findViewById(R.id.eventOwner);

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Owner = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing for now :)
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sharedPreferences.getBoolean("switch_enableTodoGroups", false)) {
            eventSpinner.setVisibility(View.VISIBLE);
            eventFor.setVisibility(View.GONE);
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
                eventSpinner.setAdapter(adapter);
            }

        } else {
            eventSpinner.setVisibility(View.GONE);
            eventFor.setVisibility(View.VISIBLE);
            //todoFor.setEnabled(true);
            Owner = "";
        }

        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String title = ((EditText)view.findViewById(R.id.eventTitle)).getText().toString();
                        String note = ((EditText)view.findViewById(R.id.eventNote)).getText().toString();
                        String owner;
                        if (Owner.equalsIgnoreCase("")) {
                            owner = Owner;
                        } else {
                            owner = eventFor.getText().toString();
                        }

                        TimePicker startTimePicker = (TimePicker)view.findViewById(R.id.eventStartTime);
                        DateTime start = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                                startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());

                        TimePicker endTimePicker = (TimePicker)view.findViewById(R.id.eventEndTime);
                        DateTime end = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                                startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());

                        Event newEvent = new Event(title, note, owner, start, end);
                        TodoDatabase.insertCalender(getContext(), newEvent);

                        GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
                        CalendarFragment.populateGrid(gridView, (TextView) getActivity().findViewById((R.id.monthTitle)), getContext(), getActivity(), offset);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

