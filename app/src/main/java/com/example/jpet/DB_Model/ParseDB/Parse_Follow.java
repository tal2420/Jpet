package com.example.jpet.DB_Model.ParseDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.jpet.profile.FollowersClass;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liran on 01-Jul-15.
 */
public class Parse_Follow {
    public String getUserNameByPostId(String postId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("objectId", postId);

        try {

            ParseObject postObject = query.getFirst();
            String userName = postObject.getString("UserName");

            return userName;


        } catch (ParseException e) {
            Log.e("number of likes: ", " request failed.");
            e.printStackTrace();
        }
        return null;
    }

    public void removeFollowerFromUser(String currentUserName) {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("FollowUserName", currentUserName);

        try {
            ParseObject postObject = query.getFirst();
            postObject.delete();


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public int getNumFollowersByUserName(String _userName) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("UserName", _userName);
        try {
            ParseObject parseObject = query.getFirst();
//            Log.e("number of followers", " request success.");
            int numOfFollowers = parseObject.getInt("numFollowers");
            return numOfFollowers;
        } catch (ParseException e) {
            Log.e("number of followers", " request failed.");
            e.printStackTrace();
        }
        return 0;
    }

    public int getNumOfFollowing(String userIFollowName) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("FollowUserName", userIFollowName);

        try {

            List<ParseObject> result = query.find();

            return result.size();
        } catch (ParseException e) {
            Log.e("number of likes: ", " request failed.");
            e.printStackTrace();
        }
        return 0;
    }

    public int getNumOfFollowers(String userFollowMeName) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("UserIFollowName", userFollowMeName);

        try {

            List<ParseObject> result = query.find();
            return result.size();
        } catch (ParseException e) {
            Log.e("number of likes: ", " request failed.");
            e.printStackTrace();
        }
        return 0;
    }

    public boolean addFollowerToUser(String currentUserName, final String _currUserId, final String userIFollowName, Bitmap userPic) {


        ParseObject po = new ParseObject("Followers");

        po.put("FollowUserName", currentUserName);
        po.put("FollowUserId", _currUserId);
        po.put("UserIFollowName", userIFollowName);


        //uploading profile pic
//        byte[] userPictureData = encodeToBase64(userPic).getBytes();
//        ParseFile userPictureFile = new ParseFile("pic.txt", userPictureData);
//        po.put("FollowerPic", userPictureFile);


        try {
            po.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isFollowedUserBefore(String currentUserName, String userIFollowName) {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("FollowUserName", currentUserName);
        query.whereEqualTo("UserIFollowName", userIFollowName);


        Boolean isFollowedBefore = false;
        try {
            ParseObject postObject = query.getFirst();
            if (postObject == null) {
                isFollowedBefore = false;
            } else {
                isFollowedBefore = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isFollowedBefore;
    }

    public ArrayList<FollowersClass> getFollowersByUserName(String userName) {

        ArrayList<FollowersClass> followersArray = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("UserIFollowName", userName);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject followerObject : result) {

                Bitmap commentProfilePicture = null;
//                ParseFile userProfilePic = (ParseFile) followerObject.get("FollowerPic");
//                if(userProfilePic!=null){
//                    String data = new String(userProfilePic.getData());
//                    commentProfilePicture = decodeBase64(data);
//                }

                followersArray.add(new FollowersClass(
                        followerObject.getString("FollowUserName"),
                        null//commentProfilePicture
                        )
                );
            }
            Log.e("get comments:", "Succeed!");
            return followersArray;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<String> getFollowersByUserNameToString(String userName) {

        ArrayList<String> followersArray = new ArrayList<>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("FollowUserName", userName);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject followerObject : result) {

                String followerName = followerObject.getString("UserIFollowName");
                followersArray.add(followerName);


            }
            return followersArray;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<FollowersClass> getFollowingByUserName(String userName) {

        ArrayList<FollowersClass> followersArray = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("FollowUserName", userName);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject followerObject : result) {

                Bitmap commentProfilePicture = null;

//                ParseFile userProfilePic = (ParseFile) followerObject.get("FollowerPic");
//                if(userProfilePic!=null){
//                    String data = new String(userProfilePic.getData());
//                    commentProfilePicture = decodeBase64(data);
//                }

                if(commentProfilePicture==null){
                    followersArray.add(new FollowersClass(
                            followerObject.getString("UserIFollowName"),
                            null));
                }else{
                    followersArray.add(new FollowersClass(
                            followerObject.getString("UserIFollowName"),
                            null//commentProfilePicture
                            )
                    );
                }

            }

            return followersArray;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void removeFollowFromUser(String userIFollowName, String currentUserName) {

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        query.whereEqualTo("UserIFollowName", userIFollowName);
        query.whereEqualTo("FollowUserName", currentUserName);

        try {
            ParseObject postObject = query.getFirst();
            postObject.delete();


        } catch (ParseException e) {
            Log.e("isLikedPostBefore: ", "object request failed.");
            e.printStackTrace();
        }

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
