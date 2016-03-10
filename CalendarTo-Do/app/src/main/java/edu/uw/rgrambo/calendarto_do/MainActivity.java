package edu.uw.rgrambo.calendarto_do;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.Date;

import layout.AddEventDialogFragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.preferences:
                FragmentManager fragmentManager = getFragmentManager();
                edu.uw.rgrambo.calendarto_do.Preference prefFragment = new edu.uw.rgrambo.calendarto_do.Preference();
                fragmentManager.beginTransaction()
                        .replace(R.id.rightContainer, prefFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCalendarDayInteraction(Date date) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
