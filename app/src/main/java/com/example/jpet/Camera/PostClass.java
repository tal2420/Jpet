package com.example.jpet.Camera;

import android.graphics.Bitmap;

import com.example.jpet.Home.CommentClass;

import java.util.ArrayList;


/**
 * Created by Liran on 29-Mar-15.
 */
public class PostClass {

    String _userName;
    String _mainString;
    String hashTag;
    Bitmap _postPicture;
    Bitmap _userProfilePicture;
    String _date;
    String objectID;
    Boolean isLiked;
    Boolean isFollowed;
    int picWidth;
    int picHeight;
    int isErased;
    ArrayList<CommentClass> commentArray;
    String[] nameOfLikers;

    public PostClass(String _userName, String _mainString, Bitmap _postPicture, Bitmap _userProfilePicture, String _date, String objectID, Boolean isLiked, Boolean isFollowed, int picWidth, int picHeight, int isErased, String hashTag) {
        this._userName = _userName;
        this._mainString = _mainString;
        this._postPicture = _postPicture;
        this._userProfilePicture = _userProfilePicture;
        this._date = _date;
        this.objectID = objectID;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
        this.isErased = isErased;
        this.hashTag = hashTag;
    }

    public PostClass(String _userName, String _mainString, Bitmap _postPicture, Bitmap _userProfilePicture, String _date, String objectID, Boolean isLiked, Boolean isFollowed, int picWidth, int picHeight, int isErased) {
        this._userName = _userName;
        this._mainString = _mainString;
        this._postPicture = _postPicture;
        this._userProfilePicture = _userProfilePicture;
        this._date = _date;
        this.objectID = objectID;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
        this.isErased = isErased;
    }

//    String _comments;
    int _numOfComments;

    int _numOfLikes;
    String _nameOfUserLike;

    int numOfUserFollowers;

    boolean isSetHashTags = false;

    public PostClass() {
    }



    public String getLine() {
        return "!@#$%^&*";
    }


    public void PostClass(String _userName, String _mainString) {
        this._userName = _userName;
        this._mainString = _mainString;
        this._userProfilePicture = _userProfilePicture;
    }

    public boolean isSetHashTags() {
        return isSetHashTags;
    }

    public void setIsSetHashTags(boolean isSetHashTags) {
        this.isSetHashTags = isSetHashTags;
    }

    public String[] getNameOfLikers() {
        return nameOfLikers;
    }

    public void setNameOfLikers(String[] nameOfLikers) {
        this.nameOfLikers = nameOfLikers;
    }

    public ArrayList<CommentClass> getCommentArray() {
        return commentArray;
    }

    public void setCommentArray(ArrayList<CommentClass> commentArray) {
        this.commentArray = commentArray;
    }

    public Bitmap get_postPicture() {
        return _postPicture;
    }

    public void set_postPicture(Bitmap _postPicture) {
        this._postPicture = _postPicture;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_mainString() {
        return _mainString;
    }

    public void set_mainString(String _mainString) {
        this._mainString = _mainString;
    }

    public Bitmap get_userProfilePicture() {
        return _userProfilePicture;
    }

    public String get_hashTag() {
        return hashTag;
    }


    public void set_userProfilePicture(Bitmap _pic) {
        this._userProfilePicture = _pic;
    }

    public String get_date() {
        return _date;
    }

    public void set_hashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public void set_date(String _date) {
        this._date = _date;
    }




    public int get_numOfComments() {
        return _numOfComments;
    }

    public void set_numOfComments(int _numOfComments) {
        this._numOfComments = _numOfComments;
    }

    public int get_numOfLikes() {
        return _numOfLikes;
    }

    public void set_numOfLikes(int _numOfLikes) {
        this._numOfLikes = _numOfLikes;
    }

    public String get_nameOfUserLike() {
        return _nameOfUserLike;
    }

    public void set_nameOfUserLike(String _nameOfUserLike) {
        this._nameOfUserLike = _nameOfUserLike;
    }


    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }


    public int getNumOfUserFollowers() {
        return numOfUserFollowers;
    }

    public void setNumOfUserFollowers(int numOfUserFollowers) {
        this.numOfUserFollowers = numOfUserFollowers;
    }


    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public PostClass(String _userName, String _mainString, Bitmap _postPicture, Bitmap _userProfilePicture, String _date, String _comments, int _numOfComments, int _numOfLikes, String _nameOfUserLike, String objectID, int numOfUserFollowers, Boolean isLiked, int picWidth, int picHeight, String hashTag, int isErased) {
        this._userName = _userName;
        this._mainString = _mainString;
        this._postPicture = _postPicture;
        this._userProfilePicture = _userProfilePicture;
        this._date = _date;
        this._numOfComments = _numOfComments;
        this._numOfLikes = _numOfLikes;
        this._nameOfUserLike = _nameOfUserLike;
        this.objectID = objectID;
        this.numOfUserFollowers = numOfUserFollowers;
        this.isLiked = isLiked;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
        this.hashTag = hashTag;
        this.isErased = isErased;
    }

    public PostClass(String _userName, String _mainString, Bitmap _postPicture, Bitmap _userProfilePicture, String _date, String objectID, int picWidth, int picHeight) {
        this._userName = _userName;
        this._mainString = _mainString;
        this._postPicture = _postPicture;
        this._userProfilePicture = _userProfilePicture;
        this._date = _date;
        this.objectID = objectID;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }

    public int getIsErased() {
        return isErased;
    }

    public void setIsErased(int isErased) {
        this.isErased = isErased;
    }
}
