package de.killerbeast.studienarbeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import java.util.Map;
import java.util.Objects;

import de.killerbeast.studienarbeit.Course;
import de.killerbeast.studienarbeit.Manager;
import de.killerbeast.studienarbeit.Parser;
import de.killerbeast.studienarbeit.R;
import de.killerbeast.studienarbeit.interfaces.Interface_Fragmenthandler;
import de.killerbeast.studienarbeit.interfaces.Interface_Parser;

import static android.view.View.GONE;

public class Fragment_showCourse_prof extends Fragment implements Interface_Parser {

    private Interface_Fragmenthandler parent;
    private Course course;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_showcourse_prof, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public static Fragment_showCourse_prof newInstance(Interface_Fragmenthandler parent, Course course) {

        Fragment_showCourse_prof fragment = new Fragment_showCourse_prof();
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
        Manager.getInstance();

        if (requireArguments().containsKey("course")) course = new Course(Objects.requireNonNull(requireArguments().getString("course")));

        if (course != null) {

            Parser parser = new Parser(this);
            parser.parse("person", course.getProfessor());

        }

    }

    public void parsed(Map<String, String> values, String mode) {

        if (mode.equals("person")) {

            showPerson(values);

        }

    }

    private void showPerson(Map<String, String> values) {

        TextView tv_profname = view.findViewById(R.id.tv_showCourse_prof);
        tv_profname.setText(values.get("name"));

        TextView tv_person_kind = view.findViewById(R.id.tv_person_kind1);
        tv_person_kind.setText(values.get("kind"));

        TextView tv_person_address = view.findViewById(R.id.tv_person_adress);
        tv_person_address.setText(values.get("address"));

        TextView tv_person_room = view.findViewById(R.id.tv_person_room);
        tv_person_room.setText(values.get("room"));

        TextView tv_person_fon = view.findViewById(R.id.tv_person_fon);
        tv_person_fon.setText(String.format("%s %s", getResources().getString(R.string.person_fon), values.get("telephone")));

        TextView tv_person_fax = view.findViewById(R.id.tv_person_fax);
        tv_person_fax.setText(values.get("fax"));

        TextView tv_person_email = view.findViewById(R.id.tv_person_email);
        tv_person_email.setText(String.format("%s %s", getResources().getString(R.string.person_email), values.get("email")));

        ImageView iv = view.findViewById(R.id.iv_person);
        ImageView iv_full = view.findViewById(R.id.iv_person_full);

        Ion.with(iv).load(values.get("img"));

        Ion.with(iv_full).load(values.get("img"));

        Button btn_searchRoom = view.findViewById(R.id.btn_person_searchRoom);
        Button btn_call = view.findViewById(R.id.btn_person_call);
        Button btn_writeEmail = view.findViewById(R.id.btn_person_email);

        btn_searchRoom.setOnClickListener((v) -> {


            String room = tv_person_room.getText().toString().substring(tv_person_room.getText().toString().indexOf(":")+1);
            //TODO
            Course temp = new Course("", "", "", "", Objects.requireNonNull(values.get("name")), room, "");
            parent.showFragment("room", temp);

        });

        btn_writeEmail.setOnClickListener((v)-> {

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ values.get("email")});

            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, getResources().getString(R.string.choose_EmailClient)));

        });

        btn_call.setOnClickListener((v)-> {

            String f = values.get("telephone");

            if (Objects.requireNonNull(f).contains("(")) {

                f = f.replaceAll("\\(.\\)", "");

            }

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(String.format("tel:%s", f)));
            startActivity(intent);

        });

        iv.setOnClickListener((v)-> iv_full.setVisibility(View.VISIBLE));
        iv_full.setOnClickListener((v -> iv_full.setVisibility(GONE)));

        btn_call.setVisibility(View.VISIBLE);
        btn_writeEmail.setVisibility(View.VISIBLE);
        btn_searchRoom.setVisibility(View.VISIBLE);

    }

}
