package com.example.jpet.Search;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.ModelSql;
import com.example.jpet.DB_Model.Models.Home_Model;
import com.example.jpet.DB_Model.Models.Search_Model;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.Home.HomeFragment;
import com.example.jpet.Home.LoadPostBitmap;
import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.example.jpet.Search.Algorithem.DataSet;
import com.example.jpet.Search.Algorithem.SortUserPostsByPriority;
import com.example.jpet.fragments.FilterFragment;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    HomeFragment homeFragment;

    public SearchFragment() {
        // Required empty public constructor
    }

    //****************************************************************
    public interface goToPostPage {
        public void infoToPostPageFunction(String Id);
    }

    static goToPostPage postInfoToPostPageDelegates;

    public static void setInfoToFragment2Delegate(goToPostPage textToFragment2Delegate) {
        postInfoToPostPageDelegates = textToFragment2Delegate;
    }

    //****************************************************************

    GridView gridview;
    ArrayList<PostClass> postsArray = new ArrayList<>();
    ArrayList<PostClass> localDBPostArray;
    ArrayList<DataSet> dateSetPostsArray;

    Bitmap noPostImageBitmap;
    Bitmap noProfilePictureBitmap;
    ImageAdapter adapter;
    View root;


    int SearchPage = 1;
    String userNamePosts;

    //***************************************************************************************************************

    public void updateListInNewPost(ArrayList<PostClass> postsArrayList){


    }
    //***************************************************************************************************************

    public void setSearchPage(int searchPage, String userNamePosts) {
        this.SearchPage = searchPage;
        this.userNamePosts = userNamePosts;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);

        // Showing the fragment's optioned actionbar
        setHasOptionsMenu(true);

        updateListInNewPostDelegate();


        noPostImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);

        gridview = (GridView) root.findViewById(R.id.gridView);
        adapter = new ImageAdapter(getActivity());


        if (postsArray.size() == 0) {

            if (SearchPage == 1) {
                if (postsArray.size() == 0) {
                    new getAllPics(userNamePosts, root).execute();
                } else {
                    gridview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    root.findViewById(R.id.searchFragmentLoadingPanel).setVisibility(View.GONE);

                }
            } else {
                new getAllPics(userNamePosts, root).execute();
            }
        } else {
            gridview.setAdapter(adapter);

            root.findViewById(R.id.searchFragmentLoadingPanel).setVisibility(View.GONE);

        }


        return root;
    }

    private String grid_currentQuery = null; // holds the current query...

    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {
            if (TextUtils.isEmpty(newText)) {
                 ((MainActivity)getActivity()).getSupportActionBar().setSubtitle("");
                grid_currentQuery = null;
            } else {
                 ((MainActivity)getActivity()).getSupportActionBar().setSubtitle("");
                grid_currentQuery = newText;

            }
            //  getLoaderManager().restartLoader(0, null, SearchFragment.this);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();

            String[] queryWords = query.split(" ");

            ArrayList<String> mainStrings = Parse_model.getInstance().getMainStringFromPosts();
            ArrayList<String> IDArrayListTemp = new ArrayList<String>();
            ArrayList<String> mainStringTemp = new ArrayList<String>();

            for (int i = 0; i < mainStrings.size(); i = i + 2) {
                mainStringTemp.add(mainStrings.get(i));
            }

            for (int i = 1; i < mainStrings.size(); i = i + 2) {
                IDArrayListTemp.add(mainStrings.get(i));
            }

            ArrayList<String> suitableWordsID = new ArrayList<String>();

            int flag = 0;

            for (String wordFromQuery : queryWords) {

                for (int i = 0; i < mainStrings.size() / 2; i++) {
                    if (mainStringTemp.get(i).equals(wordFromQuery)) {
                        if (suitableWordsID.size() != 0) {
                            for (String id : suitableWordsID) {
                                if (id.equals(IDArrayListTemp.get(i)))
                                    flag = 0;
                                else
                                    flag = 1;
                            }
                        } else
                            flag = 1;
                    }

                    if (flag != 0) {
                        suitableWordsID.add(IDArrayListTemp.get(i));
                        flag = 0;
                    }


                }
            }


            new loadQueryPictures(suitableWordsID).execute();

            return false;
        }
    };

    public class loadQueryPictures extends AsyncTask<Void, Void, Void> {
        ArrayList<String> suitableWordsID;

        public loadQueryPictures(ArrayList<String> suitableWordsID) {
            this.suitableWordsID = suitableWordsID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            postsArray = Parse_model.getInstance().getPostsById(suitableWordsID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       if(SearchPage == 1){
           inflater.inflate(R.menu.search_menu, menu);
           SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
           if (searchView != null) {
               searchView.setOnQueryTextListener(queryListener);
           }
       }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                break;


            case R.id.filter_action:
                ((MainActivity)getActivity()).openNewFrag(new FilterFragment());
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bargs) {

        String sort = "SortColumn ASC";
        String[] grid_columns = new String[]{"ColumnA", "ColumnB", "Etc..."};
        String grid_whereClause = "ColumnToSearchBy LIKE ?";

        if (!TextUtils.isEmpty(grid_currentQuery)) {
            return new CursorLoader(getActivity(), null, grid_columns, grid_whereClause, new String[]{grid_currentQuery + "%"}, sort);
        }

        return new CursorLoader(getActivity(), null, grid_columns, null, null, sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> objectLoader) {

    }

    public class getAllPics extends AsyncTask<Void, Void, Boolean> {

        String userName;
        View view;

        public getAllPics(String userName, View root) {
            this.userName = userName;
            this.view = root;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (SearchPage == 1) {
                postsArray = ModelSql.getInstance().getAllPosts();
                Log.e("search:", "got from db " + postsArray.size() + " posts");

                if (postsArray.size() != 0) {
                    return true;
                }
            } else {
                postsArray = ModelSql.getInstance().getAllPostsByUserName(userName);
                if (postsArray.size() != 0) {
                    postsArray = new CurrentDateTime().sortPostsArrayByDate(postsArray);
                    return true;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                gridview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                root.findViewById(R.id.searchFragmentLoadingPanel).setVisibility(View.GONE);
            }
            new getPostsArrayFromParse(view, aBoolean, userName).execute();
        }
    }

    public class getPostsArrayFromParse extends AsyncTask<Void, Void, Void> {

        View view;
        boolean isPostsArrayNotNull;
        String userName;

        public getPostsArrayFromParse(View view, boolean isPostsArrayNull, String userName) {
            this.view = view;
            this.isPostsArrayNotNull = isPostsArrayNull;
            this.userName = userName;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (SearchPage == 1) {
                localDBPostArray = postsArray;
                postsArray = Parse_model.getInstance().getAllPosts();
//                postsArray = new SortUserPostsByPriority(
//                        Parse_model.getInstance().getPostsClusters(),
//                        postsArray,
//                        Parse_model.getInstance().getAllCommentsLikesByUser()
//                ).getSortedPostsByPriority();
            } else {
                localDBPostArray = postsArray;
                postsArray = Parse_model.getInstance().getAllUserPosts(userName);
                if (postsArray != null)
                    postsArray = new CurrentDateTime().sortPostsArrayByDate(postsArray);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (postsArray.size() != 0) {
                if (isPostsArrayNotNull) {
                    adapter.notifyDataSetChanged();
                } else {
                    gridview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                root.findViewById(R.id.searchFragmentLoadingPanel).setVisibility(View.GONE);
            }
            new updateLocalDBPosts().execute();
        }
    }


    public class updateLocalDBPosts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //checking in local DB if there are any new posts from parse and update them
            for (PostClass currPost : postsArray) {
                boolean isPostExists = false;
                for (PostClass currLocalPost : localDBPostArray) {
                    if (currPost.getObjectID().equals(currLocalPost.getObjectID())) {
                        isPostExists = true;
                    }
                }
                if (!isPostExists) {
                    ModelSql.getInstance().addPost(currPost);
                    Log.e("post not exist", "adding local DB");
                }
            }

            return null;
        }
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return postsArray.size();
        }

        public Object getItem(int position) {
            return postsArray.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.photogallery_template, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.photoGalleryTemplate);

            if (postsArray.get(position).get_postPicture() == null) {
                AtomicReference<PostClass> currPostReference = new AtomicReference<>();
                currPostReference.set(postsArray.get(position));
                new LoadPostBitmap(currPostReference, imageView, noPostImageBitmap, 1);
                postsArray.get(position).set_postPicture(currPostReference.get().get_postPicture());
            } else {
                imageView.setImageBitmap(postsArray.get(position).get_postPicture());
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    homeFragment = new HomeFragment();
                    homeFragment.setHomoPage(3);
                    Home_Model.getInstance().setPostsArrayList(3, null, postsArray.get(position).getObjectID());
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();
                }
            });

            return convertView;
        }

    }

    public void updateListInNewPostDelegate(){
        Search_Model.getInstance().setUpdateSearchPageDelegate(new Search_Model.UpdateSearchPageInterface() {
            @Override
            public void updateArrayList(ArrayList<PostClass> postsArray) {
                updateListInNewPost(postsArray);
            }
        });
    }


}

