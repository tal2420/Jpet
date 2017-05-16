package com.example.jpet.LikeAndFollowing;

import android.graphics.Bitmap;

import com.example.jpet.LoadImageByReference.PostProfilePicture;

/**
 * Created by eliyadaida on 4/30/15.
 */
public class NotificationsOfPost extends PostProfilePicture

{

    String userName_getNotification;
    String _date;
//    String userName_doAction;
    String recentAction;
    String postIdLiked;


//    String notificationId;

    Boolean isRead;

    @Override
    public void setObjectID(String objectID) {
        super.setObjectID(objectID);
    }

    @Override
    public String getObjectID() {
        return super.getObjectID();
    }

    @Override
    public Bitmap getProfilePicture() {
        return super.getProfilePicture();
    }

    @Override
    public Bitmap getPostPicture() {
        return super.getPostPicture();
    }

    @Override
    public void setProfilePicture(Bitmap profilePicture) {
        super.setProfilePicture(profilePicture);
    }

    @Override
    public void setPostPicture(Bitmap postPicture) {
        super.setPostPicture(postPicture);
    }

    @Override
    public void setUserName(String userName) {
        super.setUserName(userName);
    }

    @Override
    public String getUserName() {
        return super.getUserName();
    }

    public NotificationsOfPost(String userName_getLike, String userName_doAction, String recentAction, String postIdLiked, Boolean isRead, Bitmap _postPicture, Bitmap _userProfilePicture, String Date, String notificationId) {
        this.userName_getNotification = userName_getLike;
        setUserName(userName_doAction);
        this.recentAction = recentAction;
        this.postIdLiked = postIdLiked;
        this.isRead = isRead;
        setPostPicture(_postPicture);
        setProfilePicture(_userProfilePicture);
        this._date = Date;
        setObjectID(notificationId);
    }


    public NotificationsOfPost() {
    }

    ;

    //************GETTERS******************
    public String getPostIdLiked() {
        return postIdLiked;
    }

    public String getUserName_getNotification() {
        return userName_getNotification;
    }

    public String get_date() {
        return _date;
    }


    public String getUserName_doAction() {
        return getUserName();
    }

    public String getRecentAction() {
        return recentAction;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public String getNotificationId() {
        return getObjectID();
    }



    //**************SETTERS******************
    public void setPostIdLiked(String postId) {
        postIdLiked = postId;
    }

    public void setUserName_doAction(String userName_doAction) {
        setUserName(userName_doAction);
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setRecentAction(String recentAction) {
        this.recentAction = recentAction;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void setUserName_getNotification(String userName_getNotification) {
        this.userName_getNotification = userName_getNotification;
    }





}
