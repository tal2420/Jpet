package com.example.jpet.DB_Model.Models;

import android.os.AsyncTask;
import android.util.Log;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.ModelSql;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.loginFragment.UserClass;

import java.util.ArrayList;

/**
 * Created by Liran on 02-Jul-15.
 */
public class Home_Model {

    Home_Model() {
    }

    static final Home_Model instance = new Home_Model();

    public static Home_Model getInstance() {
        return instance;
    }

    public void setPostsArrayList(int WhichHomePage, String userName, String id) {
        switch (WhichHomePage) {
            case 1: {
                new setPostsArrayListTask().execute();
                break;
            }
            case 2: {
                new setProfileArrayListTask(userName).execute();
                break;
            }

            case 3: {
                new setOnePostList(id).execute();
                break;
            }
            case 4:{
                //refreshing main home page
                new RefreshFollowingPosts().execute();
                break;
            }
        }

    }

    //*************************SETTING MAIN HOME PAGE POSTS LIST*********************************************************************


    private class setPostsArrayListTask extends AsyncTask<Void, ArrayList<PostClass>, Void> {

        ArrayList<String> followersList;
        ArrayList<PostClass> postsArrayList;

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostClass> LocalDBPostsArray;

            // Getting posts from local data base
            UserClass currUser = Parse_model.getInstance().getUserClass();
            followersList = ModelSql.getInstance().getFollowersArrayByUser(currUser);
            if (followersList == null) {
                followersList = new ArrayList<>();
            }
            LocalDBPostsArray = ModelSql.getInstance().getAllPosts();
            if (followersList.size() != 0) {
                if (LocalDBPostsArray.size() != 0) {
                    LocalDBPostsArray = getPostsByFollowers(LocalDBPostsArray, followersList);
                    LocalDBPostsArray = new CurrentDateTime().sortPostsArrayByDate(LocalDBPostsArray);
                    // Updating home page
                    publishProgress(LocalDBPostsArray);
                }
            }

            // Getting posts from parse
            String userName = Parse_model.getInstance().getUserClass().get_userName();
            followersList = Parse_model.getInstance().getFollowersByUserNameToString(userName);
            followersList.add(userName);


            postsArrayList = Parse_model.getInstance().getAllUsersPostsByFollowings(followersList);

            for (PostClass currPost : postsArrayList) {
                for (PostClass currLocalDBPost : LocalDBPostsArray) {
                    if (currPost.getObjectID().equals(currLocalDBPost.getObjectID())) {
                        currPost.set_postPicture(currLocalDBPost.get_postPicture());
//                                currPost.set_userProfilePicture(currLocalDBPost.get_userProfilePicture());
                    }
                }
            }
            postsArrayList = new CurrentDateTime().sortPostsArrayByDate(postsArrayList);
            //Updating home page
            publishProgress(postsArrayList);


            // Updating local data base in new posts
            //checking in local DB if there are any new posts from parse and update them
            for (PostClass currPost : postsArrayList) {
                boolean isPostExists = false;
                for (PostClass currLocalPost : LocalDBPostsArray) {
                    if (currPost.getObjectID().equals(currLocalPost.getObjectID())) {
                        isPostExists = true;
                    }
                }
                if (!isPostExists) {
                    ModelSql.getInstance().addPost(currPost);
                    Log.e("post not exist", "adding local DB");
                }
            }

            //updating followers list in local DB
            Parse_model.getInstance().getUserClass().setFollowersArray(followersList);
            ModelSql.getInstance().updateFollowersArray(Parse_model.getInstance().getUserClass());

            // Deleting erased old posts from local DB
            ArrayList<PostClass> erasedFollowingUsersPosts = Parse_model.getInstance().getAllErasedPostsByUsersFollowings(followersList);
            for(PostClass currErasedPost : erasedFollowingUsersPosts){
                ModelSql.getInstance().removePostByID(currErasedPost.getObjectID());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<PostClass>... values) {
            //pass the updated postsArrayList to home fragment
            if (setPostsInHomePageDelegate != null) {
                setPostsInHomePageDelegate.setPosts(values[0], 1);
            }
//            ((MainActivity) ApplicationContextProvider.getContext()).setPostsImHomePage(values[0]);
        }
    }

    private ArrayList<PostClass> getPostsByFollowers(ArrayList<PostClass> postsArrayList, ArrayList<String> followersArray) {

        ArrayList<PostClass> newPostsArray = new ArrayList<>();
        for (PostClass currPost : postsArrayList) {
            for (String currFollower : followersArray) {
                if (currPost.get_userName().equals(currFollower)) {
                    newPostsArray.add(currPost);
                }
            }
        }
        return newPostsArray;
    }


