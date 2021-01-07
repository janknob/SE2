package de.niceguys.studisapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.util.Map;
import de.niceguys.studisapp.HtmlParser;
import de.niceguys.studisapp.Interfaces.Interface_Parser;
import de.niceguys.studisapp.Manager;
import de.niceguys.studisapp.R;
import de.niceguys.studisapp.UniversityEvent;

public class University_EventsFragment extends Fragment implements Interface_Parser {

    private LinearLayout ll_eventContainer;
    private AlertDialog alertDialog;

    public static University_EventsFragment newInstance() {

        University_EventsFragment fragment = new University_EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_universityevents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_eventContainer = view.findViewById(R.id.ll_events_container);
        SharedPreferences sp_events = Manager.getInstance().getData("events");
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

        HtmlParser parser = new HtmlParser(this);
        parser.parse(Manager.Parser.event);

    }


    public void parsed(Map<String, ?> values, String mode) {

        Map<String, String> value = (Map<String, String>) values;

        if (mode.equals("events")) {

            SharedPreferences.Editor editor = Manager.getInstance().getData("events").edit();

            for (String s : value.keySet()) editor.putString(s, value.get(s));

            editor.apply();
            alertDialog.dismiss();

            showEvents();

        }

    }

    @SuppressLint("SetTextI18n")
    private void showEvents() {

        SharedPreferences sp_events = Manager.getInstance().getData("events");

        Map<String, ?> news = sp_events.getAll();

        for (String s : news.keySet()) {

            String value = (String) news.get(s);

            CardView cv = new CardView(Manager.getInstance().getContext());
            cv.setBackgroundColor(Color.TRANSPARENT);

            CardView.inflate(Manager.getInstance().getContext(), R.layout.cardview_universityevents, cv);
            cv.setCardElevation(20);
            cv.setRadius(20);
            assert value != null;
            UniversityEvent event = new UniversityEvent(value);

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

            ((TextView)cv.findViewById(R.id.tv_universityevent_time)).setText(timeString); //TODO unterschiedliche Tage-> mehrere Zeilen
            ((TextView)cv.findViewById(R.id.tv_universityevent_place)).setText(event.getPlace());
            ((TextView)cv.findViewById(R.id.tv_universityevent_room)).setText(event.getRoom().isEmpty() ? "/" : event.getRoom());

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
