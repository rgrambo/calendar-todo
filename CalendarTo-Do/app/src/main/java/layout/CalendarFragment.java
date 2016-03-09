package layout;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.uw.rgrambo.calendarto_do.CalendarDayAdapter;
import edu.uw.rgrambo.calendarto_do.Event;
import edu.uw.rgrambo.calendarto_do.R;
import edu.uw.rgrambo.calendarto_do.TodoDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnCalendarDayInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private OnCalendarDayInteractionListener mListener;

    private GridView gridView;
    private static DateTime[] dates;

    // Required Empty Constructor
    public CalendarFragment() {}

    /**
     * Use this factory method to create a new instance of this fragment
     *
     * @return A new instance of fragment CalendarFragment.
     */
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        gridView = (GridView) view.findViewById(R.id.gridview);

        // Set to the current month
        ((TextView)view.findViewById(R.id.monthTitle)).setText(
                new SimpleDateFormat("MMMM").format(Calendar.getInstance().getTime()));


        populateGrid(gridView, getContext(), getActivity());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateTime date = dates[position];

                // Create the AlertDialog
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = new AddEventDialogFragment();

                Bundle args = new Bundle();
                args.putString("date", date.toString());
                newFragment.setArguments(args);

                newFragment.show(ft, "dialog");
            }
        });

        return view;
    }

    public static void populateGrid(GridView gridView, Context context, Activity activity) {

        // Create the calendar
        Calendar calendar = Calendar.getInstance();

        DateTime dateTime = new DateTime(calendar);

        // Set to previous month
        dateTime = dateTime.minusMonths(1);
        // Set to last day in that month
        dateTime = dateTime.dayOfMonth().withMaximumValue();
        // Set to sunday of that week
        dateTime = dateTime.minusDays(dateTime.getDayOfWeek()-1);

        dates = new DateTime[7 * 6];
        for (int i = 0; i < 7 * 6; i++) {
            dates[i] = dateTime;
            dateTime = dateTime.plusDays(1);
        }

        List<Event> events = new ArrayList<Event>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Cursor cursor = TodoDatabase.queryDatabaseForCalendar(context);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                events.add(new Event(
                        cursor.getInt(cursor.getColumnIndex(TodoDatabase.CalendarDB._ID)),
                        cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_NOTE)),
                        cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_OWNER)),
                        new DateTime(format.parse(cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_START_TIME)))),
                        new DateTime(format.parse(cursor.getString(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_END_TIME)))),
                        cursor.getInt(cursor.getColumnIndex(TodoDatabase.CalendarDB.COL_REPEAT))
                ));
            } catch (Exception e) { }
            cursor.moveToNext();
        }

        CalendarDayAdapter calendarDayAdapter = new CalendarDayAdapter(gridView,
                activity, dates, events);

        gridView.setAdapter(calendarDayAdapter);
    }

    public void onCalendarDayInteract(Date date) {
        if (mListener != null) {
            mListener.onCalendarDayInteraction(date);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarDayInteractionListener) {
            mListener = (OnCalendarDayInteractionListener) context;
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

    public interface OnCalendarDayInteractionListener {

        void onCalendarDayInteraction(Date date);
    }
}
