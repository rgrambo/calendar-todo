package edu.uw.rgrambo.calendarto_do;

import org.joda.time.DateTime;

/**
 * Created by rossgrambo on 3/3/16.
 */
public class Event {
    private DateTime myDateTime;
    private String Title;

    public Event(DateTime dateTime, String title) {
        this.myDateTime = dateTime;
        Title = title;
    }

    public DateTime getDate() {
        return myDateTime;
    }

    public String getTitle() {
        return Title;
    }
}
