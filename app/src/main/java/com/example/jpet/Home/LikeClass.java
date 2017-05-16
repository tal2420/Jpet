package com.example.jpet.Home;

/**
 * Created by Liran on 05-Jun-15.
 */
public class LikeClass {

    String postID;
    String likerUserName;
    String postOwnerUserName;

    public LikeClass(String postID, String likerUserName, String postOwnerUserName) {
        this.postID = postID;
        this.likerUserName = likerUserName;
        this.postOwnerUserName = postOwnerUserName;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getLikerUserName() {
        return likerUserName;
    }

    public void setLikerUserName(String likerUserName) {
        this.likerUserName = likerUserName;
    }

    public String getPostOwnerUserName() {
        return postOwnerUserName;
    }

    public void setPostOwnerUserName(String postOwnerUserName) {
        this.postOwnerUserName = postOwnerUserName;
    }
}
