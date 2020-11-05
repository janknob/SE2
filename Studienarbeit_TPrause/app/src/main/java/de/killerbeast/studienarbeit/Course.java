package de.killerbeast.studienarbeit;

public class Course {

    private final String day;
    private final String startTime;
    private final String endTime;
    private final String courseName;
    private final String professor;
    private final String roomNumber;
    private final String kind;
    private boolean shown = true;

    private final String notAvaiable = "n/a";

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getKind() {
        return kind;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public Course(String day, String startTime, String endTime, String courseName, String professor, String roomNumber, String kind) {

        String notgiven = "nicht bekannt wann";
        this.day = (day.equals("")) ? notgiven : day;
        this.startTime = (startTime.equals("")) ? notAvaiable : startTime;
        this.endTime = (endTime.equals("")) ? notAvaiable : endTime;
        this.courseName = (courseName.equals("")) ? notAvaiable : courseName;
        this.professor = (professor.equals("")) ? notAvaiable : professor;
        this.roomNumber = (roomNumber.equals("")) ? notAvaiable : roomNumber;
        this.kind = (kind.equals("")) ? notAvaiable : kind;

    }

    public Course(String compressed){

        String[] info = compressed.split("\\|");
        day = info[0];
        startTime = info[1];
        endTime = info[2];
        courseName = info[3];
        professor = info[4];
        roomNumber = info[5];
        kind = info[6];
        shown = info[7].equals("true");

    }

    public String saveCourse(){

        return this.getDay() + "|" + this.getStartTime() + "|" + this.getEndTime() + "|" + this.getCourseName() + "|" + this.getProfessor() + "|" + this.getRoomNumber() + "|" + this.getKind() + "|" + this.isShown();

    }

    public String getTimeFormatted() {

        if (startTime.equals(notAvaiable)) return notAvaiable;
        else return String.format("%s - %s", getStartTime(), getEndTime());

    }

    public String getCourseIdentification(){

        String name = courseName;

        if (name.contains("\n")) name = name.replace("\n", "");

        return String.format("%s|%s|%s", name,day,endTime);

    }

}
