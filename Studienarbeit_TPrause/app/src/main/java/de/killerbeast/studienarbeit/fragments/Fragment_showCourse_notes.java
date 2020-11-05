package de.killerbeast.studienarbeit.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import de.killerbeast.studienarbeit.activities.Activity_showNote;
import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;

public class Fragment_showCourse_notes extends Fragment {

    private Interface_Fragmenthandler parent;
    private Course course;
    private View view;
    private Manager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse_notes, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    static Fragment_showCourse_notes newInstance(Interface_Fragmenthandler parent, Course course) {

        Fragment_showCourse_notes fragment = new Fragment_showCourse_notes();
        fragment.setParent(parent);
        Bundle args = new Bundle();
        args.putString("course", course.saveCourse());
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
        this.view = view;
        if (requireArguments().containsKey("course")) course = new Course(Objects.requireNonNull(requireArguments().getString("course")));

        String temp = course.getCourseName();
        if (temp.contains("-")) temp = temp.substring(0, temp.indexOf("-"));
        if (temp.contains("(")) temp = temp.substring(0, temp.indexOf("("));
        String coursename = temp.trim();

        showNotes(coursename);

    }

    private void showNotes(String coursename) {

        SharedPreferences sp_notes = Manager.getInstance().getSharedPreferences("notes");
        Set<String> notes = sp_notes.getStringSet(coursename, null);

        LinearLayout ll = view.findViewById(R.id.ll_showCourse_notes);

        if (notes != null) {
            boolean first = true;
            for (String s : notes) {

                String title = s.substring(0, s.indexOf("|"));

                TextView tv = new TextView(manager.getContext());
                tv.setTextColor(ContextCompat.getColor(manager.getContext(), R.color.normalText));
                tv.setText(title);
                tv.setTextSize(18);
                tv.setOnClickListener((v) -> startActivityForResult(new Intent().setClass(Manager.getInstance().getContext(), Activity_showNote.class).putExtra("note", s).putExtra("coursename", coursename), 6));
                tv.setOnLongClickListener((v) -> {
                    Context cw = new ContextThemeWrapper(Manager.getInstance().getContext(), R.style.dialogMenu);
                    AlertDialog.Builder warning = new AlertDialog.Builder(cw);
                    warning.setTitle(getResources().getString(R.string.warning_deleteNote));
                    warning.setPositiveButton(manager.getContext().getResources().getString(R.string.warning_deleteNote_yes), (dialog, which) -> deleteNote(coursename, s));
                    warning.setNegativeButton(manager.getContext().getResources().getString(R.string.warning_deleteNote_no), (dialog, which) -> {
                    });

                    AlertDialog warn = warning.create();
                    warn.show();
                    return true;

                });
                tv.setPadding(0, 10, 0, 10);

                if (!first) {

                    View view = new View(manager.getContext());
                    view.setBackgroundColor(ContextCompat.getColor(manager.getContext(), R.color.colorPrimary));
                    view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    ll.addView(view);

                } else first = false;

                ll.addView(tv);

            }
        }

        FloatingActionButton fab = view.findViewById(R.id.fab_showCourse_notes);
        fab.setOnClickListener((v -> {

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            startActivityForResult(new Intent().setClass(manager.getContext(), Activity_showNote.class).putExtra("note", String.format("%s|", currentDateandTime)).putExtra("coursename", coursename), 6);

        }));

    }

    private void deleteNote(String coursename, String note) {

        SharedPreferences sp_notes = Manager.getInstance().getSharedPreferences("notes");
        Set<String> notes = sp_notes.getStringSet(coursename, null);
        if (notes != null)

            notes.remove(note);

        SharedPreferences.Editor editor = sp_notes.edit();
        editor.remove(coursename);
        editor.apply();
        if (Objects.requireNonNull(notes).size() != 0) {
            editor.putStringSet(coursename, notes);
            Log.wtf("updated notes", "yeajt");
        }
        editor.apply();
        parent.updateFragment(manager.getContext().getResources().getString(R.string.notes));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        parent.updateContext();

        if (requestCode == 6) parent.updateFragment(getResources().getString(R.string.notes));

    }

}

