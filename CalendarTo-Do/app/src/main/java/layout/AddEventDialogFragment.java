package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
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

import org.joda.time.DateTime;

import edu.uw.rgrambo.calendarto_do.Event;
import edu.uw.rgrambo.calendarto_do.R;
import edu.uw.rgrambo.calendarto_do.TodoDatabase;

public class AddEventDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

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

        // Add the content to the spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.eventSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.name_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String title = ((EditText)view.findViewById(R.id.eventTitle)).getText().toString();
                        String note = ((EditText)view.findViewById(R.id.eventNote)).getText().toString();
                        String owner = Owner;

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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Owner = getResources().getStringArray(R.array.name_array)[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

