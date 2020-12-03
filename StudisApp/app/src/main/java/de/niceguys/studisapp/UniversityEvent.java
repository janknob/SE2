package de.niceguys.studisapp;

public class UniversityEvent {

    private final String name;
    private final String date;
    private final String time_start;
    private final String time_end;
    private String description;
    private final String place;

    public String getRoom() {
        return room;
    }

    private String room;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getDescription() {
        return description;
    }

    public String getPlace() {
        return place;
    }

    public UniversityEvent(String values) {

        String[] info = values.split("\\|");
        date = info[0];
        time_start = info[1];
        time_end = info[2];
        place = info[3];
        room = info[4];
        name = info[5];
        try {
            description = info[6];
        } catch (Exception ignored) { }

    }

}