    //************************SETTING PROFILE POSTS LIST******************************************************


    private class setProfileArrayListTask extends AsyncTask<Void, ArrayList<PostClass>, Void> {

        String userName;
        ArrayList<PostClass> userPostsList;
        ArrayList<PostClass> userPostsListLocalDB;

        public setProfileArrayListTask(String userName) {
            this.userName = userName;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Getting user's posts from local data base
            userPostsListLocalDB = ModelSql.getInstance().getAllPostsByUserName(userName);
            if (userPostsListLocalDB.size() != 0) {
                userPostsListLocalDB = new CurrentDateTime().sortPostsArrayByDate(userPostsListLocalDB);
            }
            publishProgress(userPostsListLocalDB);

            // Getting user's posts from parse
            userPostsList = Parse_model.getInstance().getAllUserPosts(userName);

            userPostsList = new CurrentDateTime().sortPostsArrayByDate(userPostsList);

            for (PostClass currPost : userPostsList) {
                boolean isPostExists = false;
                for (PostClass currLocalDBPost : userPostsListLocalDB) {
                    if (currPost.getObjectID().equals(currLocalDBPost.getObjectID())) {
                        //if post already exists in local db so it takes the main pic to the new posts parse array
                        currPost.set_postPicture(currLocalDBPost.get_postPicture());
//                        currPost.set_userProfilePicture(currLocalDBPost.get_userProfilePicture());
                        isPostExists = true;
                    }
                }
                //checking in local DB if there are any new posts from parse and update them
                if (!isPostExists) {
                    ModelSql.getInstance().addPost(currPost);
                    Log.e("post not exist", "adding local DB");
                }
            }
            publishProgress(userPostsList);
            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<PostClass>... values) {
            if (setPostsInHomePageDelegate != null) {
                setPostsInHomePageDelegate.setPosts(values[0], 2);
            }
        }
    }

    //************************SETTING ONE POST LIST******************************************************

    private class setOnePostList extends AsyncTask<Void, ArrayList<PostClass>, Void> {

        String postID;

        public setOnePostList(String postID) {
            this.postID = postID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<PostClass> postsArray;

            // checking if post exist in local db
            postsArray = new ArrayList<>();
            boolean postExist = false;
            PostClass post = ModelSql.getInstance().getPostById(postID);
            if (post != null) {
                postExist = true;
                postsArray.add(post);
                publishProgress(postsArray);
            }

            // Downloading the updated post from past
            postsArray = new ArrayList<>();
            post = Parse_model.getInstance().getPostById(postID);

            postsArray.add(post);
            publishProgress(postsArray);

            // Updating local data base
            if(!postExist){
                ModelSql.getInstance().addPost(post);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<PostClass>... values) {
            super.onProgressUpdate(values);
            if (setPostsInHomePageDelegate != null) {
                setPostsInHomePageDelegate.setPosts(values[0], 3);
            }

        }
    }

    //************************REFRESHING HOME PAGE******************************************************
    private class RefreshFollowingPosts extends AsyncTask<Void, Void, Void>{

        ArrayList<PostClass> postsArrayList;
        ArrayList<String> followersList;

        @Override
        protected Void doInBackground(Void... params) {


            String userName = Parse_model.getInstance().getUserClass().get_userName();
            followersList = Parse_model.getInstance().getFollowersByUserNameToString(userName);
            followersList.add(userName);


            postsArrayList = Parse_model.getInstance().getAllUsersPostsByFollowings(followersList);

            postsArrayList = new CurrentDateTime().sortPostsArrayByDate(postsArrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(setPostsInHomePageDelegate!=null){
                setPostsInHomePageDelegate.setPosts(postsArrayList, 1);
            }
            super.onPostExecute(aVoid);
        }
    }

    //**************SETTING HOME PAGE POSTS LIST DELEGATE****************************
    public interface SetPostsInHomePage {
        public void setPosts(ArrayList<PostClass> postsArrayList, int WhichHomePage);
    }

    SetPostsInHomePage setPostsInHomePageDelegate;

    public void setSetPostsInHomePageDelegate(SetPostsInHomePage setPostsInHomePageDelegate) {
        this.setPostsInHomePageDelegate = setPostsInHomePageDelegate;
    }

}
