package com.example.jpet.DB_Model.LocalDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.jpet.Camera.PostClass;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class PostSQL {

    final static String POST_TABLE = "posts";
    final static String POST_TABLE_ID = "_id";
    final static String POST_TABLE_USER_NAME = "userName";
    final static String POST_TABLE_MAIN_STRING = "mainString";
    final static String POST_TABLE_POST_PICTURE = "postPicture";
    final static String POST_TABLE_USER_PICTURE = "userPicture";
    final static String POST_TABLE_DATE = "date";
    final static String POST_TABLE_IS_LIKED = "isLiked";
    final static String POST_TABLE_IS_FOLLOWED = "isFollowed";
    final static String POST_TABLE_PIC_WIDTH = "picWidth";
    final static String POST_TABLE_PIC_HEIGHT = "picHeight";
    final static String POST_TABLE_IS_ERASED = "isErased";
    final static String POST_TABLE_HASH_TAG = "HashTags";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + POST_TABLE + " (" +
                POST_TABLE_ID + " TEXT," +
                POST_TABLE_USER_NAME + " TEXT," +
                POST_TABLE_MAIN_STRING + " TEXT," +
                POST_TABLE_POST_PICTURE + " TEXT," +
                POST_TABLE_USER_PICTURE + " TEXT," +
                POST_TABLE_DATE + " TEXT," +
                POST_TABLE_IS_LIKED + " TEXT," +
                POST_TABLE_IS_FOLLOWED + " TEXT," +
                POST_TABLE_PIC_WIDTH + " TEXT," +
                POST_TABLE_HASH_TAG + " TEXT," +
                POST_TABLE_PIC_HEIGHT + " TEXT," +
                POST_TABLE_IS_ERASED + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + POST_TABLE + ";");
    }

    public static ArrayList<PostClass> getAllPostsByUserName(SQLiteDatabase db, String _userName) {
        String where = POST_TABLE_USER_NAME + " = ?";
        String[] args = {_userName};
        Cursor cursor = db.query(POST_TABLE, null, where, args, null, null, null);

        ArrayList<PostClass> posts = new ArrayList<PostClass>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_TABLE_ID);
            int userNameIndex = cursor.getColumnIndex(POST_TABLE_ID);
            int mainStringIndex = cursor.getColumnIndex(POST_TABLE_MAIN_STRING);
            int postPictureIndex = cursor.getColumnIndex(POST_TABLE_POST_PICTURE);
            int userPictureIndex = cursor.getColumnIndex(POST_TABLE_USER_PICTURE);
            int dateIndex = cursor.getColumnIndex(POST_TABLE_DATE);
            int isLikedIndex = cursor.getColumnIndex(POST_TABLE_IS_LIKED);
            int isFollowedIndex = cursor.getColumnIndex(POST_TABLE_IS_FOLLOWED);
            int picWidthIndex = cursor.getColumnIndex(POST_TABLE_PIC_WIDTH);
            int picHeightIndex = cursor.getColumnIndex(POST_TABLE_PIC_HEIGHT);
            int isErasedIndex = cursor.getColumnIndex(POST_TABLE_IS_ERASED);
            int hashTags = cursor.getColumnIndex(POST_TABLE_HASH_TAG);
            do {
                String id = cursor.getString(idIndex);
                String userName = cursor.getString(userNameIndex);
                String mainString = cursor.getString(mainStringIndex);
                String postPicture = cursor.getString(postPictureIndex);
                String userPicture = cursor.getString(userPictureIndex);
                String date = cursor.getString(dateIndex);
                String isLiked = cursor.getString(isLikedIndex); //1-true, 0-false
                String isFollowed = cursor.getString(isFollowedIndex);//1-true, 0-false
                int picWidth = cursor.getInt(picWidthIndex);
                int picHeight = cursor.getInt(picHeightIndex);
                int isErased = Integer.valueOf(cursor.getString(isErasedIndex));
//                String hashTagsString = cursor.getString(hashTags);

                boolean isLikedBool;
                boolean isFollowedBool;

                if (isLiked == "1") {
                    isLikedBool = true;
                } else {
                    isLikedBool = false;
                }

                if (isFollowed == "1") {
                    isFollowedBool = true;

                } else {
                    isFollowedBool = false;
                }

                PostClass post = new PostClass(
                        userName,
                        mainString,
                        decodeBase64(postPicture),
                        decodeBase64(userPicture),
                        date,
                        id,
                        isLikedBool,
                        isFollowedBool,
                        picWidth,
                        picHeight,
                        isErased,
                        //hashTagsString
                        null);
                if (post.getIsErased() == 1) {
                    posts.add(post);
                }

            } while (cursor.moveToNext());
        }
        return posts;
    }

    public static ArrayList<PostClass> getAllPosts(SQLiteDatabase db) {
        Log.e("LocalDB", " getAllPost() 1");
        Cursor cursor = db.query(POST_TABLE, null, null, null, null, null, null);
        ArrayList<PostClass> posts = new ArrayList<PostClass>();

        Log.e("LocalDB", " getAllPost() 2");

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_TABLE_ID);
            int userNameIndex = cursor.getColumnIndex(POST_TABLE_USER_NAME);
            int mainStringIndex = cursor.getColumnIndex(POST_TABLE_MAIN_STRING);
            int postPictureIndex = cursor.getColumnIndex(POST_TABLE_POST_PICTURE);
            int userPictureIndex = cursor.getColumnIndex(POST_TABLE_USER_PICTURE);
            int dateIndex = cursor.getColumnIndex(POST_TABLE_DATE);
            int isLikedIndex = cursor.getColumnIndex(POST_TABLE_IS_LIKED);
            int isFollowedIndex = cursor.getColumnIndex(POST_TABLE_IS_FOLLOWED);
            int picWidthIndex = cursor.getColumnIndex(POST_TABLE_PIC_WIDTH);
            int picHeightIndex = cursor.getColumnIndex(POST_TABLE_PIC_HEIGHT);
            int isErasedIndex = cursor.getColumnIndex(POST_TABLE_IS_ERASED);
            int hashTags = cursor.getColumnIndex(POST_TABLE_HASH_TAG);
            do {
                String id = cursor.getString(idIndex);
                String userName = cursor.getString(userNameIndex);
                String mainString = cursor.getString(mainStringIndex);
                String postPicture = cursor.getString(postPictureIndex);
                String userPicture = cursor.getString(userPictureIndex);
                String date = cursor.getString(dateIndex);
                String isLiked = cursor.getString(isLikedIndex); //1-true, 0-false
                String isFollowed = ""; //cursor.getString(isFollowedIndex);//1-true, 0-false
                int picWidth = cursor.getInt(picWidthIndex);
                int picHeight = cursor.getInt(picHeightIndex);
                int isErased = Integer.valueOf(cursor.getString(isErasedIndex));
               // String hashTagsString = cursor.getString(hashTags);


                boolean isLikedBool;
                boolean isFollowedBool;

                if (isLiked == "1") {
                    isLikedBool = true;
                } else {
                    isLikedBool = false;
                }

                if (isFollowed == "1") {
                    isFollowedBool = true;

                } else {
                    isFollowedBool = false;
                }

                PostClass post = new PostClass(
                        userName,
                        mainString,
                        decodeBase64(postPicture),
                        decodeBase64(userPicture),
                        date,
                        id,
                        isLikedBool,
                        isFollowedBool,
                        picWidth,
                        picHeight,
                        isErased,
                     //   hashTagsString
                "");
                if (post.getIsErased() == 1) {
                    posts.add(post);
                    Log.e("Local DB", "getting posts from local DB");
                }

            } while (cursor.moveToNext());
        }
        return posts;
    }

    public static ArrayList<String> getAllPostsID(SQLiteDatabase db) {
        String where = POST_TABLE_IS_ERASED + " = ?";
        String[] args = {"false"};
        Cursor cursor = db.query(POST_TABLE, null, where, args, null, null, null);
        ArrayList<String> postsID = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_TABLE_ID);

            do {
                String id = cursor.getString(idIndex);
                postsID.add(id);
            } while (cursor.moveToNext());
        }
        return postsID;
    }

    public static PostClass getPostById(SQLiteDatabase db, String postId) {
        String where = POST_TABLE_ID + " = ?";
        String[] args = {postId};
        Cursor cursor = db.query(POST_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_TABLE_ID);
            int userNameIndex = cursor.getColumnIndex(POST_TABLE_ID);
            int mainStringIndex = cursor.getColumnIndex(POST_TABLE_MAIN_STRING);
            int postPictureIndex = cursor.getColumnIndex(POST_TABLE_POST_PICTURE);
            int userPictureIndex = cursor.getColumnIndex(POST_TABLE_USER_PICTURE);
            int dateIndex = cursor.getColumnIndex(POST_TABLE_DATE);
            int isLikedIndex = cursor.getColumnIndex(POST_TABLE_IS_LIKED);
            int isFollowedIndex = cursor.getColumnIndex(POST_TABLE_IS_FOLLOWED);
            int picWidthIndex = cursor.getColumnIndex(POST_TABLE_PIC_WIDTH);
            int picHeightIndex = cursor.getColumnIndex(POST_TABLE_PIC_HEIGHT);
            int isErasedIndex = cursor.getColumnIndex(POST_TABLE_IS_ERASED);
            int hashTags = cursor.getColumnIndex(POST_TABLE_HASH_TAG);

            String id = cursor.getString(idIndex);
            String userName = cursor.getString(userNameIndex);
            String mainString = cursor.getString(mainStringIndex);
            String postPicture = cursor.getString(postPictureIndex);
            String userPicture = cursor.getString(userPictureIndex);
            String date = cursor.getString(dateIndex);
            String isLiked = cursor.getString(isLikedIndex); //1-true, 0-false
            String isFollowed = cursor.getString(isFollowedIndex);//1-true, 0-false
            int picWidth = cursor.getInt(picWidthIndex);
            int picHeight = cursor.getInt(picHeightIndex);
            int isErased = Integer.valueOf(cursor.getString(isErasedIndex));
            String hashTagsString = cursor.getString(hashTags);

            boolean isLikedBool;
            boolean isFollowedBool;

            if (isLiked == "1") {
                isLikedBool = true;
            } else {
                isLikedBool = false;
            }

            if (isFollowed == "1") {
                isFollowedBool = true;

            } else {
                isFollowedBool = false;
            }

            PostClass post = new PostClass(
                    userName,
                    mainString,
                    decodeBase64(postPicture),
                    decodeBase64(userPicture),
                    date,
                    id,
                    isLikedBool,
                    isFollowedBool,
                    picWidth,
                    picHeight,
                    isErased,
                    hashTagsString);
            return post;
        }
        return null;
    }

    public static void addPost(SQLiteDatabase db, PostClass post) {
        ContentValues values = new ContentValues();
        values.put(POST_TABLE_ID, post.getObjectID());
        values.put(POST_TABLE_USER_NAME, post.get_userName());
        values.put(POST_TABLE_MAIN_STRING, post.get_mainString());
        values.put(POST_TABLE_POST_PICTURE, "");
        values.put(POST_TABLE_USER_PICTURE, "");
        values.put(POST_TABLE_DATE, post.get_date());
        values.put(POST_TABLE_IS_LIKED, post.getIsLiked());
        values.put(POST_TABLE_IS_FOLLOWED, post.getIsFollowed());
        values.put(POST_TABLE_PIC_WIDTH, post.getPicWidth());
        values.put(POST_TABLE_PIC_HEIGHT, post.getPicHeight());
        values.put(POST_TABLE_IS_ERASED, post.getIsErased());
      //  values.put(POST_TABLE_HASH_TAG, post.get_hashTag());

        db.insert(POST_TABLE, POST_TABLE_ID, values);
    }

    public static void updatePostPictureToPost(SQLiteDatabase db,Bitmap postPicture, String postID){

        String where = POST_TABLE_ID + " = ?";
        String[] args = {postID};

        ContentValues values = new ContentValues();

        values.put(POST_TABLE_POST_PICTURE, encodeToBase64(postPicture));
//        values.put(POST_TABLE_USER_PICTURE, encodeToBase64(profilePicture));

        if(db.update(POST_TABLE, values, where, args) < 1){
            Log.e("updateSQLpostPicture","FAILED");
        }else
            Log.e("updateSQLpostPicture", "SUCCEED");
    }

    public static void updateProfilePictureToPost(SQLiteDatabase db,Bitmap profilePicture, String postID){

        String where = POST_TABLE_ID + " = ?";
        String[] args = {postID};

        ContentValues values = new ContentValues();

//        values.put(POST_TABLE_POST_PICTURE, encodeToBase64(postPicture));
        values.put(POST_TABLE_USER_PICTURE, encodeToBase64(profilePicture));


//        db.insert(POST_TABLE, POST_TABLE_ID, values);

//        db.insert(POST_TABLE, POST_TABLE_ID,  )

        if(db.update(POST_TABLE, values, where, args) < 1){
            Log.e("updateSQLprofilePicture","FAILED");
        }else
            Log.e("updateSQLprofilePicture", "SUCCEED");
    }

    public static Bitmap getPostPictureByID(SQLiteDatabase db, String postID){
        String where = POST_TABLE_ID + " = ?";
        String[] args = {postID};
        Cursor cursor = db.query(POST_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {

            int postPictureIndex = cursor.getColumnIndex(POST_TABLE_POST_PICTURE);
//            int userPictureIndex = cursor.getColumnIndex(POST_TABLE_USER_PICTURE);

            String postPicture = cursor.getString(postPictureIndex);

            return decodeBase64(postPicture);
        }
        return null;
    }

    public static Bitmap getProfilePictureByID(SQLiteDatabase db, String postID){
        String where = POST_TABLE_ID + " = ?";
        String[] args = {postID};
        Cursor cursor = db.query(POST_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {

            int userPictureIndex = cursor.getColumnIndex(POST_TABLE_USER_PICTURE);

            String profilePicture = cursor.getString(userPictureIndex);

            return decodeBase64(profilePicture);
        }
        return null;
    }

    public static void removePostByID(SQLiteDatabase db, String postID){
        String where = POST_TABLE_ID + " = ?";
        String[] args = {postID};

        if(db.delete(POST_TABLE, where, args) < 1){
            Log.e("deletePostFromSQLite","FAILED");
        }else
            Log.e("deletePostFromSQLite", "SUCCEED");
    }



    //****************************************************************************
    public static String encodeToBase64(Bitmap image) {
        if (image != null) {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            return imageEncoded;
        } else {
            return null;
        }

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
    //***************************************************************************
}
