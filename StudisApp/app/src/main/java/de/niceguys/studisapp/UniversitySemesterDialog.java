package de.niceguys.studisapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UniversitySemesterDialog extends AppCompatDialogFragment
{
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.warning)).setMessage(getResources().getString(R.string.selectCourseOfStudyFirst)).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BottomNavigationView bnv = requireActivity().findViewById(R.id.bottom_navigation);
                bnv.setSelectedItemId(R.id.nav_profile);

            }
        });
        return builder.create();
    }
}
