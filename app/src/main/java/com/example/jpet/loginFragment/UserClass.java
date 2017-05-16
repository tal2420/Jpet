package com.example.jpet.loginFragment;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Liran on 29-Mar-15.
 */
public class UserClass {

    String _userName;
    String _password;
    String _email;

    Bitmap _userPic;


    ArrayList<String> followersArray = new ArrayList<>();

    String lastOn;


    String _userId;

    public UserClass(String _userName, String _password, String _email, Bitmap _userPic, ArrayList<String> followersArray) {
        this._userName = _userName;
        this._password = _password;
        this._email = _email;
        this._userPic = _userPic;
        this.followersArray = followersArray;
//        this.lastOn = lastOn;
    }

    public UserClass() {
    }

    public UserClass(String _password, String _email, String _userId) {
        this._password = _password;
        this._email = _email;
        this._userId = _userId;
    }

    public String getLastOn() {
        return lastOn;
    }

    public void setLastOn(String lastOn) {
        this.lastOn = lastOn;
    }

    public void addFollowersArray(String followerName){
        followersArray.add(followerName);
    }

    public ArrayList<String> getFollowersArray() {
        return followersArray;
    }

    public void setFollowersArray(ArrayList<String> followersArray) {
        this.followersArray = followersArray;
    }

    public Boolean get_isOn() {
        return _isOn;
    }

    public void set_isOn(Boolean _isOn) {
        this._isOn = _isOn;
    }

    Boolean _isOn;

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_userId() {
        return _userId;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public Bitmap get_userPic() {
        return _userPic;
    }

    public void set_userPic(Bitmap _userPic) {
        this._userPic = _userPic;
    }


}
