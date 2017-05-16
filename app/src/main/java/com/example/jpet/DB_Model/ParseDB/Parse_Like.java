package com.example.jpet.DB_Model.ParseDB;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Liran on 01-Jul-15.
 */
public class Parse_Like {
    public void removeLikeToPost(String objectID, final String likerName, String likerId) {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
        query.whereEqualTo("PostId", objectID);
        query.whereEqualTo("Liker", likerName);

        // Boolean isLikedBefore = false;
        try {
            ParseObject postObject = query.getFirst();
            postObject.delete();


        } catch (ParseException e) {
            Log.e("isLikedPostBefore: ", "object request failed.");
            e.printStackTrace();
        }

    }

    public Boolean isLikedPostBefore(String _postID, String _currentUserName) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
        query.whereEqualTo("PostId", _postID);
        query.whereEqualTo("Liker", _currentUserName);


        Boolean isLikedBefore = false;
        try {
            List<ParseObject> result = query.find();
            if (result.size() != 0) {
                isLikedBefore = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isLikedBefore;
    }

    public int getNumOfLikes(String postID) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Likes");
        query.whereEqualTo("PostId", postID);

        try {

            List<ParseObject> result = query.find();

            return result.size();
        } catch (ParseException e) {
            Log.e("number of likes: ", " request failed.");
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addLikeToPost(String objectPostId, final String likerName, String postOwner, String currentUserId) {

        ParseObject po = new ParseObject("Likes");

        po.put("PostId", objectPostId);
        po.put("PostOwner", postOwner);
        po.put("Liker", likerName);
        po.put("LikerId", currentUserId);


        try {
            po.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
