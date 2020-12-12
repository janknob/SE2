package de.niceguys.studisapp.Model;

import android.util.Log;

import de.niceguys.studisapp.Manager;

public class User {

    private String id;
    private String username;
    private String imgUrl;

    public User (String id, String username, String imgUrl)
    {
        this.id = id;
        this.username = username;
        this.imgUrl = imgUrl;

    }



    private User() {
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
