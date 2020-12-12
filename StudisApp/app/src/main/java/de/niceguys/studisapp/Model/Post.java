package de.niceguys.studisapp.Model;

public class Post
{
    private String postText;
    private String postid;
    private String publisher;
    private String category;

    public Post (String postid, String postText, String publisher, String category)
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
