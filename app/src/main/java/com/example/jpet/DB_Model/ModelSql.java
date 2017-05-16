package com.example.jpet.DB_Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.LocalDB.PostSQL;
import com.example.jpet.DB_Model.LocalDB.UserSQL;
import com.example.jpet.loginFragment.UserClass;

import java.util.ArrayList;


public class ModelSql {
    final static int VERSION = 4;

    Helper sqlDb;

    static ModelSql instance = new ModelSql();

    public static ModelSql getInstance() {
        return instance;
    }

    public void init(Context context){
        if (sqlDb == null){
            sqlDb = new Helper(context);
        }
    }

    public void removePostByID(String postID){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        PostSQL.removePostByID(db, postID);
    }

    public ArrayList<String> getFollowersArrayByUser(UserClass user){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return UserSQL.getFollowersArrayByUser(db, user);
    }


    public void updateFollowersArray(UserClass user){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        UserSQL.updateFollowersArray(db, user);
    }

    public void addUser(UserClass user){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        UserSQL.addUser(db, user);
    }

    public ArrayList<UserClass> getAllUsers(){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return UserSQL.getAllUsers(db);
    }


    public ArrayList<PostClass> getAllPosts(){
            SQLiteDatabase db = sqlDb.getReadableDatabase();
        return PostSQL.getAllPosts(db);
    }

    public ArrayList<PostClass> getAllPostsByUserName(String userName){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return PostSQL.getAllPostsByUserName(db, userName);
    }

    public ArrayList<String> getAllPostsID(){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return PostSQL.getAllPostsID(db);
    }

    public PostClass getPostById(String id){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return PostSQL.getPostById(db, id);
    }

    public void addPost(PostClass post){
        SQLiteDatabase db = sqlDb.getWritableDatabase();
        PostSQL.addPost(db, post);
    }

    public void updatePostPicture(Bitmap postPicture, String postID){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        PostSQL.updatePostPictureToPost(db, postPicture, postID);
    }

    public void updateProfilePicture(Bitmap profilePicture, String postID){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        PostSQL.updateProfilePictureToPost(db, profilePicture, postID);
    }

    public Bitmap getPostPictureByID(String postID){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return PostSQL.getPostPictureByID(db, postID);
    }

    public Bitmap getProfilePictureByID(String postID){
        SQLiteDatabase db = sqlDb.getReadableDatabase();
        return PostSQL.getProfilePictureByID(db, postID);
    }

    class Helper extends SQLiteOpenHelper{
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            PostSQL.create(db);
            UserSQL.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            PostSQL.drop(db);
            UserSQL.drop(db);
            onCreate(db);
        }
    }
}
