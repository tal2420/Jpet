package com.example.jpet.LoadImageByReference;

import android.graphics.Bitmap;

/**
 * Created by Liran on 19-Jul-15.
 */
public class PostProfilePicture {

    String objectID;

    Bitmap profilePicture;
    Bitmap postPicture;

    String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Bitmap getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(Bitmap postPicture) {
        this.postPicture = postPicture;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
