package de.niceguys.studisapp.Model;

public class Post
{
    private String postText;
    private String postid;
    private String publisher;

    public Post (String postid, String postText, String publisher)
    {
        this.postid = postid;
        this.postText = postText;
        this.publisher = publisher;
    }
    public Post ()
    {

    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postID) {
        this.postid = postID;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
