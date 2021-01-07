package de.niceguys.studisapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

import de.niceguys.studisapp.MainActivity;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View view;

    // creates a new Instance of the fragment when the fragment is called
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

        Manager.log("Show the layout", this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Manager.log("I am hearing...", this);

        // list selection for language
        ListPreference listPreference = findPreference("language");
        listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals("german"))
            {
                selectLanguage("de");
                update();
                Toast.makeText(getActivity(), getResources().getString(R.string.germanLang), Toast.LENGTH_SHORT).show();
            }
            if (newValue.equals("english"))
            {
                selectLanguage("en");
                update();
                Toast.makeText(getActivity(), getResources().getString(R.string.englishLang), Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        // switch for darkmode
        SwitchPreferenceCompat darkmodeSwitch = findPreference("darkmode");
        darkmodeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {

            // dark theme
            if ((boolean) newValue) {

                Manager.getInstance().getData("settings").edit().putString("appTheme", "Dunkel").commit();
                Toast.makeText(getActivity(), getResources().getString(R.string.themeDark), Toast.LENGTH_SHORT).show();

            }
            // light theme
            else {

                Manager.getInstance().getData("settings").edit().putString("appTheme", "Hell").commit();
                Toast.makeText(getActivity(), getResources().getString(R.string.themeBright), Toast.LENGTH_SHORT).show();

            }
            new Thread(this::restart).start();
            return true;

        });
    }

    // restarts the main activity to apply the setting changes
    private void restart() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(requireContext(), MainActivity.class);
        // intent.putExtras get here
        startActivity(intent);
        requireActivity().finish();

    }

    // gives the main activity the selected language
    private void selectLanguage (String language)
    {
        Manager.log("First: " + language);
        Manager.getInstance().getData("settings").edit().putString("language", language).commit();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        getActivity().getResources().getConfiguration().locale= locale;
        requireContext().getResources().updateConfiguration(getActivity().getResources().getConfiguration(), getResources().getDisplayMetrics());

        //TODO Add great userexperience -> get back here after restart

        Intent intent = new Intent(requireContext(), MainActivity.class);
        // intent.putExtras get here
        startActivity(intent);
        requireActivity().finish();

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





















