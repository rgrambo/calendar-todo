package edu.uw.rgrambo.calendarto_do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by holdenstegman on 3/8/16.
 */
public class TodoDatabase {
    public TodoDatabase() {

    }

    public static abstract class TodoDB implements BaseColumns {
        public static final String TABLE_NAME = "todos";
        public static final String COL_TITLE = "quantity";
        public static final String COL_TODOFOR = "todofor";
    }

    public static final String CREATE_TODOS =
            "CREATE TABLE IF NOT EXISTS " + TodoDB.TABLE_NAME + "(" +
                    TodoDB._ID + " INTEGER PRIMARY KEY" + ", "+
                    TodoDB.COL_TITLE + " TEXT" +"," +
                    TodoDB.COL_TODOFOR + " TEXT" +
                    ")";

    public static final String DROP_TODOS = "DROP TABLE IF EXISTS "+ TodoDB.TABLE_NAME;


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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODOS);
            onCreate(db);
        }
    }

    public static void tryCreateDB(Context context) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(CREATE_TODOS);
    }

    //Helper method to reset my data
    public static void dropDB(Context context) {
        Helper helper = Helper.getHelper(context);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(DROP_TODOS);
        db.execSQL(CREATE_TODOS);
    }

//    public static int getSums(Context context) {
//        Helper helper = Helper.getHelper(context);
//
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        String[] cols = new String[] {
//                ObservationDB._ID,
//                ObservationDB.COL_QUANTITY,
//                ObservationDB.COL_DESC,
//                ObservationDB.COL_DATE
//        };
//
//        Cursor results = db.query(ObservationDB.TABLE_NAME, cols, null, null, null, null, null, null);
//
//        int count = 0;
//        results.moveToFirst();
//        for (int i = 0; i < results.getCount(); i++) {
//            count += results.getInt(results.getColumnIndex(ObservationDB.COL_QUANTITY));
//            results.moveToNext();
//        }
//        return count;
//    }


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


}
