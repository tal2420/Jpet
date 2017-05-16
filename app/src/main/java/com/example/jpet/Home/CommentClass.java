package com.example.jpet.Home;

import android.graphics.Bitmap;

/**
 * Created by Liran on 29-Mar-15.
 */
public class CommentClass {
    public Bitmap get_profilePicture() {
        return _profilePicture;
    }

    public void set_profilePicture(Bitmap _profilePicture) {
        this._profilePicture = _profilePicture;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public String get_comment() {
        return _comment;
    }

    public void set_comment(String _comment) {
        this._comment = _comment;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_postId() {
        return _postId;
    }

    public void set_postId(String _postId) {
        this._postId = _postId;
    }

    public String get_commentID() {
        return _commentID;
    }

    public void set_commentID(String _commentID) {
        this._commentID = _commentID;
    }

    Bitmap _profilePicture;
    String _userName;
    String _date;
    String _comment;
    String _postId;
    String _commentID;

    public CommentClass(String _postId, String _userName, String _comment) {
        this._postId = _postId;
        this._userName = _userName;
        this._comment = _comment;
    }

    public CommentClass(Bitmap _profilePicture, String _userName, String _date, String _comment, String _postId, String _commentID) {
        this._profilePicture = _profilePicture;
        this._userName = _userName;
        this._date = _date;
        this._comment = _comment;
        this._postId = _postId;
        this._commentID = _commentID;
    }

    public CommentClass(Bitmap _profilePicture, String _userName, String _date, String _comment, String _postId) {
        this._profilePicture = _profilePicture;
        this._userName = _userName;
        this._date = _date;
        this._comment = _comment;
        this._postId = _postId;
    }

    public CommentClass() {
    }
}
