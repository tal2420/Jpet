package com.example.jpet.LikeAndFollowing;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.example.jpet.profile.RoundImageClass;

import java.util.ArrayList;


public class LikeAndFollowingFragment extends Fragment {

    static ArrayList<NotificationsOfPost> notificationsOfPostArrayList = new ArrayList<NotificationsOfPost>();
    static ListView listView;
    static LikeFollowAdapter adapter;
    ImageView followerPic;
    View root;


    //****************************************************************
    public interface goToPostPage {
        public void infoToPostPageFunction(String Id);
    }

    static goToPostPage postInfoToPostPageDelegates;

    public static void setInfoToFragment2Delegate(goToPostPage textToFragment2Delegate) {
        postInfoToPostPageDelegates = textToFragment2Delegate;
    }

    //****************************************************************

    //*******************************************************************************
    //going to user profile from post
    public interface userPostToUserProfileInterface {
        public void userPostToUserProfile(String userName, Bitmap userPicture);
    }


    static userPostToUserProfileInterface userPostToUserProfileDelegate1;

    public static void setnotificationFragToUserProfileDelegate(userPostToUserProfileInterface userPostToUserProfileDelegate) {
        userPostToUserProfileDelegate1 = userPostToUserProfileDelegate;
    }

    //*******************************************************************************

    // Required empty public constructor

    public LikeAndFollowingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_like_and_following, container, false);

         ((MainActivity)getActivity()).getSupportActionBar().setTitle("Notifications");

        listView = (ListView) root.findViewById(R.id.listView);

        followerPic = (ImageView) root.findViewById(R.id.user_picture);


        new getNotification(root).execute();

        adapter = new LikeFollowAdapter();

        return root;

    }


    //the LikeFollow list adapter
    private class LikeFollowAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notificationsOfPostArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return notificationsOfPostArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View itemView, ViewGroup parent) {


            int i = position;


            // commercialsList = commercialsListTemp;
            if (itemView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                itemView = inflater.inflate(R.layout.like_following_item, null);
            }

            //get the notification from the i potion
            final NotificationsOfPost currentNotification = notificationsOfPostArrayList.get(i);

            TextView userName = (TextView) itemView.findViewById(R.id.user_name);
            userName.setText(currentNotification.getUserName_doAction());
            userName.setTypeface(null, Typeface.BOLD);
            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (userPostToUserProfileDelegate1 != null) {
                        Bitmap userPicture = currentNotification.getProfilePicture();
                        String userNameString = currentNotification.getUserName_doAction();

                        userPostToUserProfileDelegate1.userPostToUserProfile(userNameString, userPicture);
                    }
                }
            });


            TextView description = (TextView) itemView.findViewById(R.id.description);
            String descriptionTemp = null;

            //Like notification case
            if (currentNotification.getRecentAction().equals("L")) {
                descriptionTemp = " like your photo     ";
                ImageView userImage = (ImageView) itemView.findViewById(R.id.user_picture);
                userImage.setImageBitmap(currentNotification.getPostPicture());
                userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (postInfoToPostPageDelegates != null) {
                            postInfoToPostPageDelegates.infoToPostPageFunction(currentNotification.getPostIdLiked());
                        }
//                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, postFragment).addToBackStack(null).commit();
                    }
                });

                //Follow notification case
            } else if (currentNotification.getRecentAction().equals("F")) {
                descriptionTemp = " started following you";
                ImageView userImage = (ImageView) itemView.findViewById(R.id.user_picture);
                userImage.setImageDrawable(null);

                //Comment notification case
            } else if (currentNotification.getRecentAction().equals("C")) {
                descriptionTemp = " left a comment      ";
                ImageView userImage = (ImageView) itemView.findViewById(R.id.user_picture);
                userImage.setImageBitmap(currentNotification.getPostPicture());
                userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (postInfoToPostPageDelegates != null) {
                            postInfoToPostPageDelegates.infoToPostPageFunction(currentNotification.getPostIdLiked());
                        }
                        FragmentManager fm = getFragmentManager();
                        getActivity().overridePendingTransition(R.animator.animation1, R.animator.animator2);

//                            fm.beginTransaction().replace(R.id.fragment_container, postFragment).addToBackStack("notification").commit();
                        fm.popBackStack();

                    }
                });
                ;
            }


            description.setText(descriptionTemp);
            ImageView followerImage = (ImageView) itemView.findViewById(R.id.follower_picture);
            RoundImageClass roundProfileImage = new RoundImageClass(currentNotification.getProfilePicture());
            followerImage.setImageDrawable(roundProfileImage);

//            if(currentNotification.getProfilePicture()==null){
//                AtomicReference<NotificationsOfPost> currNotificationReference = new AtomicReference<>();
//                currNotificationReference.set(currentNotification);
//                new LoadProfileImageBitmap(currNotificationReference, followerImage, ((MainActivity)getActivity()).getNoProfilePictureBitmap());
//            }




            TextView date = (TextView) itemView.findViewById(R.id.date);
            String s = currentNotification.get_date();
            CurrentDateTime.Date dateOfNotification = new CurrentDateTime().getDateByString(s);
            String dateNow = new CurrentDateTime().getDateTime();
            CurrentDateTime.Date currentDate = new CurrentDateTime().getDateByString(dateNow);

            if (dateOfNotification.getYear() != currentDate.getYear())
                date.setText(String.valueOf(currentDate.getYear() - dateOfNotification.getYear()) + "y");

            else if (dateOfNotification.getMonth() != currentDate.getMonth())
                date.setText(String.valueOf(currentDate.getMonth() - dateOfNotification.getMonth()) + "m");

            else if (dateOfNotification.getDay() != currentDate.getDay())
                date.setText(String.valueOf(currentDate.getDay() - dateOfNotification.getDay()) + "d");

            else if (dateOfNotification.getHour() != currentDate.getHour())
                date.setText(String.valueOf(currentDate.getHour() - dateOfNotification.getHour()) + "h");

            else if (dateOfNotification.getMinute() != currentDate.getMinute())
                date.setText(String.valueOf(currentDate.getMinute() - dateOfNotification.getMinute()) + "minutes");

            else if (dateOfNotification.getSecond() != currentDate.getSecond())
                date.setText(String.valueOf(currentDate.getSecond() - dateOfNotification.getSecond()) + "sec");


            //check if the notification was seen by the user before
            if (!currentNotification.getIsRead()) {
                itemView.setBackgroundColor(Color.rgb(192, 192, 192));
                new updateNotification(currentNotification.getNotificationId()).execute();

            } else
                itemView.setBackgroundColor(Color.argb(29, 31, 186, 255));

            return itemView;


        }
    }


    protected class getNotification extends AsyncTask<Void, Void, Void> {

        View view;
        ArrayList<NotificationsOfPost> arrayList;

        public getNotification(View view) {
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = Parse_model.getInstance().getAllNotification(Parse_model.getInstance().getUserClass().get_userName());

            arrayList = new CurrentDateTime().sortNotificationsArrayByDate(arrayList);
            notificationsOfPostArrayList = arrayList;


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listView.setAdapter(adapter);
            view.findViewById(R.id.LikeAndFollowingLoadingPanel).setVisibility(View.GONE);
        }
    }


    protected class updateNotification extends AsyncTask<Void, Void, Void> {

        String notificationId;

        public updateNotification(String notificationId) {
            this.notificationId = notificationId;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Parse_model.getInstance().updateNotifications(notificationId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

}