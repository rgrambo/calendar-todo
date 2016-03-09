package edu.uw.rgrambo.calendarto_do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by holdenstegman on 3/8/16.
 */
public class TodoDatabase {
    public TodoDatabase() {

    }

    public static abstract class TodoDB implements BaseColumns {
        public static final String TABLE_NAME = "todos";
        public static final String COL_TITLE = "todotitle";
        public static final String COL_TODOFOR = "todofor";
    }

    public static abstract class CalendarDB implements BaseColumns {
        public static final String TABLE_NAME = "calendar";
        public static final String COL_TITLE = "title";
        public static final String COL_NOTE = "note";
        public static final String COL_OWNER = "owner";
        public static final String COL_START_TIME = "start_time";
        public static final String COL_END_TIME = "end_time";
        public static final String COL_REPEAT = "repeat";
    }

    public static final String CREATE_TODOS =
            "CREATE TABLE IF NOT EXISTS " + TodoDB.TABLE_NAME + "(" +
                    TodoDB._ID + " INTEGER PRIMARY KEY" + ", "+
                    TodoDB.COL_TITLE + " TEXT" +"," +
                    TodoDB.COL_TODOFOR + " TEXT" +
                    ")";

    public static final String CREATE_CALENDAR =
            "CREATE TABLE IF NOT EXISTS " + CalendarDB.TABLE_NAME + "(" +
                    CalendarDB._ID + " INTEGER PRIMARY KEY" + ", "+
                    CalendarDB.COL_TITLE + " TEXT" +"," +
                    CalendarDB.COL_NOTE + " TEXT" + ", " +
                    CalendarDB.COL_OWNER + " TEXT" + ", " +
                    CalendarDB.COL_START_TIME + " DATETIME" + ", " +
                    CalendarDB.COL_END_TIME + " DATETIME" +", " +
                    CalendarDB.COL_REPEAT + " INTEGER" +
                    ")";

    public static final String DROP_TODOS = "DROP TABLE IF EXISTS "+ TodoDB.TABLE_NAME;
    public static final String DROP_CALENDAR = "DROP TABLE IF EXISTS " + CalendarDB.TABLE_NAME;

    public static class Helper extends SQLiteOpenHelper {

        private static Helper instance;

        public static final String DATABASE_NAME = "calendartodo.db";
        public static final int DATABASE_VERSION = 1;

        public Helper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static synchronized Helper getHelper(Context context){
            if(instance == null){
                instance = new Helper(context);
            }
            return instance;

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TODOS);
            db.execSQL(CREATE_CALENDAR);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODOS);
            db.execSQL(DROP_CALENDAR);
            onCreate(db);
        }
    }

    public static void tryCreateDB(Context context) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(CREATE_TODOS);
        db.execSQL(CREATE_CALENDAR);
    }

    //Helper method to reset my data
    public static void dropDB(Context context) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DROP_TODOS);
        db.execSQL(DROP_CALENDAR);
        db.execSQL(CREATE_TODOS);
        db.execSQL(CREATE_CALENDAR);
    }


    public static void insertTodo(Context context, TodoItem todo) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(TodoDB.COL_TITLE, todo.title);
        content.put(TodoDB.COL_TODOFOR, todo.todoFor);

        try {
            long newRowId = db.insert(TodoDB.TABLE_NAME, null, content);
        } catch (SQLiteConstraintException e){}
    }

    public static void updateTodo(Context context, TodoItem todo, int id) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(TodoDB.COL_TITLE, todo.title);
        content.put(TodoDB.COL_TODOFOR, todo.todoFor);

        try {
            int rowsAffected = db.update(TodoDB.TABLE_NAME, content, TodoDB._ID + "=" + id, null);
        } catch (SQLiteConstraintException e){}
    }

    public static void deleteTodo(Context context, int id) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            int rowsAffected = db.delete(TodoDB.TABLE_NAME, TodoDB._ID + "=" + id, null);
        } catch (SQLiteConstraintException e){}
    }

    public static Cursor queryDatabase(Context context) {

        Helper helper = Helper.getHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] cols = new String[] {
                TodoDB._ID,
                TodoDB.COL_TITLE,
                TodoDB.COL_TODOFOR
        };

        Cursor results = db.query(TodoDB.TABLE_NAME, cols, null, null, null, null, null, null);

        return results;
    }

    public static Cursor queryDatabaseForCalendar(Context context) {
        Helper helper = Helper.getHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] cols = new String[] {
                CalendarDB._ID,
                CalendarDB.COL_TITLE,
                CalendarDB.COL_NOTE,
                CalendarDB.COL_OWNER,
                CalendarDB.COL_START_TIME,
                CalendarDB.COL_END_TIME,
                CalendarDB.COL_REPEAT
        };

        Cursor results = db.query(CalendarDB.TABLE_NAME, cols, null, null, null, null, null, null);

        return results;
    }

    public static void insertCalender(Context context, Event event) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(CalendarDB.COL_TITLE, event.getTitle());
        content.put(CalendarDB.COL_NOTE, event.getNote());
        content.put(CalendarDB.COL_OWNER, event.getOwner());
        content.put(CalendarDB.COL_START_TIME, event.getStartTime().toString());
        content.put(CalendarDB.COL_END_TIME, event.getEndTime().toString());
        content.put(CalendarDB.COL_REPEAT, event.getRepeat());

        try {
            long newRowId = db.insert(CalendarDB.TABLE_NAME, null, content);
        } catch (Exception e){ }
    }

    public static void deleteCalendar(Context context, int id) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            int rowsAffected = db.delete(CalendarDB.TABLE_NAME, CalendarDB._ID + "=" + id, null);
        } catch (SQLiteConstraintException e){}
    }

    public static Cursor getCalendar(Context context, int id) {
        Helper helper = Helper.getHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] cols = new String[] {
                CalendarDB._ID,
                CalendarDB.COL_TITLE,
                CalendarDB.COL_NOTE,
                CalendarDB.COL_OWNER,
                CalendarDB.COL_START_TIME,
                CalendarDB.COL_END_TIME,
                CalendarDB.COL_REPEAT
        };

        Cursor results = db.query(CalendarDB.TABLE_NAME, cols, CalendarDB._ID+"=?", new String[]{id+""}, null,
                null, null, null);

        return results;
    }

    public static void updateCalendar(Context context, Event event, int id) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(CalendarDB.COL_TITLE, event.getTitle());
        content.put(CalendarDB.COL_NOTE, event.getNote());
        content.put(CalendarDB.COL_OWNER, event.getOwner());
        content.put(CalendarDB.COL_START_TIME, event.getStartTime().toString());
        content.put(CalendarDB.COL_END_TIME, event.getEndTime().toString());
        content.put(CalendarDB.COL_REPEAT, event.getRepeat());

        try {
            int rowsAffected = db.update(CalendarDB.TABLE_NAME, content, CalendarDB._ID + "=" + id, null);
        } catch (SQLiteConstraintException e){}
    }
}
