package de.niceguys.studisapp.Model;

public class CurrentUser {

    // initialize Attributes
    private String id;
    private String username;
    private String semesterId = "";
    private String degreeId = "";
    private String semester = "";
    private String degree = "";
    private static CurrentUser instance;

    // constructor
    public static CurrentUser getInstance() {

        if (instance == null)
            instance = new CurrentUser();

        return instance;

    }
    private CurrentUser() {
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {

        if (!semester.equals(""))
            Manager.getInstance().getData("settings").edit().putBoolean("UniversityStuff_selected", true).apply();
        Manager.log("Usersemester: "+ semester, this);
        this.semester = semester;

    }

    public static void clear() {

        instance = null;

    }


    // getter and setter
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
}
