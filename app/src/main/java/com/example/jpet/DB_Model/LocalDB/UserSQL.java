package com.example.jpet.DB_Model.LocalDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jpet.loginFragment.UserClass;

import java.util.ArrayList;

/**
 * Created by Liran on 02-Jun-15.
 */
public class UserSQL {


    final static String USER_TABLE = "USER";
    //    final static String POST_TABLE_ID = "_id";
    final static String USER_TABLE_USER_NAME = "userName";
    final static String USER_TABLE_PASSWORD = "password";
    final static String USER_TABLE_EMAIL = "email";
    final static String USER_TABLE_PROFILE_PICTURE = "profilePicture";
    final static String USER_TABLE_FOLLOWERS_ARRAY = "followersArray";


    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE + " (" +
//                POST_TABLE_ID + " TEXT," +
                USER_TABLE_USER_NAME + " TEXT," +
                USER_TABLE_PASSWORD + " TEXT," +
                USER_TABLE_EMAIL + " TEXT," +
                USER_TABLE_FOLLOWERS_ARRAY + " TEXT," +
                USER_TABLE_PROFILE_PICTURE + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + USER_TABLE + ";");
    }

    public static ArrayList<UserClass> getAllUsers(SQLiteDatabase db) {
        Cursor cursor = db.query(USER_TABLE, null, null, null, null, null, null);
        ArrayList<UserClass> users = new ArrayList<UserClass>();


        if (cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(POST_TABLE_ID);
            int userNameIndex = cursor.getColumnIndex(USER_TABLE_USER_NAME);
            int passwordIndex = cursor.getColumnIndex(USER_TABLE_PASSWORD);
            int emailIndex = cursor.getColumnIndex(USER_TABLE_EMAIL);
            int userPictureIndex = cursor.getColumnIndex(USER_TABLE_PROFILE_PICTURE);
            int followersArrayIndex = cursor.getColumnIndex(USER_TABLE_FOLLOWERS_ARRAY);

            do {
//                String id = cursor.getString(idIndex);
                String userName = cursor.getString(userNameIndex);
                String password = cursor.getString(passwordIndex);
                String email = cursor.getString(emailIndex);
                String userPicture = cursor.getString(userPictureIndex);
                String followersArray = cursor.getString(followersArrayIndex);

                ArrayList<String> followersNamesArray = new ArrayList<>();
                if (followersArray != null) {
                    String[] followersNamesTemp = followersArray.split(" ");
                    for (String currFollow : followersNamesTemp) {
                        followersNamesArray.add(currFollow);
                    }
                }

                UserClass currUser = new UserClass(
                        userName,
                        password,
                        email,
                        null,
                        followersNamesArray
                );

                users.add(currUser);
                Log.e("Local DB", "getting users from local DB");

            } while (cursor.moveToNext());
        }
        return users;
    }


    public static void addUser(SQLiteDatabase db, UserClass user) {
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_USER_NAME, user.get_userName());
        values.put(USER_TABLE_PASSWORD, user.get_password());
        values.put(USER_TABLE_EMAIL, user.get_email());
        values.put(USER_TABLE_PROFILE_PICTURE, "");


        String followersNamesString = null;
        for(String currFollowerItem : user.getFollowersArray()){
            followersNamesString += currFollowerItem;
        }
        values.put(USER_TABLE_FOLLOWERS_ARRAY, followersNamesString);

        db.insert(USER_TABLE, USER_TABLE_USER_NAME, values);
    }

    public static void updateFollowersArray(SQLiteDatabase db, UserClass user) {

        String where = USER_TABLE_USER_NAME + "=?";
        String[] args = {user.get_userName()};

        ContentValues values = new ContentValues();


        ArrayList<String> tempFollowersArray = user.getFollowersArray();

        String tempFollowersString = null;
        if(tempFollowersArray!=null){
            for (String follower : tempFollowersArray) {
                tempFollowersString += follower + " ";
            }
        }

        values.put(USER_TABLE_FOLLOWERS_ARRAY, tempFollowersString);

        if (db.update(USER_TABLE, values, where, args) < 1) {
            Log.e("updateSQLfollowersArray", "FAILED");
        } else
            Log.e("updateSQLfollowersArray", "SUCCEED");
    }

    public static ArrayList<String> getFollowersArrayByUser(SQLiteDatabase db, UserClass user) {
        String where = USER_TABLE_USER_NAME + " = ?";
        String[] args = {user.get_userName()};
        Cursor cursor = db.query(USER_TABLE, null, where, args, null, null, null);

        ArrayList<String> followersNamesArray = new ArrayList<>();

        if (cursor.moveToFirst()) {

            int followersArrayIndex = cursor.getColumnIndex(USER_TABLE_FOLLOWERS_ARRAY);

            String followersArray = cursor.getString(followersArrayIndex);

            if (followersArray == null) return null;
            String[] followersNamesTemp = followersArray.split(" ");


            for (String currFollow : followersNamesTemp) {
                followersNamesArray.add(currFollow);
            }

        }
        cursor.close();
        return followersNamesArray;
    }
}
