package de.niceguys.studisapp.Model;

public class User {

    // initialize
    private String id;
    private String username;
    private String imgUrl;

    // constructor
    public User (String id, String username, String imgUrl)
    {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;

    }

    private User() {
    }

    // getter and setter
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
