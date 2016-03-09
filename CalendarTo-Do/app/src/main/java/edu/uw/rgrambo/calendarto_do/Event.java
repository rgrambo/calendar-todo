package edu.uw.rgrambo.calendarto_do;

import org.joda.time.DateTime;

/**
 * Created by rossgrambo on 3/3/16.
 */
public class Event {
    private int Id; // -1 represents no current id
    private DateTime StartTime;
    private DateTime EndTime;
    private String Title;
    private String Note;
    private String Owner;
    private int Repeat;

    public Event(String title, String note, String owner, DateTime start, DateTime end, int repeat) {
        this (-1, title, note, owner, start, end, repeat);
    }

    public Event(int id, String title, String note, String owner, DateTime start, DateTime end, int repeat) {
        Id = id;
        StartTime = start;
        EndTime = end;
        Title = title;
        Note = note;
        Owner = owner;
        Repeat = repeat;
    }

    public DateTime getStartTime() { return StartTime; }
    public DateTime getEndTime() { return EndTime; }

    public int getId() {
        return Id;
    }
    public String getTitle() {
        return Title;
    }
    public String getNote() {
        return Note;
    }
    public String getOwner() {
        return Owner;
    }
    public int getRepeat() { return Repeat; }
}
