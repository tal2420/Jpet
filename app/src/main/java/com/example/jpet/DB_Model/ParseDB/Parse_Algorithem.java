package com.example.jpet.DB_Model.ParseDB;

import com.example.jpet.DB_Model.CommentsLikes;
import com.example.jpet.Home.CommentClass;
import com.example.jpet.Home.LikeClass;
import com.example.jpet.Search.Algorithem.DataSet;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liran on 01-Jul-15.
 */
public class Parse_Algorithem {

    public ArrayList<DataSet> getPostsClusters() {
        ArrayList<DataSet> postsArrayList = new ArrayList<DataSet>();

        ParseQuery<ParseObject> query = new ParseQuery("PostsClusters");

        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {

                DataSet currDataSet = new DataSet(
                        po.getString("PostID"),
                        po.getInt("Cluster"),
                        po.getInt("NumOfClusters"),
                        null
                );

                postsArrayList.add(currDataSet);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postsArrayList;

    }

    public CommentsLikes getAllCommentsLikesByUser() {

        ArrayList<CommentClass> commentsArray = new ArrayList<>();
        ArrayList<LikeClass> likesArray = new ArrayList<>();

        ParseQuery<ParseObject> likesQuery = new ParseQuery("Likes");
        ParseQuery<ParseObject> commentsQuery = new ParseQuery("Comments");
        try {
            List<ParseObject> likesResults = likesQuery.find();
            for (ParseObject likeObject : likesResults) {
                LikeClass currLike = new LikeClass(
                        likeObject.getString("PostId"),
                        likeObject.getString("Liker"),
                        likeObject.getString("PostOwner")
                );
                likesArray.add(currLike);
            }

            List<ParseObject> commentsResults = commentsQuery.find();
            for (ParseObject commentObject : commentsResults) {
                CommentClass currComment = new CommentClass(
                        commentObject.getString("PostID"),
                        commentObject.getString("UserName"),
                        commentObject.getString("CommentClass")
                );
                commentsArray.add(currComment);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new CommentsLikes(likesArray, commentsArray);
    }



}
