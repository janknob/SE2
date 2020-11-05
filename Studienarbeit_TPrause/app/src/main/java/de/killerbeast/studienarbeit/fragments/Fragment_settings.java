package de.killerbeast.studienarbeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import de.killerbeast.studienarbeit.activities.Activity_SetupAssistant;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;

public class Fragment_settings extends Fragment {

    private Manager manager;
    private Interface_Fragmenthandler parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_settings newInstance(Interface_Fragmenthandler parent) {

        Fragment_settings fragment = new Fragment_settings();
        fragment.setParent(parent);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    private void setParent(Interface_Fragmenthandler parent) {

        this.parent = parent;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);

        manager = Manager.getInstance();

        TextView tv_degree = view.findViewById(R.id.tv_main_settings_degreename);
        TextView tv_semester = view.findViewById(R.id.tv_main_settings_semester);

        tv_degree.setText(manager.getSharedPreferences("settings"). getString("degree", ""));
        tv_semester.setText(manager.getSharedPreferences("settings"). getString("semester", ""));

        Button btn = view.findViewById(R.id.btn_main_settings_rerunSetupAssistant);
        btn.setOnClickListener((v -> {

            manager.getSharedPreferences("courses").edit().clear().apply();

            String temp = Manager.getInstance().getSharedPreferences("settings").getString("username", "");

            manager.getSharedPreferences("settings").edit().clear().apply();
            manager.getSharedPreferences("settings").edit().putString("username", temp).apply();

            startActivity(new Intent().setClass(manager.getContext(), Activity_SetupAssistant.class));
            parent.getActivity().finish();

        }));

        CheckBox cb_animations = view.findViewById(R.id.cb_main_settings_allowAnimations);
        cb_animations.setChecked(manager.getSharedPreferences("settings").getBoolean("animations", true));
        cb_animations.setOnCheckedChangeListener((v, c) -> manager.getSharedPreferences("settings").edit().putBoolean("animations", c).apply());

        Spinner sp_apptheme = view.findViewById(R.id.sp_main_settings_apptheme);
        String[] settings = Manager.getInstance().getContext().getResources().getStringArray(R.array.appthemeSettings);
        sp_apptheme.setAdapter(new ArrayAdapter<>(Manager.getInstance().getContext(), R.layout.spinner_items, settings));


        String selectedSettings = manager.getSharedPreferences("settings").getString("appTheme", "System");
        final int[] selection = new int[1];
        if (selectedSettings.equals("System")) {sp_apptheme.setSelection(2); selection[0] = 2;}
        if (selectedSettings.equals("Hell")) {sp_apptheme.setSelection(1); selection[0] = 1;}
        else {sp_apptheme.setSelection(0); selection[0] = 0;}


        sp_apptheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (view != null && selection[0] != position) {

                    selection[0] = position;
                    String set = ((AppCompatTextView) view).getText().toString();

                    Manager.getInstance().getSharedPreferences("settings").edit().putString("appTheme", set).apply();
                    switch (Manager.getInstance().getSharedPreferences("settings").getString("appTheme", "System")) {

                        case "Hell":
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case "Dunkel":
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        default:
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.w("nthin", "selected");

            }
        });



    }

}
