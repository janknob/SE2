package de.niceguys.studisapp.Fragments;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import de.niceguys.studisapp.R;


import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View view;
    private Profile_SettingsFragment settingsFragment;
    private Switch aSwitch;

    public static Profile_SettingsFragment newInstance() {

        Profile_SettingsFragment fragment = new Profile_SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;



    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        ListPreference listPreference = (ListPreference) findPreference("language");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                CharSequence currText = listPreference.getEntry();
                String currValue = listPreference.getValue();
                if (currValue.equals("german"))
                {
                    selectLanguage("de");
                    update();
                }
                if (currValue.equals("english"))
                {
                    selectLanguage("en-rGB");
                    update();
                }
                return true;
            }
        });
    }

    private void selectLanguage (String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        //requireContext() = requireContext().createConfigurationContext(configuration);
        //getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        onConfigurationChanged(configuration);
        requireActivity().recreate();

    }
    private void update ()
    {
       newInstance();
    }
    @Override
    public void onConfigurationChanged (Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    }
}





















