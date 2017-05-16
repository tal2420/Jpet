package com.example.jpet.DB_Model.ParseDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liran on 01-Jul-15.
 */
public class Parse_Notification {

    NotificationsOfPost notification;

    public boolean addFollowNotification(NotificationsOfPost notification) {
        ParseObject po = new ParseObject("LastEvents");

        po.put("L_C_F", notification.getRecentAction());
        po.put("Reded", false);
        po.put("userNameGetLike", notification.getUserName_getNotification());
        po.put("userNameSetLike", notification.getUserName_doAction());
        po.put("Date", new CurrentDateTime().getDateTime());


        //uploading profile pic

//        byte[] userPictureData = encodeToBase64(notification.getProfilePicture()).getBytes();
//        ParseFile userPictureFile = new ParseFile("pic.txt", userPictureData);
//        po.put("likerPic", userPictureFile);

        try {
            po.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addLikeNotification(NotificationsOfPost notification) {
        ParseObject po = new ParseObject("LastEvents");

        po.put("L_C_F", notification.getRecentAction());
        po.put("Reded", false);
        po.put("userNameGetLike", notification.getUserName_getNotification());
        po.put("userNameSetLike", notification.getUserName_doAction());
        po.put("postIdLiked", notification.getPostIdLiked());
        po.put("Date", new CurrentDateTime().getDateTime());


        //uploading profile pic
        if (notification.getProfilePicture() != null) {
            byte[] userPictureData = encodeToBase64(notification.getProfilePicture()).getBytes();
            ParseFile userPictureFile = new ParseFile("pic.txt", userPictureData);
            po.put("likerPic", userPictureFile);
        }

        //uploading post pic
        byte[] postPictureData = encodeToBase64(notification.getPostPicture()).getBytes();
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

    public void updateNotifications(String postId) {
        ParseQuery<ParseObject> query = new ParseQuery("LastEvents");
        query.whereEqualTo("objectId", postId);
        try {
            ParseObject postObject = query.getFirst();
            postObject.put("Reded", true);
            postObject.saveInBackground();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NotificationsOfPost> getAllNotification(String userName) {


        ArrayList<NotificationsOfPost> notificationArrayList = new ArrayList<NotificationsOfPost>();

        ParseQuery<ParseObject> query = new ParseQuery("LastEvents");
        query = query.whereEqualTo("userNameGetLike", userName);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {

                String userNameGetLike = po.getString("userNameGetLike");
                String userNameSetLike = po.getString("userNameSetLike");
                String postID = po.getString("postIdLiked");
                String description = po.getString("L_C_F");
                Boolean isRead = po.getBoolean("Reded");
                String date = po.getString("Date");
                String notificationId = po.getObjectId();


                //getting the profile picture
//                ParseFile profilePic = (ParseFile) po.get("likerPic");
//                String profilePicData = new String(profilePic.getData());
//                Bitmap profileImage = decodeBase64(profilePicData);
                Bitmap profileImage = Parse_model.getInstance().getProfilePictureByUserName(userNameGetLike);


                if (!description.equals("F")) {
                    //getting the post picture
                    ParseFile postPic = (ParseFile) po.get("picThatLiked");
                    String postPicData = new String(postPic.getData());
                    Bitmap postImage = decodeBase64(postPicData);

                    notification = new NotificationsOfPost(
                            userNameGetLike,
                            userNameSetLike,
                            description,
                            postID,
                            isRead,
                            postImage,
                            profileImage,
                            date,
                            notificationId);

                    notificationArrayList.add(notification);

                } else {
                    notification = new NotificationsOfPost(
                            userNameGetLike,
                            userNameSetLike,
                            description,
                            postID,
                            isRead,
                            null,
                            profileImage,
                            date,
                            notificationId);

                    notificationArrayList.add(notification);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return notificationArrayList;
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
