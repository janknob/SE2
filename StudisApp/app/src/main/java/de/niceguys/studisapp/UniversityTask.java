package de.niceguys.studisapp;

public class UniversityTask {

    // initialize
    private final String title;
    private boolean isFinished;

    // constructor
    public UniversityTask(String title){

        this.title = title.substring(0,title.indexOf("|"));
        this.isFinished = Boolean.parseBoolean(title.substring(title.indexOf("|")+1));

    }

    // getter and setter
    public String getTitle() {

        return title;

    }

    public boolean isFinished() {

        return isFinished;

    }

    public void setFinished(boolean state) {

        isFinished = state;

    }
}

