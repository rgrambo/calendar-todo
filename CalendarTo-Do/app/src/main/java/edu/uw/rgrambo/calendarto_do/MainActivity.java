package edu.uw.rgrambo.calendarto_do;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import layout.CalendarFragment;
import layout.ToDoFragment;

public class MainActivity extends AppCompatActivity implements CalendarFragment.OnCalendarDayInteractionListener, ToDoFragment.onToDoFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.leftContainer, new CalendarFragment())
                    .add(R.id.rightContainer, new ToDoFragment())
                    .commit();
        }
    }

    public void onCalendarDayInteraction(Date date) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
