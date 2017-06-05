package com.example.jpet.DB_Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.ParseDB.Parse_Algorithem;
import com.example.jpet.DB_Model.ParseDB.Parse_Comments;
import com.example.jpet.DB_Model.ParseDB.Parse_Follow;
import com.example.jpet.DB_Model.ParseDB.Parse_HashTag;
import com.example.jpet.DB_Model.ParseDB.Parse_Like;
import com.example.jpet.DB_Model.ParseDB.Parse_Notification;
import com.example.jpet.DB_Model.ParseDB.Parse_Posts;
import com.example.jpet.DB_Model.ParseDB.Parse_User;
import com.example.jpet.Home.CommentClass;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.example.jpet.LoadImageByReference.PostProfilePicture;
import com.example.jpet.Search.Algorithem.DataSet;
import com.example.jpet.loginFragment.UserClass;
import com.example.jpet.profile.FollowersClass;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Liran on 25-Mar-15.
 */
public class Parse_model {


    Parse_Comments parse_comments;
    Parse_Follow parse_follow;
    Parse_Like parse_like;
    Parse_Notification parse_notification;
    Parse_Posts parse_posts;
    Parse_HashTag parse_hashTag;
    Parse_User parse_user;
    Parse_Algorithem parse_algorithem;


    Parse_model() {
        parse_comments = new Parse_Comments();
        parse_follow = new Parse_Follow();
        parse_like = new Parse_Like();
        parse_notification = new Parse_Notification();
        parse_posts = new Parse_Posts();
        parse_hashTag = new Parse_HashTag();
        parse_user = new Parse_User();
        parse_algorithem = new Parse_Algorithem();
    }

    UserClass userClass = new UserClass();
    PostClass currPost;


    NotificationsOfPost notification;

    static final Parse_model instance = new Parse_model();


    public static Parse_model getInstance() {
        return instance;
    }

    public void setSignUp(String _userName, String _password, String _email) {
        userClass.set_userName(_userName);
        userClass.set_password(_password);
        userClass.set_email(_email);
    }


    //******PARSE FUNCTIONS*******

    //******************************************************************************************
    //************************************************
    //COMMENT department
    public int getNumOfComments(String postID) {
        return parse_comments.getNumOfComments(postID);
    }

    public boolean addCommentNotification(NotificationsOfPost notification) {
        return parse_comments.addCommentNotification(notification);
    }

    public Bitmap getCommentPictureAndChange(AtomicReference<CommentClass> currComment) {
        return parse_comments.getCommentPictureAndChange(currComment);
    }

    public ArrayList<CommentClass> getCommentsByPost(String postId) {
        return parse_comments.getCommentsByPost(postId);
    }

    public Boolean addCommentToPost(CommentClass newComment) {
        return parse_comments.addCommentToPost(newComment);
    }

    //************************************************
    //FOLLOW USER department

    public String getUserNameByPostId(String postId) {
        return parse_follow.getUserNameByPostId(postId);
    }

    public void removeFollowerFromUser(String currentUserName) {
        parse_follow.removeFollowerFromUser(currentUserName);
    }

    public int getNumFollowersByUserName(String _userName) {
        return parse_follow.getNumFollowersByUserName(_userName);
    }

    public int getNumOfFollowing(String userIFollowName) {
        return parse_follow.getNumOfFollowing(userIFollowName);
    }

    public int getNumOfFollowers(String userFollowMeName) {
        return parse_follow.getNumOfFollowers(userFollowMeName);
    }

    public boolean addFollowerToUser(String currentUserName, final String _currUserId, final String userIFollowName, Bitmap userPic) {
        return parse_follow.addFollowerToUser(currentUserName, _currUserId, userIFollowName, userPic);
    }

    public Boolean isFollowedUserBefore(String currentUserName, String userIFollowName) {
        return parse_follow.isFollowedUserBefore(currentUserName, userIFollowName);
    }

    public ArrayList<FollowersClass> getFollowersByUserName(String userName) {
        return parse_follow.getFollowersByUserName(userName);
    }

    public ArrayList<String> getFollowersByUserNameToString(String userName) {
        return parse_follow.getFollowersByUserNameToString(userName);
    }

    public ArrayList<FollowersClass> getFollowingByUserName(String userName) {
        return parse_follow.getFollowingByUserName(userName);
    }

    public void removeFollowFromUser(String userIFollowName, String currentUserName) {
        parse_follow.removeFollowFromUser(userIFollowName, currentUserName);
    }

    //************************************************
    //LIKE POST department

    public void removeLikeToPost(String objectID, final String likerName, String likerId) {
        parse_like.removeLikeToPost(objectID, likerName, likerId);
    }

    public Boolean isLikedPostBefore(String _postID, String _currentUserName) {
        return parse_like.isLikedPostBefore(_postID, _currentUserName);
    }

    public int getNumOfLikes(String postID) {
        return parse_like.getNumOfLikes(postID);
    }

    public boolean addLikeToPost(String objectPostId, final String likerName, String postOwner, String currentUserId) {
        return parse_like.addLikeToPost(objectPostId, likerName, postOwner, currentUserId);
    }

    //************************************************
    //User details department
    private static Boolean isSucceedUpdatingHolder;

    public void addProfilePicture(final Bitmap pic) {
        parse_user.addProfilePicture(pic);
    }

    public boolean addNewUser() {
        return parse_user.addNewUser();
    }

    public boolean loginWithUser() {
        return parse_user.loginWithUser();
    }

    public boolean updateUserDetails(final UserClass currUser) {
        return parse_user.updateUserDetails(currUser);
    }

