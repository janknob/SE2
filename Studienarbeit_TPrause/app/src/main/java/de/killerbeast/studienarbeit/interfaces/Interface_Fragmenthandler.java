package de.killerbeast.studienarbeit.interfaces;

import android.app.Activity;


public interface Interface_Fragmenthandler {

    default void updateFragment(String which) {}

    default void updateContext(){}

    default void showFragment(String fragmentname, Object ...args) {}

    default Activity getActivity(){

        return new Activity();

    }

}