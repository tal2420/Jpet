package com.example.jpet.profile;

import android.graphics.Bitmap;
import android.widget.Button;

/**
 * Created by eliyadaida on 5/12/15.
 */
public class FollowersClass {

    String userNameThatFollowMe;
    Button isFollowBefore;
    Bitmap userThatFollowMePic;


    public FollowersClass(String userNameThatFollowMe, Bitmap userThatFollowMePic) {
        this.userNameThatFollowMe = userNameThatFollowMe;
        this.isFollowBefore = isFollowBefore;
        this.userThatFollowMePic = userThatFollowMePic;
    }

    public Bitmap getUserThatFollowMePic() {
        return userThatFollowMePic;
    }

    public void setUserThatFollowMePic(Bitmap userThatFollowMePic) {
        this.userThatFollowMePic = userThatFollowMePic;
    }

    public Button getIsFollowBefore() {
        return isFollowBefore;
    }

    public void setIsFollowBefore(Button isFollowBefore) {
        this.isFollowBefore = isFollowBefore;
    }

    public String getUserNameThatFollowMe() {
        return userNameThatFollowMe;
    }

    public void setUserNameThatFollowMe(String userNameThatFollowMe) {
        this.userNameThatFollowMe = userNameThatFollowMe;
    }

}
