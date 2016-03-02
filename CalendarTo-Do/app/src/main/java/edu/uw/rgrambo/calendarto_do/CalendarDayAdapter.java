package edu.uw.rgrambo.calendarto_do;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rossgrambo on 2/25/16.
 */
public class CalendarDayAdapter extends BaseAdapter {
    private Context context;
    private final DateTime[] dates;

    public CalendarDayAdapter(Context context, DateTime[] dates) {
        this.context = context;
        this.dates = dates;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.calendar_day, null);

            TextView test = (Button) gridView
                    .findViewById(R.id.calendarDayButton);

            test.setText("Example Event");

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.calendarDayNumber);

            textView.setText(dates[position].dayOfMonth().getAsText());

            // Set BackgroundColor appropriately
            if (dates[position].getMonthOfYear() != DateTime.now().getMonthOfYear()) {
                ((LinearLayout)gridView.findViewById(R.id.calendarDay))
                        .setBackgroundColor(context.getResources().getColor(R.color.calendarDayOtherBackground));
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
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
