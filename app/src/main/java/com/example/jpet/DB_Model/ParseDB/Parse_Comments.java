package com.example.jpet.DB_Model.ParseDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.jpet.ApplicationContextProvider;
import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.Home.CommentClass;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Liran on 01-Jul-15.
 */
public class Parse_Comments {

    //COMMENT department
    public int getNumOfComments(String postID) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("PostID", postID);

        try {

            List<ParseObject> result = query.find();

            return result.size();
        } catch (ParseException e) {
            Log.e("number of likes: ", " request failed.");
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addCommentNotification(NotificationsOfPost notification) {
        ParseObject po = new ParseObject("LastEvents");

        po.put("L_C_F", notification.getRecentAction());
        po.put("Reded", false);
        po.put("userNameGetLike", notification.getUserName_getNotification());
        po.put("userNameSetLike", notification.getUserName_doAction());
        po.put("postIdLiked", notification.getPostIdLiked());
        po.put("Date", new CurrentDateTime().getDateTime());


        Bitmap profilePicture = notification.getProfilePicture();
        if (profilePicture == null) {
            profilePicture = MainActivity.getBitmapFromSource(R.drawable.no_profile_picture);
        }

        //uploading profile pic
        byte[] userPictureData = encodeToBase64(profilePicture).getBytes();
        ParseFile userPictureFile = new ParseFile("pic.txt", userPictureData);
        po.put("likerPic", userPictureFile);


        Bitmap postPicture = notification.getPostPicture();
        if (postPicture == null) {
            postPicture = MainActivity.getBitmapFromSource(R.drawable.no_image_available);
        }

        //uploading post pic
        byte[] postPictureData = encodeToBase64(postPicture).getBytes();
        ParseFile postPictureFile = new ParseFile("pic.txt", postPictureData);
        po.put("picThatLiked", postPictureFile);

        try {
            po.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Bitmap getCommentPictureAndChange(AtomicReference<CommentClass> currComment) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserName", currComment.get().get_userName());
        try {
            ParseObject commentObject = query.getFirst();
            ParseFile userProfilePic = (ParseFile) commentObject.get("picture");

            if (userProfilePic == null) return MainActivity.getBitmapFromSource(R.drawable.no_profile_picture);

            String data = new String(userProfilePic.getData());
            Bitmap commentProfilePicture = decodeBase64(data);

            currComment.get().set_profilePicture(commentProfilePicture);

            Log.e("comment profilePicture:", "Succeed!");
            return commentProfilePicture;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<CommentClass> getCommentsByPost(String postId) {

        ArrayList<CommentClass> commentsArray = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("PostID", postId);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject commentObject : result) {

//                ParseFile userProfilePic = (ParseFile) commentObject.get("ProfilePicture");
//                String data = new String(userProfilePic.getData());
//                Bitmap commentProfilePicture = decodeBase64(data);

                commentsArray.add(new CommentClass(
//                        commentProfilePicture,
                        null,
                        commentObject.getString("UserName"),
                        commentObject.getString("Date"),
                        commentObject.getString("CommentContent"),
                        commentObject.getString("postID"),
                        commentObject.getObjectId()
                ));
            }
            Log.e("get comments:", "Succeed!");
            return commentsArray;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Boolean addCommentToPost(CommentClass newComment) {

        ParseObject newCommentObject = new ParseObject("Comments");

        newCommentObject.put("PostID", newComment.get_postId());
        newCommentObject.put("UserName", newComment.get_userName());
        newCommentObject.put("CommentContent", newComment.get_comment());
        newCommentObject.put("Date", newComment.get_date());

        Bitmap userProfilePicture = Parse_model.getInstance().getUserClass().get_userPic();

        if (userProfilePicture != null) {
            String profilePictureString = encodeToBase64(userProfilePicture);
            byte[] data = profilePictureString.getBytes();

            ParseFile file = new ParseFile("pic.txt", data);
            newCommentObject.put("ProfilePicture", file);
        }
        try {
            newCommentObject.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;

    }

    //************************************************
    //encoding decoding functions for transferring pictures to parse
    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        if (input != null) {
            Log.d("decode pic:", "is NOT null, which means it got the pics");

            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } else {
            // if(dialogDelegate!=null){
            //    dialogDelegate.alert();
            //  }
            return null;

        }

    }

    //************************************************

}
