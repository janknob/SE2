package de.niceguys.studisapp.Model;

import android.util.Log;

import de.niceguys.studisapp.Manager;

public class User {

    private String id;
    private String username;
    private String imgUrl;
    private String semesterId = "";
    private String degreeId = "";
    private String semester = "";
    private String degree = "";
    private static User instance;

    public static User getInstance() {

        if (instance == null)
            instance = new User();

        return instance;

    }
    private User() {
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        Manager.getInstance().getData("settings").edit().putBoolean("UniversityStuff_selected", true).apply();
        Log.wtf("USER-Semester", semester);
        this.semester = semester;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(String degreeId) {
        this.degreeId = degreeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
