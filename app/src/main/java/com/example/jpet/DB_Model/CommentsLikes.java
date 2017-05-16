package com.example.jpet.DB_Model;

import com.example.jpet.Home.CommentClass;
import com.example.jpet.Home.LikeClass;

import java.util.ArrayList;

/**
 * Created by Liran on 31-Aug-15.
 */
public class CommentsLikes {
    ArrayList<LikeClass> likeArray;
    ArrayList<CommentClass> commentArray;

    public CommentsLikes() {
    }

    public CommentsLikes(ArrayList<LikeClass> likeArray, ArrayList<CommentClass> commentArray) {
        this.likeArray = likeArray;
        this.commentArray = commentArray;
    }

    public ArrayList<LikeClass> getLikesArray() {
        return likeArray;
    }

    public ArrayList<CommentClass> getCommentsArray() {
        return commentArray;
    }
}
