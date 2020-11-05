package de.killerbeast.studienarbeit.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.SocketListener;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;
import de.killerbeast.studienarbeit.interfaces.Interface_SocketListener;

public class Fragment_showCourse_overview extends Fragment implements Interface_SocketListener {

    private Interface_Fragmenthandler parent;
    private Course course;
    private String username;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse_overview, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    static Fragment_showCourse_overview newInstance(Interface_Fragmenthandler parent, Course course) {

        Fragment_showCourse_overview fragment = new Fragment_showCourse_overview();
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
        
        this.view = view;

        if (requireArguments().containsKey("course")) course = new Course(Objects.requireNonNull(requireArguments().getString("course")));

        TextView tv_time = view.findViewById(R.id.tv_showCourse_time);
        TextView tv_day = view.findViewById(R.id.tv_showCourse_day);
        TextView tv_prof = view.findViewById(R.id.tv_showCourse_prof);
        TextView tv_room = view.findViewById(R.id.tv_showCourse_room);
        TextView tv_type = view.findViewById(R.id.tv_showCourse_kind);

        tv_time.setText(course.getTimeFormatted());
        tv_day.setText(course.getDay());
        tv_prof.setText(course.getProfessor());
        tv_room.setText(course.getRoomNumber());
        tv_type.setText(course.getKind());

        ImageButton btn_prof = view.findViewById(R.id.btn_showCourse_prof);
        ImageButton btn_room = view.findViewById(R.id.btn_showCourse_room);
        Button btn_chat = view.findViewById(R.id.btn_showCourse_chat);
        btn_chat.setOnClickListener((v)-> hasUsername());
        btn_prof.setOnClickListener((v -> parent.showFragment("prof")));
        btn_room.setOnClickListener((v -> parent.showFragment("room", course)));

    }

    private void hasUsername(){

        SharedPreferences sp_settings = Manager.getInstance().getSharedPreferences("settings");

        if (sp_settings.getString("username", "").equals("")) {

            AlertDialog.Builder warn = new AlertDialog.Builder(Manager.getInstance().getContext());

            warn.setTitle(getResources().getString(R.string.warning_no_username));
            warn.setMessage(getResources().getString(R.string.warning_nousername_message));

            LinearLayout ll = new LinearLayout(getContext());
            EditText et = new EditText(getContext());
            et.setHint(getString(R.string.username));

            ll.addView(et);
            ll.setGravity(Gravity.CENTER);

            warn.setView(ll);

            warn.setPositiveButton(getString(R.string.save), (dialog, which) -> {

                if (et.getText().toString().equals("")) {

                    Snackbar.make(view, getResources().getString(R.string.warning_no_username), BaseTransientBottomBar.LENGTH_LONG).show();

                } else {

                    username = et.getText().toString();
                    try {
                        Socket s = new Socket(Manager.STRING_SERVER_IP, Manager.INTEGER_SERVER_PORT);

                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        bw.write(String.format("checkIfAvaiable: %s\n", username));
                        bw.flush();

                        SocketListener sl = new SocketListener(this, s);
                        Thread t = new Thread(sl);
                        t.setDaemon(false);
                        t.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });

            warn.setNegativeButton(getString(R.string.dontUse), null);

            AlertDialog warning = warn.create();
            warning.show();

        } else parent.showFragment("chat");
        
    }
    

    @Override
    public void received(String received) {


        if (received.equals("Username avaiable")) {

            Manager.getInstance().getSharedPreferences("settings").edit().putString("username", username).apply();
            parent.showFragment("chat");
            
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(Manager.getInstance().getContext());
            builder.setTitle(getResources().getString(R.string.username_notAvaiable));
            builder.setMessage(getResources().getString(R.string.username_notAvaiable_message));
            builder.setPositiveButton(getResources().getString(R.string.username_ok), null);
            builder.setOnDismissListener(dialog -> hasUsername());
            parent.getActivity().runOnUiThread(()-> {
                AlertDialog dialog = builder.create();
                dialog.show();

            });

        }

    }
}
