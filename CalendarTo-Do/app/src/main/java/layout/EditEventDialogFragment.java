package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import java.text.SimpleDateFormat;

import edu.uw.rgrambo.calendarto_do.Event;
import edu.uw.rgrambo.calendarto_do.R;
import edu.uw.rgrambo.calendarto_do.TodoDatabase;

/**
 * Created by rossgrambo on 3/9/16.
 */
public class EditEventDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    int repeat = 0;
    View view;
    Event event;

    private DateTime date = DateTime.now();

    public EditEventDialogFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_edit_event_dialog, null);

        Bundle bundle = this.getArguments();
        final int eventId = bundle.getInt("id");
        final int offset = bundle.getInt("offset");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Cursor cursor = TodoDatabase.getCalendar (getContext(), eventId);
        cursor.moveToFirst();

        // Populate view with current data
        Event event;
        try {

            Log.e("TAG", "Start");
            event = new Event(
                cursor.getInt(cursor.getColumnIndex(TodoDatabase.CalendarDB._ID)),
                cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_TITLE)),
                cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_NOTE)),
                cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_OWNER)),
                new DateTime(format.parse(cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_START_TIME)))),
                new DateTime(format.parse(cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_END_TIME)))),
                cursor.getInt(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_REPEAT))
            );

            ((EditText)view.findViewById(R.id.eventTitle)).setText(event.getTitle());
            ((EditText)view.findViewById(R.id.eventNote)).setText(event.getNote());
            ((EditText)view.findViewById(R.id.eventOwner)).setText(event.getOwner());

            TimePicker startTimePicker = ((TimePicker)view.findViewById(R.id.eventStartTime));
            startTimePicker.setCurrentHour(event.getStartTime().getHourOfDay());
            startTimePicker.setCurrentMinute(event.getStartTime().getMinuteOfDay());
            TimePicker endTimePicker = ((TimePicker)view.findViewById(R.id.eventEndTime));
            endTimePicker.setCurrentHour(event.getEndTime().getHourOfDay());
            endTimePicker.setCurrentMinute(event.getEndTime().getMinuteOfDay());

            Log.e("TAG", "END");
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String title = ((EditText)view.findViewById(R.id.eventTitle)).getText().toString();
                        String note = ((EditText)view.findViewById(R.id.eventNote)).getText().toString();
                        String owner = ((EditText)view.findViewById(R.id.eventOwner)).getText().toString();

                        TimePicker startTimePicker = (TimePicker)view.findViewById(R.id.eventStartTime);
                        DateTime start = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                                startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());

                        TimePicker endTimePicker = (TimePicker)view.findViewById(R.id.eventEndTime);
                        DateTime end = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                                startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());

                        Event newEvent = new Event(title, note, owner, start, end, repeat);
                        TodoDatabase.updateCalendar(getContext(), newEvent, eventId);

                        GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
                        CalendarFragment.populateGrid(gridView, (TextView) getActivity().findViewById((R.id.monthTitle)), getContext(), getActivity(), offset);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TodoDatabase.deleteCalendar(getContext(), eventId);

                        GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
                        CalendarFragment.populateGrid(gridView, (TextView) getActivity().findViewById((R.id.monthTitle)), getContext(), getActivity(), offset);
                    }
                });

        // Add the content to the spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.eventSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        repeat = pos;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

