package de.killerbeast.studienarbeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import de.killerbeast.studienarbeit.activities.Activity_addTask;
import de.killerbeast.studienarbeit.activities.Activity_showTask;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.Task;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;

public class Fragment_tasks extends Fragment {

    private Interface_Fragmenthandler parent;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_tasks newInstance(Interface_Fragmenthandler parent) {

        Fragment_tasks fragment = new Fragment_tasks();
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
        this.view = view;

        showTasks();

    }

    private void showTasks(){

        SharedPreferences sp_settings = Manager.getInstance().getSharedPreferences("settings");
        SharedPreferences sp_tasks = Manager.getInstance().getSharedPreferences("tasks");

        ArrayList<Task> tasks = new ArrayList<>();
        LinearLayout ll_main_tasks_container = view.findViewById(R.id.ll_main_tasks_container);
        ll_main_tasks_container.removeAllViews();
        FloatingActionButton fab_main_tasks_addTask = view.findViewById(R.id.floatingActionButton);
        fab_main_tasks_addTask.setOnClickListener((v)-> startActivityForResult(new Intent().setClass(Manager.getInstance().getContext(), Activity_addTask.class),7));
        tasks.clear();

        TextView tv_orderBy = view.findViewById(R.id.tv_main_tasks_orderby);
        TextView tv_invert = view.findViewById(R.id.tv_main_tasks_invert);
        tv_orderBy.setText(sp_settings.getString("compareTasksTo", getResources().getString(R.string.title)));
        tv_orderBy.setOnClickListener((v)->{

            if (tv_orderBy.getText().equals(getResources().getString(R.string.title))) {

                tv_orderBy.setText(getResources().getString(R.string.deadline));
                SharedPreferences.Editor editor = sp_settings.edit();
                editor.putString("compareTasksTo", getResources().getString(R.string.deadline));
                editor.apply();
            } else {

                tv_orderBy.setText(getResources().getString(R.string.title));
                SharedPreferences.Editor editor = sp_settings.edit();
                editor.putString("compareTasksTo", getResources().getString(R.string.title));
                editor.apply();

            }
            showTasks();

        });

        tv_invert.setOnClickListener((v -> {

            SharedPreferences.Editor editor = sp_settings.edit();
            editor.putBoolean("invertTasks", !sp_settings.getBoolean("invertTasks", false));
            editor.apply();
            showTasks();

        }));

        while (!sp_tasks.getString("" + tasks.size(), "").equals("")) {

            Task task = new Task(sp_tasks.getString("" + tasks.size(), ""), Manager.getInstance().getContext());
            tasks.add(task);

        }

        if (tasks.size() > 1) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tasks.sort(Task::compareTo);
        }

        if (sp_settings.getBoolean("invertTasks", false)) Collections.reverse(tasks);

        for (Task t : tasks) {

            TextView tv = new TextView(Manager.getInstance().getContext());
            String show = sp_settings.getString("compareTasksTo", getResources().getString(R.string.title));
            if (show.equals(getResources().getString(R.string.title))) tv.setText(t.getTitle());
            else tv.setText(t.getDeadline());

            if (!t.getDeadline().equals("NAN")) {

                String[] taskdate = t.getDeadline().split("\\.");
                System.out.println(t.getDeadline());

                Calendar calendar = Calendar.getInstance();
                int[] today = {calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR)};

                int state = 0;

                for (int i = 0; i < 3; i++)
                    if (today[i] != Integer.parseInt(taskdate[i]))
                        state = (Integer.parseInt(taskdate[i]) > today[i]) ? (-1) : (1);


                if (state == 0) tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.taskToday));
                else if (state == 1 ) tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.taskToLate));
                else tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.normalText));

            } else tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.normalText));
            tv.setTextSize(20);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            if (t.isFinished()) tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setOnClickListener((v)-> {

                Intent intent = new Intent().setClass(Manager.getInstance().getContext(), Activity_showTask.class);
                intent.putExtra("task", t.getCompressedString());
                startActivityForResult(intent, 7);

            });

            tv.setOnLongClickListener((v -> {

                int taskid = 0;
                while (!sp_tasks.getString(""+taskid, "").equals(t.getCompressedString())) taskid++;

                SharedPreferences.Editor editor = sp_tasks.edit();

                for (int i = taskid; i < tasks.size(); i++) {

                    String newstring = sp_tasks.getString(""+(i+1), "");

                    editor.putString(""+i, newstring);

                }

                editor.apply();

                tasks.remove(t);
                showTasks();
                Snackbar.make(view, String.format(Manager.getInstance().getContext().getResources().getString(R.string.taskDeleted), t.getTitle()), Snackbar.LENGTH_LONG).setActionTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.colorPrimary)).setAction(Manager.getInstance().getContext().getResources().getString(R.string.undo), v1 -> {

                    int id = 0;

                    Map<String, ?> map = sp_tasks.getAll();

                    for (String s : map.keySet()) {

                        if (Objects.equals(map.get(s), "")) break;
                        else id++;

                    }

                    SharedPreferences.Editor editor1 = sp_tasks.edit();
                    editor1.putString(id+"", t.getCompressedString());
                    editor1.apply();
                    requireActivity().runOnUiThread(this::showTasks);

                }).show();
                return true;


            }));

            LinearLayout ll_taskcontainer = new LinearLayout(Manager.getInstance().getContext());
            ll_taskcontainer.setOrientation(LinearLayout.HORIZONTAL);
            ll_taskcontainer.addView(tv);

            CheckBox cb_task = new CheckBox(Manager.getInstance().getContext());
            CompoundButtonCompat.setButtonTintList(cb_task, ColorStateList.valueOf(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.pageTitle)));

            cb_task.setChecked(t.isFinished());
            cb_task.setOnCheckedChangeListener((v, checked)->{

                int taskid = 0;
                while (!sp_tasks.getString(""+taskid, "").equals(t.getCompressedString())) taskid++;
                t.setFinished(checked);
                SharedPreferences.Editor editor = sp_tasks.edit();
                editor.putString(""+taskid, t.getCompressedString());
                editor.apply();
                if (cb_task.isChecked() && sp_settings.getBoolean("deleteFinishedTasks", false))
                    tv.performLongClick();
                showTasks();

            });

            ll_taskcontainer.addView(cb_task);

            ll_main_tasks_container.addView(ll_taskcontainer);

            Space sp = new Space(Manager.getInstance().getContext());
            sp.setMinimumHeight(20);

            ll_main_tasks_container.addView(sp);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.wtf("ActivityResult", ""+requestCode);
        if (requestCode == 7) {

            parent.updateContext();
            parent.updateFragment(getResources().getString(R.string.tasks));

        }

    }

}
