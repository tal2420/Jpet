package com.example.jpet.profile;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.example.jpet.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by eliyadaida on 5/13/15.
 */
public class FollowingDialogFragment extends DialogFragment {


    String _UserName;
    String currentUser;
    ListView listViewFollowers;
    View root;
    ListAdapter listAdapter;

    ArrayList<FollowersClass> followersList = new ArrayList<>();


    Bitmap noProfilePictureBitmap;


    static FollowingDialogFragment newInstance(String _UserName) {
        FollowingDialogFragment FollowingDialogFragment = new com.example.jpet.profile.FollowingDialogFragment();
        FollowingDialogFragment.set_UserName(_UserName);
        return FollowingDialogFragment;
    }

    public void set_UserName(String _UserName) {
        this._UserName = _UserName;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_followers_dialog, container, false);

        //set the dialog's title
        getDialog().setTitle("Following");

        listViewFollowers = (ListView)root.findViewById(R.id.list_view_followers);
        currentUser = Parse_model.getInstance().getUserClass().get_userName();
        noProfilePictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);

        new getAllFollowers(root).execute();


        return root;
    }



    protected class getAllFollowers extends AsyncTask<String, String, String> {

        View currView;

        public getAllFollowers(View view) {
            currView = view;
        }

        @Override
        protected void onPreExecute() {
//            root.findViewById(R.id.followFragmentLoadingPanel).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            followersList = Parse_model.getInstance().getFollowingByUserName(_UserName);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            listViewFollowers = (ListView) currView.findViewById(R.id.list_view_followers);
            listAdapter = new ListAdapter(getActivity(), getView());
            if (followersList.size() != 0) {
                listViewFollowers.setAdapter(listAdapter);
                //scrollMyListViewToBottom();
            } else{
                listAdapter.notifyDataSetChanged();
            }
            root.findViewById(R.id.followFragmentLoadingPanel).setVisibility(View.GONE);

            super.onPostExecute(s);
        }
    }



    public class ListAdapter extends BaseAdapter {
        private Context mContext;

        View mainView;

        public ListAdapter(Context mContext, View mainView) {
            this.mContext = mContext;
            this.mainView = mainView;
        }

        public int getCount() {
            return followersList.size();
        }

        public Object getItem(int position) {
            return followersList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.follower_item, null);
            }

            ImageView followingPic = (ImageView) convertView.findViewById(R.id.follower_picture);
            TextView _userName = (TextView) convertView.findViewById(R.id.user_name);

            // sets profile picture
            followingPic.setBackground(null);
            FollowersClass currFollowerItem = followersList.get(position);

            if(currFollowerItem.getUserThatFollowMePic()==null){
                AtomicReference<FollowersClass> currFollowingReference = new AtomicReference<>();
                currFollowingReference.set(currFollowerItem);
                new LoadFollowerBitmap(currFollowingReference, followingPic, noProfilePictureBitmap);
                currFollowerItem.setUserThatFollowMePic(currFollowingReference.get().getUserThatFollowMePic());
            }else{
                followingPic.setImageBitmap(followersList.get(position).getUserThatFollowMePic());
            }

            // sets user name
            final String followingName = followersList.get(position).getUserNameThatFollowMe();
            _userName.setText(followingName);

            //set the suitable button
            final Button follow = (Button) convertView.findViewById(R.id.start_follow_btn);
            final Button alreadyFollow = (Button) convertView.findViewById(R.id.is_follow);

            if(Parse_model.getInstance().isFollowedUserBefore(currentUser , followersList.get(position).getUserNameThatFollowMe())) {
                alreadyFollow.setVisibility(View.VISIBLE);
                follow.setVisibility(View.GONE);
            }

            else {
                alreadyFollow.setVisibility(View.GONE);
                follow.setVisibility(View.VISIBLE);

            }


            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!followingName.equals(Parse_model.getInstance().getUserClass().get_userName())){
                        alreadyFollow.setVisibility(View.VISIBLE);
                        follow.setVisibility(View.GONE);

                        String _currUserName = currentUser;
                        String _currentUserId = Parse_model.getInstance().getUserClass().get_userId();
                        String _userIFollowName = String.valueOf(followersList.get(position).getUserNameThatFollowMe());

                        new FollowingNewUser(_userIFollowName, position).execute(_currUserName, _currentUserId);

                    }else{
                        Toast.makeText(getActivity(), "You can't follow yourself", Toast.LENGTH_SHORT).show();
                    }

                }
            });



            return convertView;
        }
    }

    private void scrollMyListViewToBottom(final int num) {
        listViewFollowers.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listViewFollowers.setSelection(num);
            }
        });
    }


    public class FollowingNewUser extends AsyncTask<String, Void, String> {

        String userIFollowName;
        int followersCounter;
        Bitmap followerPic;
        int position;

        public FollowingNewUser(String userIFollowName, int position) {
            this.userIFollowName = userIFollowName;
            this.position = position;
            followersCounter = 0;
            followerPic = Parse_model.getInstance().getUserClass().get_userPic();
        }

        public int getFollowersCounter() {
            followersCounter = Parse_model.getInstance().getNumOfFollowers(userIFollowName);
            return followersCounter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Button follow = (Button) getView().findViewById(R.id.start_follow_btn);
            Button alreadyFollow = (Button) getView().findViewById(R.id.is_follow);
            alreadyFollow.setVisibility(View.VISIBLE);
            follow.setVisibility(View.GONE);

            listAdapter.notifyDataSetInvalidated();
            scrollMyListViewToBottom(position);
        }

        @Override
        protected String doInBackground(String... params) {

            Parse_model.getInstance().addFollowerToUser(params[0], params[1], userIFollowName, followerPic );

            NotificationsOfPost notification = new NotificationsOfPost();
            notification.setUserName_getNotification(userIFollowName);
            notification.setRead(false);
            notification.setRecentAction("F");
            notification.setUserName_doAction(params[0]);
            notification.setProfilePicture(followerPic);

            Parse_model.getInstance().addFollowNotification(notification);

            return  null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

}
