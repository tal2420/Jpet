package com.example.jpet.profile;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.Home.LoadPostBitmap;
import com.example.jpet.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {

    public void setPostsArray(ArrayList<PostClass> postClassVector) {
        this.postClassVector = postClassVector;
    }

    Bitmap noPostImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
    Bitmap noProfilePictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);

    ArrayList<PostClass> postClassVector;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        GridView gridview = (GridView) root.findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        // gets all the posts by the current user's name


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return postClassVector.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            if(postClassVector.get(position).get_postPicture()==null){
                AtomicReference<PostClass> currPost = new AtomicReference<>();
                new LoadPostBitmap(currPost, imageView,noPostImageBitmap,1);
                postClassVector.get(position).set_postPicture(currPost.get().get_postPicture());
            }


            return imageView;
        }

    }


}
