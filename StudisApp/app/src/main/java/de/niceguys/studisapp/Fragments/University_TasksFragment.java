package de.niceguys.studisapp.Fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import de.niceguys.studisapp.Model.Manager;
import de.niceguys.studisapp.R;
import de.niceguys.studisapp.Model.UniversityTask;
public class University_TasksFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universitytasks, container, false);
    }

    public static University_TasksFragment newInstance() {

        University_TasksFragment fragment = new University_TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);
        this.view = view;

        showTasks();

    }

    private void showTasks(){

        SharedPreferences sp_tasks = Manager.getInstance().getData("tasks");

        ArrayList<UniversityTask> tasks = new ArrayList<>();
        LinearLayout unfinished = view.findViewById(R.id.ll_university_tasks_unfinished);
        unfinished.removeAllViews();
        LinearLayout finished = view.findViewById(R.id.ll_university_tasks_finished);
        finished.removeAllViews();
        FloatingActionButton fab_main_tasks_addTask = view.findViewById(R.id.floatingActionButton);
        fab_main_tasks_addTask.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(getResources().getString(R.string.createUniversityTask));
            EditText et = new EditText(requireContext());
            et.setHint(getResources().getString(R.string.desription));
            builder.setView(et);

            builder.setPositiveButton(getResources().getString(R.string.save), (dialogInterface, i) -> {

                SharedPreferences.Editor editor = sp_tasks.edit();
                if (et.getText() != null)
                editor.putString(sp_tasks.getAll().size() + "", et.getText().toString() + "|false");
                editor.apply();

                showTasks();

            });

            builder.setNegativeButton(getResources().getString(R.string.warning_cancle_add_task), null);

            requireActivity().runOnUiThread(() -> builder.create().show());

        });
        tasks.clear();

        while (!sp_tasks.getString("" + tasks.size(), "").equals("")) {

            UniversityTask task = new UniversityTask(sp_tasks.getString("" + tasks.size(), ""));
            tasks.add(task);

        }

        for (UniversityTask t : tasks) {

            CardView cv = new CardView(requireContext());

            CardView.inflate(requireContext(), R.layout.cardview_tasks, cv);

            CardView root = cv.findViewById(R.id.cv_university_task);

            TextView tv = cv.findViewById(R.id.tv_university_task);
            tv.setText(t.getTitle());

            cv.setRadius(root.getRadius());

            tv.setTextSize(20);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            if (t.isFinished()) tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            CheckBox cb = cv.findViewById(R.id.cb_university_task);

            cb.setChecked(t.isFinished());

            cb.setOnCheckedChangeListener((compoundButton, b) -> {

                Map<String, ?> map = sp_tasks.getAll();

                String id = "";

                for (String o : map.keySet()) {

                    String s = (String) map.get(o);

                    if (Objects.equals(s, t.getTitle() + "|" + t.isFinished())) {
                        id = o;
                    }

                }

                t.setFinished(!t.isFinished());

                SharedPreferences.Editor editor = sp_tasks.edit();
                editor.putString(id, t.getTitle() + "|" + t.isFinished());
                editor.apply();

                showTasks();

            });

            if (t.isFinished()) {

                finished.addView(cv);
                Space sp = new Space(Manager.getInstance().getContext());
                sp.setMinimumHeight(25);
                finished.addView(sp);

            } else {

                unfinished.addView(cv);
                Space sp = new Space(Manager.getInstance().getContext());
                sp.setMinimumHeight(25);
                unfinished.addView(sp);

            }

        }

    }

}
