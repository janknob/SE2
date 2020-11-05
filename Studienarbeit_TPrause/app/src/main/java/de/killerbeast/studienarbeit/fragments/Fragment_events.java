package de.killerbeast.studienarbeit.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import java.util.Map;
import de.killerbeast.studienarbeit.Event;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.Parser;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;
import de.killerbeast.studienarbeit.interfaces.Interface_Parser;

public class Fragment_events extends Fragment implements Interface_Parser {

    private Interface_Fragmenthandler parent;
    private LinearLayout ll_eventContainer;
    private AlertDialog alertDialog;

    public Fragment_events() {
        // Required empty public constructor
    }

    public static Fragment_events newInstance(Interface_Fragmenthandler parent) {
        Fragment_events fragment = new Fragment_events();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setParent(parent);
        return fragment;
    }

    private void setParent(Interface_Fragmenthandler parent) { this.parent = parent; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_eventContainer = view.findViewById(R.id.ll_events_container);
        SharedPreferences sp_events = Manager.getInstance().getSharedPreferences("events");
        if (sp_events.getAll().size() == 0) downloadEvents();
        else showEvents();

    }

    @SuppressLint("InflateParams")
    private void downloadEvents() {

        Context cw = new ContextThemeWrapper(Manager.getInstance().getContext(), R.style.dialogMenu);
        AlertDialog.Builder downloading = new AlertDialog.Builder(cw);
        LayoutInflater inflater = getLayoutInflater();
        downloading.setView(inflater.inflate(R.layout.dialog_downloading, null));
        alertDialog = downloading.create();
        alertDialog.show();

        Parser parser = new Parser(this);
        parser.parse("events");

    }

    public void parsed(Map<String, String> values, String mode) {

        if (mode.equals("events")) {

            SharedPreferences.Editor editor = Manager.getInstance().getSharedPreferences("events").edit();

            for (String s : values.keySet()) editor.putString(s, values.get(s));

            editor.apply();
            alertDialog.dismiss();

            parent.updateFragment(Manager.STRING_FRAGMENT_EVENTS);

        }

    }

    @SuppressLint("SetTextI18n")
    private void showEvents() {

        SharedPreferences sp_events = Manager.getInstance().getSharedPreferences("events");

        Map<String, ?> news = sp_events.getAll();

        for (String s : news.keySet()) {

            String value = (String) news.get(s);

            CardView cv = new CardView(Manager.getInstance().getContext());
            cv.setBackgroundColor(Color.TRANSPARENT);

            CardView.inflate(Manager.getInstance().getContext(), R.layout.cardview_events, cv);
            cv.setCardElevation(20);
            cv.setRadius(20);
            assert value != null;
            Event event = new Event(value);

            ((TextView)cv.findViewById(R.id.tv_eventName)).setText(event.getName());
            ((TextView)cv.findViewById(R.id.tv_event_date)).setText(event.getDate());

            String timeString = event.getTime_start() + " - " + event.getTime_end();
            // event.getTime_start() + " - " + event.getTime_end()

            // 17:15, 08:00 - 22:00, 18:00

            if (timeString.contains(", ")) {

                String[] temp1 = timeString.split(" - ");

                String[] temp11 = temp1[0].split(", ");
                String[] temp12 = temp1[1].split(", ");

                timeString = String.format("%s - %s,\n%s%s", temp11[0], temp12[0], temp11[1], (temp12[1] == null) ? "" : " - " + temp12[1]);

            }

            ((TextView)cv.findViewById(R.id.tv_event_time)).setText(timeString); //TODO unterschiedliche Tage-> mehrere Zeilen
            ((TextView)cv.findViewById(R.id.tv_event_place)).setText(event.getPlace());
            ((TextView)cv.findViewById(R.id.tv_event_room)).setText(event.getRoom().isEmpty() ? "/" : event.getRoom());

            cv.setOnClickListener((v)-> {

                //TODO

            });

            ll_eventContainer.addView(cv);

            Space sp = new Space(requireContext());
            sp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40));

            ll_eventContainer.addView(sp);


        }

    }

}