package de.killerbeast.studienarbeit.fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.Parser;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Parser;

public class Fragment_showCourse_modulbook extends Fragment implements Interface_Parser {

    private Course course;
    private View view;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse_modulbook, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    static Fragment_showCourse_modulbook newInstance(Course course) {

        Fragment_showCourse_modulbook fragment = new Fragment_showCourse_modulbook();
        Bundle args = new Bundle();
        args.putString("course", course.saveCourse());
        fragment.setArguments(args);
        return fragment;

    }

    @SuppressLint("InflateParams")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);

        this.view = view;

        if (requireArguments().containsKey("course")) course = new Course(Objects.requireNonNull(requireArguments().getString("course")));
        Context cw = new ContextThemeWrapper(view.getContext(), R.style.dialogMenu);
        AlertDialog.Builder downloading = new AlertDialog.Builder(cw);
        LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        alertDialog = downloading.create();
        alertDialog.show();

        Parser parser = new Parser(this);
        parser.parse("modulbook", course.getCourseName());

    }

    public void parsed(Map<String, String> values, String mode) {

        if (mode.equals("modulbook")) showModulbook(values);

    }

    private void showModulbook(Map<String, String> values) {

        Spinner sp = view.findViewById(R.id.sp_showCourse_modulbook);
        ScrollView sv = view.findViewById(R.id.sv_showCourse_modulbook);
        LinearLayout ll = view.findViewById(R.id.ll_showCourse_modulbook);

        ArrayList<String> header = new ArrayList<>(values.keySet());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Manager.getInstance().getContext(), R.layout.spinner_items, header);

        sp.setAdapter(arrayAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedHeader = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < ll.getChildCount(); i++) {

                    if (ll.getChildAt(i) instanceof TextView) {

                        TextView tv = (TextView) ll.getChildAt(i);
                        if (tv.getText().equals(selectedHeader)) {

                            if (Manager.getInstance().getSharedPreferences("settings").getBoolean("animations", true)) {

                                ObjectAnimator animator = ObjectAnimator.ofInt(sv, "scrollY", tv.getTop());

                                animator.setDuration(500);
                                animator.start();
                                animator.addUpdateListener(animation -> {

                                    int val = (int) animation.getAnimatedValue();
                                    requireActivity().runOnUiThread(() -> sv.setScrollX(val));

                                });

                            } else
                                sv.scrollTo(0, tv.getTop());

                        }
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (String key : header) {

            TextView tHeader = new TextView(Manager.getInstance().getContext());
            tHeader.setText(key);
            tHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tHeader.setTextSize(20);
            tHeader.setPadding(0,0,0,10);
            tHeader.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.normalText));
            TextView tDetails = new TextView(Manager.getInstance().getContext());
            tDetails.setText(values.get(key));
            tDetails.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tDetails.setTextSize(16);
            tDetails.setPadding(0,0,0,20);
            tDetails.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext().getApplicationContext(), R.color.normalText));

            ll.addView(tHeader);
            ll.addView(tDetails);

            View v = new View(Manager.getInstance().getContext());
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            v.setBackgroundColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.pageTitle));

            ll.addView(v);
            Space space = new Space(Manager.getInstance().getContext());
            space.setMinimumHeight(20);
            ll.addView(space);

        }

        alertDialog.dismiss();

    }

}
