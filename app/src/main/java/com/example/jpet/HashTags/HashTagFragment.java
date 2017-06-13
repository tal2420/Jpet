package com.example.jpet.HashTags;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.Home.LoadPostBitmap;
import com.example.jpet.MainActivity;
import com.example.jpet.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by eliyadaida on 5/28/15.
 */
public class HashTagFragment extends Fragment {


    public HashTagFragment(){};

    static String title;
     static PostClass currentPost;
    GridView gridview;
    ArrayList<PostClass> postsArray;

    Bitmap noPostImageBitmap;
    Bitmap noProfilePictureBitmap;
    ImageAdapter adapter;
    View root;

    public static void setInfo(String text, PostClass post) {
       title = text;
       currentPost = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         root = inflater.inflate(R.layout.fragment_hash_tag, container, false);


         ((MainActivity)getActivity()).getSupportActionBar().setTitle(title.toString());

        adapter = new ImageAdapter(getActivity());


        noPostImageBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_image_available);
        noProfilePictureBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_profile_picture);

        gridview = (GridView) root.findViewById(R.id.gridView);

        new getAllPics(title).execute();
//        gridview.notifyAll();



        return root;
    }


    public class getAllPics extends AsyncTask<Void, Void, Bitmap> {

         String hashTag;

        public getAllPics(String hashTag){
            this.hashTag = hashTag;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            postsArray = Parse_model.getInstance().getAllPostsByHashTag(hashTag);

            return null;
        }

        protected void onPostExecute(Bitmap result){
            gridview.setAdapter(adapter);
            root.findViewById(R.id.searchFragmentLoadingPanel).setVisibility(View.GONE);

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.photogallery_template, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.photoGalleryTemplate);
//            imageView.setImageBitmap(Parse_model.getInstance().getSearchFragmentPicsVector().get(position).get_postPicture());

            if(postsArray.get(position).get_postPicture()==null){
                AtomicReference<PostClass> currPostReference = new AtomicReference<>();
                currPostReference.set(postsArray.get(position));
                new LoadPostBitmap(currPostReference, imageView, noPostImageBitmap, 1);
                postsArray.get(position).set_postPicture(currPostReference.get().get_postPicture());
            }

            //TextView textView = (TextView) convertView.findViewById(R.id.textViewTemplate);
            //textView.setText(postClassVector.get(position).get_mainString());
            return imageView;
        }

    }

//    public void setPostsArray(){
//        new getAllPics().execute();
//    }


}
