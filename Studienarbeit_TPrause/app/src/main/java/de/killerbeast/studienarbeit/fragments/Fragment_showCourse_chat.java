package de.killerbeast.studienarbeit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

public class Fragment_showCourse_chat extends Fragment implements Interface_SocketListener {

    private Interface_Fragmenthandler parent;
    private View view;
    private String courseIdentification = "";
    private Socket socket;
    private SocketListener socketListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse_chat, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_showCourse_chat newInstance(Interface_Fragmenthandler parent, Course course) {

        Fragment_showCourse_chat fragment = new Fragment_showCourse_chat();
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
        Course course = null;
        if (requireArguments().containsKey("course")) course = new Course(Objects.requireNonNull(requireArguments().getString("course")));
        courseIdentification = Objects.requireNonNull(course).getCourseIdentification();

        try {

            socket = new Socket(Manager.STRING_SERVER_IP, Manager.INTEGER_SERVER_PORT);

            sendToServer("//enter");

            socketListener = new SocketListener(this, socket);

            new Thread(socketListener).start();


        } catch (Exception e) {

            e.printStackTrace();

        }

        Button btn = view.findViewById(R.id.btn_chat_send);
        btn.setOnClickListener((v)->{

            EditText et = view.findViewById(R.id.et_chat_send);
            String toSend = et.getText().toString();
            et.setText("");
            if (!toSend.equals("")) sendToServer(toSend);

        });

    }

    @Override
    public void received(String received) {

        LinearLayout ll = view.findViewById(R.id.ll_chat);
        ScrollView sv = view.findViewById(R.id.sv_chat);

        Log.wtf("received", received);

        TextView tv = new TextView(Manager.getInstance().getContext());
        tv.setText(received);
        tv.setTextSize(20);
        tv.setPadding(0,0,0,20);
        if (received.contains(" !SERVER_CLIENTCONNECTED!")) {
            String username = received.substring(0, received.indexOf(" !SERVER_CLIENTCONNECTED!"));
            tv.setText(String.format("%s %s", username, Manager.getInstance().getContext().getResources().getString(R.string.chat_userEntered)));
            tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.backgroundText));
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextSize(14);
        } else if (received.contains(" !SERVER_CLIENTDISSCONNECTED!")) {
            String username = received.substring(0, received.indexOf(" !SERVER_CLIENTDISSCONNECTED!"));
            tv.setText(String.format("%s %s", username, Manager.getInstance().getContext().getResources().getString(R.string.chat_userLeft)));
            tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.backgroundText));
            tv.setTextSize(14);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }else
        tv.setTextColor(ContextCompat.getColor(Manager.getInstance().getContext(), R.color.normalText));

        if (parent.getActivity() == null) Log.wtf("no activity" , ":(");

        parent.getActivity().runOnUiThread(()->ll.addView(tv));


        sv.postDelayed(()-> sv.fullScroll(ScrollView.FOCUS_DOWN), 100);



    }



    @Override
    public void warn() {

        Log.wtf("test", "hallo=");
        onDestroy();

    }

    public void sendToServer(String msg) {

        String send = String.format("%s|%s|%s\n", courseIdentification, Manager.getInstance().getSharedPreferences("settings").getString("username", ""), msg);
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(send);
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sendToServer("//exit");
        Log.wtf("bye", "bye");
        socketListener.disconnect();

    }



}
