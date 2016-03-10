package edu.uw.rgrambo.calendarto_do;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.rgrambo.calendarto_do.R;

/**
 * Created by holdenstegman on 3/9/16.
 */
public class Preference extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public Preference() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

//        EditTextPreference editTextPreference = (EditTextPreference) findPreference("test");
//        PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("category");
//        preferenceCategory.removePreference(editTextPreference);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getView().setBackgroundColor(Color.parseColor("#FFFFFF"));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase("edittext_addToGroup")) {
            String newPerson = sharedPreferences.getString("edittext_addToGroup", null);
            ((EditTextPreference)getPreferenceManager().findPreference("edittext_addToGroup")).setText("");
            EditTextPreference storedPeople = (EditTextPreference) findPreference("test");
            String oldValues = storedPeople.getText();
            Gson gson = new Gson();
            ArrayList<String> values = gson.fromJson(oldValues, ArrayList.class);
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(newPerson);
            String updatedValues = gson.toJson(values);
            storedPeople.setText(updatedValues);




            MultiSelectListPreference listPreference = (MultiSelectListPreference) getPreferenceManager().findPreference("listpreference_todoGroup");
            CharSequence[] entries = listPreference.getEntries();
            CharSequence[] entryValues = listPreference.getEntryValues();
            CharSequence[] newEntries = Arrays.copyOf(entries, entries.length + 1);
            CharSequence[] newEntryValues = Arrays.copyOf(entryValues, entryValues.length + 1);
            newEntries[newEntries.length - 1] = newPerson;
            newEntryValues[newEntryValues.length - 1] = "";
            listPreference.setEntries(newEntries);
            listPreference.setEntryValues(newEntryValues);
        }
    }
}
