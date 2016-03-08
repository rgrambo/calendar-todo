package edu.uw.rgrambo.calendarto_do;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import layout.AddEventDialogFragment;
import layout.EventButton;

/**
 * Created by rossgrambo on 2/25/16.
 */
public class CalendarDayAdapter extends BaseAdapter {
    public static int ROW_NUMBER = 6;

    private Context context;
    private final DateTime[] dates;
    private List<Event> events;
    GridView myGridView;

    public CalendarDayAdapter(GridView gridview, Context context, DateTime[] dates, List<Event> events) {
        this.context = context;
        this.myGridView = gridview;
        this.dates = dates;
        this.events = events;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_day, null);
        }

        // get layout from mobile.xml
        convertView = inflater.inflate(R.layout.calendar_day, null);

        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                myGridView.getHeight()/ROW_NUMBER - 2);
        convertView.setLayoutParams(param);

        // set value into textview
        TextView textView = (TextView) convertView
                .findViewById(R.id.calendarDayNumber);

        DateTime test = dates[position];
        textView.setText(test.dayOfMonth().getAsText());

        List<Event> events = checkDayForEvents(position);

        for (Event event: events) {
            EventButton button = new EventButton(context, event);

            ((LinearLayout)convertView).addView(button);
        }


        // Set BackgroundColor appropriately
        if (dates[position].getMonthOfYear() != DateTime.now().getMonthOfYear()) {
            ((LinearLayout)convertView.findViewById(R.id.calendarDay))
                    .setBackgroundColor(context.getResources().getColor(R.color.calendarDayOtherBackground));
        }

        return convertView;
    }

    private List<Event> checkDayForEvents(int i) {
        DateTime testDate = dates[i];

        Log.e("TAG", testDate.toString());

        List<Event> results = new ArrayList();

        for (Event event : events) {
            if (event.getStartTime().getYear() == testDate.getYear() &&
                event.getStartTime().getMonthOfYear() == testDate.getMonthOfYear() &&
                event.getStartTime().getDayOfMonth() == testDate.getDayOfMonth()) {
                results.add(event);
            }
        }

        return results;
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