    public String getUserIdByUserName(String userName){
        return parse_user.getUserIdByUserName(userName);
    }

    //************************************************
    // Notifications department
    public boolean addFollowNotification(NotificationsOfPost notification) {
        return parse_notification.addFollowNotification(notification);
    }

    public boolean addLikeNotification(NotificationsOfPost notification) {
        return parse_notification.addLikeNotification(notification);
    }

    public void updateNotifications(String postId) {
         parse_notification.updateNotifications(postId);
    }

    public ArrayList<NotificationsOfPost> getAllNotification(String userName) {
        return parse_notification.getAllNotification(userName);
    }

    //************************************************
    //Posts function

    public ArrayList<PostClass> getAllErasedPostsByUsersFollowings(ArrayList<String> userName){
        return parse_posts.getAllErasedPostsByUsersFollowings(userName);
    }

    public PostClass getPostById(String postID){
        return parse_posts.getPostById(postID);
    }

    public ArrayList<String> getMainStringFromPosts() {
    return parse_posts.getMainStringFromPosts();
    }

    public Bitmap getPictureByPostId(String postId) {
        return parse_posts.getPictureByPostId(postId);
    }

    public Bitmap getProfilePictureByPostAndChange(AtomicReference<PostClass> currPost) {
        return parse_posts.getProfilePictureByPostAndChange(currPost);
    }

    public Bitmap getPictureByPostAndChangeNOGOOD(AtomicReference<PostClass> currPost) {
        return parse_posts.getPictureByPostAndChangeNOGOOD(currPost);
    }

    public Bitmap getPictureByPostAndChange(AtomicReference<PostProfilePicture> currPost) {
        return parse_posts.getPictureByPostAndChange(currPost);
    }

    public Bitmap getProfilePictureAndChange(AtomicReference<PostProfilePicture> currPost) {
        return parse_posts.getProfilePictureAndChange(currPost);
    }

    public boolean removePostById(String postId) {
        return parse_posts.removePostById(postId);
    }

    public ArrayList<PostClass> getAllPosts() {
        return parse_posts.getAllPosts();
    }

    public ArrayList<PostClass> getPostsById(ArrayList<String> objectsIdArray) {
        return parse_posts.getPostsById(objectsIdArray);
    }

    public boolean addPost(PostClass post) {
        return parse_posts.addPost(post);
    }

    public ArrayList<PostClass> getAllUserPosts(String userName) {
        return parse_posts.getAllUserPosts(userName);
    }

    public ArrayList<PostClass> getAllUsersPostsByFollowings(ArrayList<String> userName) {
        return parse_posts.getAllUsersPostsByFollowings(userName);
    }

    public ArrayList<String> getAllFollowingPostsID(ArrayList<String> followingArrayList) {
        return parse_posts.getAllFollowingPostsID(followingArrayList);
    }

    public int getNumOfPostsByUserName(String userName) {
        return parse_posts.getNumOfPostsByUserName(userName);
    }

    public Bitmap getProfilePictureByUserName(String userName ){
        return parse_posts.getProfilePictureByUserName(userName);
    }


    //*********************************************************************************
    //Algorithem functions
    public ArrayList<DataSet> getPostsClusters() {
        return parse_algorithem.getPostsClusters();
    }

//    public CommentsLikes getAllCommentsLikesByUser() {
//        return parse_algorithem.getAllCommentsLikesByUser();
//    }

    public CommentsLikes getAllCommentsLikesByUser(){
        return parse_algorithem.getAllCommentsLikesByUser();
    }




    //******************************************************************************************
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
    //getters setters crap
    public UserClass getUserClass() {
        return userClass;
    }

    public ArrayList<PostClass> getAllPostsByHashTag(String hashTag) {

        ArrayList<PostClass> postsArray = new ArrayList<PostClass>();
        ArrayList hashTagArray = new ArrayList<>();


        ParseQuery<ParseObject> query = new ParseQuery("Posts");
//        query.whereEqualTo("objectId",postId);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {
                String hash = po.getString("HashTags");
                String id = po.getObjectId();
                if (hash != null && (!hash.equals(""))) {
                    String[] parts = hash.split("#");
                    for (String word : parts) {
                        if (hashTag.equals("#" + word + " "))
                            hashTagArray.add(id);

                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < hashTagArray.size(); i++) {
            query.whereEqualTo("objectId", hashTagArray.get(i));
            try {
                List<ParseObject> result2 = query.find();
                for (ParseObject po : result2) {
                    String postUserName = po.getString("UserName");
                    String currUserName = getUserClass().get_userName();
                    String postID = po.getObjectId();

                    currPost = new PostClass(
                            postUserName,
                            po.getString("MainString"),
                            //getPictureInPost(po),
//                        getProfilePictureInPost(po),
                            null,
                            null,
                            po.getString("date"),
                            po.getString("Comments"),
                            po.getInt("numComments"),
                            po.getInt("numLikes"),
                            po.getString("nameLikes"),
                            postID,
                            getNumFollowersByUserName(po.getString("UserName")),
                            isLikedPostBefore(postID, currUserName),
                            po.getInt("picWidth"),
                            po.getInt("picHeight"),
                            po.getString("HashTags"),
                            po.getInt("isErased")
                    );
                    postsArray.add(currPost);


                }
            } catch (ParseException e) {
                e.printStackTrace();

            }

        }

        return postsArray;

    }
    //******************************************************************************************

    public boolean addNewFacebookUser() {
        return parse_user.addNewFacebookUser();
    }
}
