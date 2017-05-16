package com.example.jpet.DB_Model.Models;

import android.os.AsyncTask;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.Parse_model;

import java.util.ArrayList;

/**
 * Created by Liran on 06-Jul-15.
 */
public class Search_Model {


    public static Search_Model instance = new Search_Model();

    public static Search_Model getInstance() {
        return instance;
    }

    public void getPostsForSearchPage(int WhichSearchPage) {

        switch (WhichSearchPage){
            //Main search page
            case 1:{
                new getAllPostsForSearchPage().execute();
                break;
            }
            // Profile search page view
            case 2:{

                break;
            }
            // After search in search page
            case 3:{

                break;
            }

        }

    }

    //***********************************UPDATE MAIN SEARCH FRAGMENT***********************************************
    public class getAllPostsForSearchPage extends AsyncTask<Void, ArrayList<PostClass>, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<PostClass> postsArray = new ArrayList<>();

            //Get all posts
            postsArray = Parse_model.getInstance().getAllPosts();
            onProgressUpdate(postsArray);

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<PostClass>... values) {
            if(updateSearchPageDelegate!=null){
                updateSearchPageDelegate.updateArrayList(values[0]);
            }

            super.onProgressUpdate(values);
        }

    }

    //***************************************UPDATE SEARCH PAGE POSTS LIST*****************************************
    public interface UpdateSearchPageInterface {
        public void updateArrayList(ArrayList<PostClass> postsArray);
    }

    UpdateSearchPageInterface updateSearchPageDelegate;

    public void setUpdateSearchPageDelegate(UpdateSearchPageInterface updateSearchPageDelegate) {
        this.updateSearchPageDelegate = updateSearchPageDelegate;
    }

    //************************************************************************************************************


    public static class SearchPageMenu{
        static int MainSearchPage = 1;
        static int ProfileSearchPage = 2;
        static int ResultSeachPage = 3;

        SearchPageMenu searchPageMenu = new SearchPageMenu();

    }

}
