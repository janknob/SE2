package de.niceguys.studisapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import de.niceguys.studisapp.HtmlParser;
import de.niceguys.studisapp.Interfaces.Interface_Parser;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;

public class University_MealsFragment extends Fragment implements Interface_Parser {

    private AlertDialog alertDialog;

    public static University_MealsFragment newInstance() {

        University_MealsFragment fragment = new University_MealsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universitymeals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        downloadMeals();

    }

    @SuppressLint("InflateParams")
    private void downloadMeals() {

        Context cw = new ContextThemeWrapper(Manager.getInstance().getContext(), R.style.dialogMenu);
        AlertDialog.Builder downloading = new AlertDialog.Builder(cw);
        LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        Date date = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        String firstDayOfWeek = f.format(date);
        HtmlParser parser = new HtmlParser(this);
        parser.parse(Manager.Parser.meals, firstDayOfWeek);

    }

    public void parsed(Map<String, ?> values, String mode) {

        Map<String, Map<String, String>> value = (Map<String, Map<String, String>>) values;

        if (mode.equals("meals")) {

            showMeals(value);

        }

    }

    public void showMeals(Map<String, Map<String, String>> values) {

        //runs up to 7 times

        System.out.println("juhuu");


    }

}
