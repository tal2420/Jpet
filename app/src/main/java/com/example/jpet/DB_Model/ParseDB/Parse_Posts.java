package com.example.jpet.DB_Model.ParseDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.LoadImageByReference.PostProfilePicture;
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
public class Parse_Posts {

    PostClass currPost;

    public ArrayList<String> getMainStringFromPosts() {

        ArrayList<String> mainStringArrayList = new ArrayList<String>();


        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query.whereEqualTo("isErased", 1);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {
                String mainString = po.getString("MainString");
                String objectId = po.getObjectId();
                String[] parts = mainString.split(" ");
                for (String word : parts) {
                    mainStringArrayList.add(word);
                    mainStringArrayList.add(objectId);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mainStringArrayList;
    }

    public Bitmap getPictureByPostId(String postId) {//AtomicReference<PostClass> currPost){

        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query = query.whereEqualTo("objectId", postId);

        try {
            ParseObject currPostObject = query.getFirst();

            ParseFile userProfilePic = (ParseFile) currPostObject.get("Picture");
            String data = new String(userProfilePic.getData());
            Bitmap postImage = decodeBase64(data);
            return postImage;


        } catch (ParseException e) {

            e.printStackTrace();
        }
        return null;
    }

    public PostClass getPostById(String postID){

        PostClass post = new PostClass();

        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query.whereEqualTo("isErased", 1);
        query.whereEqualTo("objectId", postID);
        try {
            ParseObject po = query.getFirst();

            String postUserName = po.getString("UserName");
            String currUserName = Parse_model.getInstance().getUserClass().get_userName();

            post = new PostClass(
                    postUserName,
                    po.getString("MainString"),
                    //getPictureInPost(po),
//                        getProfilePictureInPost(po),
                    null,
                    null,
                    po.getString("date"),
                    po.getString("Comments"),
                    getNumOfComments(postID),
                    getNumOfLikes(postID),
                    po.getString("nameLikes"),
                    postID,
                    getNumFollowersByUserName(postUserName),
                    isLikedPostBefore(postID, currUserName),
                    po.getInt("picWidth"),
                    po.getInt("picHeight"),
                    po.getString("HashTags"),
                    po.getInt("isErased")
            );


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return post;
    }

    public Bitmap getProfilePictureByPostAndChange(AtomicReference<PostClass> currPost) {
        final String TAG = "getProfilePictureByPost(): ";

        ParseQuery<ParseObject> query = new ParseQuery("Users");
        query = query.whereEqualTo("UserName", currPost.get().get_userName());

        try {
            ParseFile parsePictureFile;
            ParseObject currPostObject = query.getFirst();


            parsePictureFile = (ParseFile) currPostObject.get("picture");

            Bitmap downloadedImage = null;
            if (parsePictureFile != null) {
                String data = new String(parsePictureFile.getData());
                downloadedImage = decodeBase64(data);
            }

            currPost.get().set_userProfilePicture(downloadedImage);


            return downloadedImage;

        } catch (ParseException e) {
            Log.e(TAG, " request failed.");
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getPictureByPostAndChangeNOGOOD(AtomicReference<PostClass> currPost) {
        final String TAG = "getPictureByPost(): ";

        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query = query.whereEqualTo("objectId", currPost.get().getObjectID());

        try {
            ParseFile parsePictureFile;
            ParseObject currPostObject = query.getFirst();

            parsePictureFile = (ParseFile) currPostObject.get("Picture");

            String data = new String(parsePictureFile.getData());
            Bitmap downloadedImage = decodeBase64(data);

            currPost.get().set_postPicture(downloadedImage);

            return downloadedImage;

        } catch (ParseException e) {
            Log.e(TAG, " request failed.");
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getPictureByPostAndChange(AtomicReference<PostProfilePicture> currPost) {
        final String TAG = "getPictureByPost(): ";

        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query = query.whereEqualTo("objectId", currPost.get().getObjectID());

        try {
            ParseFile parsePictureFile;
            ParseObject currPostObject = query.getFirst();

            parsePictureFile = (ParseFile) currPostObject.get("Picture");

            String data = new String(parsePictureFile.getData());
            Bitmap downloadedImage = decodeBase64(data);

            currPost.get().setPostPicture(downloadedImage);

            return downloadedImage;

        } catch (ParseException e) {
            Log.e(TAG, " request failed.");
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getProfilePictureAndChange(AtomicReference<PostProfilePicture> currPost) {
        final String TAG = "getPictureByPost(): ";

        ParseQuery<ParseObject> query = new ParseQuery("Users");
        query = query.whereEqualTo("UserName", currPost.get().getUserName());

        try {
            ParseFile parsePictureFile;
            ParseObject currPostObject = query.getFirst();

            parsePictureFile = (ParseFile) currPostObject.get("picture");

            String data = new String(parsePictureFile.getData());
            Bitmap downloadedImage = decodeBase64(data);

            currPost.get().setPostPicture(downloadedImage);

            return downloadedImage;

        } catch (ParseException e) {
            Log.e(TAG, " request failed.");
            e.printStackTrace();
        }
        return null;
    }

    public boolean removePostById(String postId) {
        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query.whereEqualTo("objectId", postId);
        try {
            ParseObject postObject = query.getFirst();
            postObject.put("isErased", 0);
            postObject.saveInBackground();

            Log.e("Delete post Object", "succeed!");
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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
                    String currUserName = Parse_model.getInstance().getUserClass().get_userName();
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

    public ArrayList<PostClass> getAllPosts() {

        ArrayList<PostClass> postsArrayList = new ArrayList<PostClass>();

        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query.whereEqualTo("isErased", 1);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {
                String postUserName = po.getString("UserName");
                String currUserName = Parse_model.getInstance().getUserClass().get_userName();
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
                        getNumOfComments(postID),
                        getNumOfLikes(postID),
                        po.getString("nameLikes"),
                        postID,
                        getNumFollowersByUserName(postUserName),
                        isLikedPostBefore(postID, currUserName),
                        po.getInt("picWidth"),
                        po.getInt("picHeight"),
                        po.getString("HashTags"),
                        po.getInt("isErased")
                );
                postsArrayList.add(currPost);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return postsArrayList;
    }

    public ArrayList<PostClass> getPostsById(ArrayList<String> objectsIdArray) {

        ArrayList<PostClass> postsArrayList = new ArrayList<PostClass>();
        for (String objectId : objectsIdArray) {
            ParseQuery<ParseObject> query = new ParseQuery("Posts");
            query.whereEqualTo("objectId", objectId);

            try {
                ParseObject po = query.getFirst();

                String postUserName = po.getString("UserName");
                String currUserName = Parse_model.getInstance().getUserClass().get_userName();
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
                postsArrayList.add(currPost);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return postsArrayList;
    }

    public boolean addPost(PostClass post) {
        ParseObject po = new ParseObject("Posts");

        po.put("UserName", post.get_userName());
        po.put("MainString", post.get_mainString());
        po.put("HashTags", post.get_hashTag());


        po.put("picWidth", post.getPicWidth());
        po.put("picHeight", post.getPicHeight());


        po.put("isErased", 1);

        //uploading profile pic
        Bitmap userPicture = Parse_model.getInstance().getUserClass().get_userPic();
        if (userPicture != null) {
            byte[] userPictureData = encodeToBase64(userPicture).getBytes();
            ParseFile userPictureFile = new ParseFile("pic.txt", userPictureData);
            po.put("userPicture", userPictureFile);
        }


        //uploading post pic
        byte[] postPictureData = encodeToBase64(post.get_postPicture()).getBytes();
        ParseFile postPictureFile = new ParseFile("pic.txt", postPictureData);
        po.put("Picture", postPictureFile);

        po.put("date", new CurrentDateTime().getDateTime());


        try {
            po.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<PostClass> getAllUserPosts(String userName) {
        ArrayList<PostClass> userPostsArrayList = new ArrayList<PostClass>();


        ParseQuery<ParseObject> query = new ParseQuery("Posts");
        query.whereEqualTo("UserName", userName);
        query.whereEqualTo("isErased", 1);
        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {
                PostClass currUserPost;
                String postUserName = po.getString("UserName");
                String currUserName = Parse_model.getInstance().getUserClass().get_userName();
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
                        getNumOfComments(postID),
                        getNumOfLikes(postID),
                        po.getString("nameLikes"),
                        postID,
                        getNumFollowersByUserName(postUserName),
                        isLikedPostBefore(postID, currUserName),
                        po.getInt("picWidth"),
                        po.getInt("picHeight"),
                        po.getString("HashTags"),
                        po.getInt("isErased")
                );

                userPostsArrayList.add(currPost);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userPostsArrayList;

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

    public ArrayList<PostClass> getAllUsersPostsByFollowings(ArrayList<String> userName){//, ArrayList<String> currPostsID) {
        ArrayList<PostClass> userPostsArrayList = new ArrayList<PostClass>();

        for (String userNameString : userName) {
            ParseQuery<ParseObject> query = new ParseQuery("Posts");
            query.whereEqualTo("UserName", userNameString);
            query.whereEqualTo("isErased", 1);
            try {
                List<ParseObject> result = query.find();
                for (ParseObject po : result) {
                    PostClass currUserPost;
                    String postUserName = po.getString("UserName");
                    String currUserName = Parse_model.getInstance().getUserClass().get_userName();
                    String postID = po.getObjectId();

//                    boolean isPostExist = false;
//                    for (String currPostID : currPostsID) {
//                        if (currPostID.equals(postID)) {
//                            isPostExist = true;
//                        }
//                    }
//                    if (!isPostExist) {
                    currPost = new PostClass(
                            postUserName,
                            po.getString("MainString"),
                            //getPictureInPost(po),
//                        getProfilePictureInPost(po),
                            null,
                            null,
                            po.getString("date"),
                            po.getString("Comments"),
                            getNumOfComments(postID),
                            getNumOfLikes(postID),
                            po.getString("nameLikes"),
                            postID,
                            getNumFollowersByUserName(postUserName),
                            isLikedPostBefore(postID, currUserName),
                            po.getInt("picWidth"),
                            po.getInt("picHeight"),
                            po.getString("HashTags"),
                            po.getInt("isErased")
                    );
                    userPostsArrayList.add(currPost);
//                    }


                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return userPostsArrayList;

    }

    public ArrayList<PostClass> getAllErasedPostsByUsersFollowings(ArrayList<String> userName){//, ArrayList<String> currPostsID) {
        ArrayList<PostClass> userPostsArrayList = new ArrayList<PostClass>();

        for (String userNameString : userName) {
            ParseQuery<ParseObject> query = new ParseQuery("Posts");
            query.whereEqualTo("UserName", userNameString);
            query.whereEqualTo("isErased", 0);
            try {
                List<ParseObject> result = query.find();
                for (ParseObject po : result) {

                    PostClass currUserPost = new PostClass();

                    currUserPost.setObjectID(po.getObjectId());
                    currUserPost.setIsErased(0);

                    userPostsArrayList.add(currPost);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return userPostsArrayList;

    }

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

    public ArrayList<String> getAllFollowingPostsID(ArrayList<String> followingArrayList) {
        ArrayList<String> userPostsIDArrayList = new ArrayList<>();

        for (String userNameString : followingArrayList) {
            ParseQuery<ParseObject> query = new ParseQuery("Posts");
            query.whereEqualTo("UserName", userNameString);
            query.whereEqualTo("isErased", 1);
            try {
                List<ParseObject> result = query.find();
                for (ParseObject po : result) {

                    String postID = po.getObjectId();

                    userPostsIDArrayList.add(postID);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return userPostsIDArrayList;

    }

    public int getNumOfPostsByUserName(String userName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.whereEqualTo("UserName", userName);
        query.whereEqualTo("isErased", 1);

        try {
            List<ParseObject> result = query.find();
            Log.e("number of posts: ", String.valueOf(result.size()));
            return result.size();
        } catch (ParseException e) {
            Log.e("number of posts: ", " request failed.");
            e.printStackTrace();
        }
        return 0;
    }

    public Bitmap getProfilePictureByUserName(String userName ){
        ParseQuery<ParseObject> query = new ParseQuery("Users");
        query = query.whereEqualTo("UserName", userName);

        try {
            ParseObject currPostObject = query.getFirst();

            ParseFile parsePictureFile = (ParseFile) currPostObject.get("picture");
            Bitmap downloadedImage;

            String data = new String(parsePictureFile.getData());
            if(data!=null){
                downloadedImage = decodeBase64(data);
            }else{
                downloadedImage = null;
            }


            return downloadedImage;

        } catch (ParseException e) {
            Log.e("ParseProfilePicByID", " request failed.");
            e.printStackTrace();
        }
        return null;
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
