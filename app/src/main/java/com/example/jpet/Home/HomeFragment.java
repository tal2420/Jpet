package com.example.jpet.Home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.Camera.ScalableImageView;
import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.ModelSql;
import com.example.jpet.DB_Model.Models.Home_Model;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.DEBUG;
import com.example.jpet.HashTags.HashTagFragment;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.example.jpet.loginFragment.UserClass;
import com.example.jpet.objects.Animal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

//import com.example.jpet.SQL_model;

/**
 * Created by eliyadaida on 3/22/15.
 */
public class HomeFragment extends Fragment {

    public static int HOMO_PAGE;

    /*
    1 = main home page
    2 = profile home page
    3 = 1 post home page
     */
    public void setHomoPage(int homoPage) {
        HOMO_PAGE = homoPage;
    }

    //*******************************************************************************
    //going to user profile from post
    public interface userPostToUserProfileInterface {
        public void userPostToUserProfile(String userName, Bitmap userPicture);
    }

    userPostToUserProfileInterface userPostToUserProfileDelegate;

    public void setUserPostToUserProfileDelegate(userPostToUserProfileInterface userPostToUserProfileDelegate) {
        this.userPostToUserProfileDelegate = userPostToUserProfileDelegate;
    }

    //****************************************************************
    public interface postToHashFragment {
        public void infoToHashFragmentFunction(String text, PostClass post);
    }

    static postToHashFragment postInfoToHashDelegates;

    public static void setTextToFragment2Delegate(postToHashFragment textToFragment2Delegate) {
        postInfoToHashDelegates = textToFragment2Delegate;
    }

    //****************************************************************


    //number of comments that comes from the comment dialog fragment
    public void setNumOfComments(String numOfComments) {
        this.numOfComments = numOfComments;
    }

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    ArrayList<String> followersList;
    String numOfComments;

    DialogFragment dialogFragment;

    static int numOfLikes;
//    ArrayList<String> hashTags;

    ArrayList<PostClass> localDBPostArray;
    //    ListView listView;

    Bitmap noPostImageBitmap;
    Bitmap noProfilePictureBitmap;

    Activity main = getActivity();

    String idObject;
    String userNamePosts;

    View root;
    ArrayList<PostClass> postsArrayList = new ArrayList<>();

    public interface ChangeToGridViewButtonOn {
        public void changeGridViewButtonOn();
    }

    ChangeToGridViewButtonOn changeToGridViewButtonOnDelegate;

    public void setChangeToGridViewButtonOnDelegate(ChangeToGridViewButtonOn changeToGridViewButtonOnDelegate) {
        this.changeToGridViewButtonOnDelegate = changeToGridViewButtonOnDelegate;
    }

    //********************************************************************************************************************
    boolean isPostsArrayUpdated = false;

    ListAdapter listAdapter;
    PullToRefreshListView listView;

    public void updatePostsArrayList(final ArrayList<PostClass> postsArrayListTemp, int WhichHomePage) {
        if (WhichHomePage == HOMO_PAGE) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for (Iterator<PostClass> it = postsArrayListTemp.iterator(); it.hasNext();) {
                            PostClass post = it.next();
                            if (post.isShouldSendBreedingOffers()) {
                                ArrayList<Animal> userAnimals = Parse_model.getInstance().getUserClass().getAnimals();
                                boolean shouldSeePost = false;
                                for (Animal animal : userAnimals) {
                                    if (animal.shouldSeePost(post)) {
                                        shouldSeePost = true;
                                    }
                                }

                                if (!shouldSeePost) {
                                    it.remove();
                                }
                            }
                        }

                        if (!isPostsArrayUpdated) {
                            // First time updating posts array list
//                        listAdapter = new ListAdapter(getActivity(), root);
//                        listView = (PullToRefreshListView) root.findViewById(R.id.list_container);

                            postsArrayList = postsArrayListTemp;
                            listView.setAdapter(listAdapter);
                            isPostsArrayUpdated = true;
                            root.findViewById(R.id.homeFragmentLoadingPanel).setVisibility(View.GONE);
                        } else {
                            // Have updated posts before
                            postsArrayList = postsArrayListTemp;
                            listAdapter.notifyDataSetChanged();
                            root.findViewById(R.id.homeFragmentLoadingPanel).setVisibility(View.GONE);
                        }

                        listView.onRefreshComplete();

                        if (HOMO_PAGE == 2) {
                            if (changeToGridViewButtonOnDelegate != null) {
                                changeToGridViewButtonOnDelegate.changeGridViewButtonOn();
                            }
                        }

                    }
                });

            }
        }

    }
//********************************************************************************************************************

    Parcelable state;

    @Override
    public void onPause() {
        state = listView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isFirstResume) {
            root.findViewById(R.id.homeFragmentLoadingPanel).setVisibility(View.GONE);
        }
        isFirstResume = false;

        if (listAdapter != null) {
//            root.findViewById(R.id.homeFragmentLoadingPanel).setVisibility(View.GONE);
            listView.setAdapter(listAdapter);

            if (state != null) {
                listView.requestFocus();
                listView.onRestoreInstanceState(state);
            }
        }

    }

    boolean isFirstResume = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("InstaShop");

        DEBUG.MSG(getClass(), "isAdmin = " + Parse_model.getInstance().getUserClass().isAdmin());

        getActivity().findViewById(R.id.home_page).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.search).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.like).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.camera).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.profile).setVisibility(View.VISIBLE);

        noPostImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
        noProfilePictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);

        listView = (PullToRefreshListView) root.findViewById(R.id.list_container);
        listAdapter = new ListAdapter(getActivity(), root);


        setPostsImHomePage();

        if (state != null) {
//            listView.requestFocus();
            listView.onRestoreInstanceState(state);
        }

        if (HOMO_PAGE == 1) {
            listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Home_Model.getInstance().setPostsArrayList(4, null, null);
                }
            });
        }


        listView.onRefreshComplete();

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        return root;
    }

    public class ListAdapter extends BaseAdapter implements View.OnClickListener {
        private Context mContext;

        View mainView;

        public ListAdapter(Context mContext, View mainView) {
            this.mContext = mContext;
            this.mainView = mainView;
        }

        public int getCount() {
            return postsArrayList.size();
        }

        public Object getItem(int position) {
            return postsArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.post_item, null);
            }


            final View finalConvertView = convertView;
            final PostClass currPost = postsArrayList.get(position);

            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView time = (TextView) convertView.findViewById(R.id.postTime);
            TextView mainText = (TextView) convertView.findViewById(R.id.mainText);
            ScalableImageView postImage = (ScalableImageView) convertView.findViewById(R.id.postImageScalable);
            final Button likeButton = (Button) convertView.findViewById(R.id.likeButton);
            final Button likeRedButton = (Button) convertView.findViewById(R.id.likedButton);
            final Button commentButton = (Button) convertView.findViewById(R.id.commentButton);

            if (currPost.isShouldSendBreedingOffers()) {
                convertView.findViewById(R.id.post_item_sponsored_text).setVisibility(View.VISIBLE);
            }

//********************************LIKE BUTTON***********************************************
            if (postsArrayList.get(position).getIsLiked()) {

                likeRedButton.setVisibility(View.VISIBLE);
                likeButton.setVisibility(View.GONE);
                likeButton.setText(postsArrayList.get(position).get_numOfLikes() + "Likes");
            } else {
                likeRedButton.setVisibility(View.GONE);
                likeButton.setVisibility(View.VISIBLE);
                likeButton.setText(postsArrayList.get(position).get_numOfLikes() + "Likes");
            }
            likeButton.setText(String.valueOf(currPost.get_numOfLikes()) + " Likes");
            likeRedButton.setText(String.valueOf(currPost.get_numOfLikes()) + " Likes");

//********************************DELETE BUTTON***********************************************
            Button delete = (Button) convertView.findViewById(R.id.delete);
            if (postsArrayList.get(position).get_userName().equals(Parse_model.getInstance().getUserClass().get_userName())) {
                delete.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
            }

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItemDialog(position);
                }
            });

//************************************HASH TAGS**************************************************

            setHashTagsInPost(currPost, convertView);

            final LinearLayout lm = (LinearLayout) convertView.findViewById(R.id.linear_main);
            String hash = postsArrayList.get(position).get_hashTag();
            if (hash == null)
                hash = "";
            if (hash.equals(""))
                lm.setVisibility(View.GONE);
            else
                lm.setVisibility(View.VISIBLE);
//************************************COMMENT BUTTON**************************************************

            // Setting comment button's on click listener
            final View finalConvertView1 = convertView;
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(finalConvertView1, postsArrayList.get(position).getObjectID());
                }
            });
            commentButton.setText(String.valueOf(currPost.get_numOfComments()) + " Comments");

//************************************PROFILE IMAGE**************************************************
            final TouchHighlightImageButton userPicture = (TouchHighlightImageButton) convertView.findViewById(R.id.userProfileImage);
            userPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zoomImageFromThumb(userPicture, postsArrayList.get(position).get_userProfilePicture());

                }
            });

            // Profile image setter
            Bitmap profilePicBitmap = currPost.get_userProfilePicture();
            if (profilePicBitmap != null) {
                //RoundImageClass roundProfilePicture = new RoundImageClass(profilePic);
                //userPicture.setBackground(roundProfilePicture);

                userPicture.setImageBitmap(profilePicBitmap);
                Log.e("Profile picture: ", "not NULL");
            } else {
                Log.e("Profile picture: ", "NULL");
                AtomicReference<PostClass> currPostProfileReference = new AtomicReference<>(postsArrayList.get(position));
                new LoadPostBitmap(currPostProfileReference, userPicture, noProfilePictureBitmap, 2);
                postsArrayList.get(position).set_userProfilePicture(currPostProfileReference.get().get_userProfilePicture());
                ModelSql.getInstance().updateProfilePicture(currPostProfileReference.get().get_userProfilePicture(), currPostProfileReference.get().getObjectID());
            }
//************************************POST IMAGE**************************************************
            //Post image setter
            Bitmap postPictureBitmap = postsArrayList.get(position).get_postPicture();
            if (postPictureBitmap != null) {
                postImage.setImageBitmap(postPictureBitmap);
                postImage.setVisibility(ScalableImageView.VISIBLE);
                Log.e("Post picture: ", "not NULL");
            } else {
                Log.e("Post picture: ", "NULL");
                AtomicReference<PostClass> currPostReference = new AtomicReference<>(postsArrayList.get(position));
                Bitmap noPostImageBitmapScaled = noPostImageBitmap;
                int postPictureWidth = currPostReference.get().getPicWidth();
                int postPictureHeight = currPostReference.get().getPicHeight();
                if (postPictureWidth > 0 && postPictureHeight > 0) {
//                    noPostImageBitmapScaled.setWidth(postPictureWidth);
//                    noPostImageBitmapScaled.setHeight(postPictureHeight);
                    noPostImageBitmapScaled = noPostImageBitmapScaled.createScaledBitmap(noPostImageBitmapScaled, postPictureWidth, postPictureHeight, true);
                }
                new LoadPostBitmap(currPostReference, postImage, noPostImageBitmapScaled, 1);
                postsArrayList.get(position).set_postPicture(currPostReference.get().get_postPicture());
//                ModelSql.getInstance().updatePostPicture(currPostReference.get().get_userProfilePicture(), currPostReference.get().getObjectID());
                postImage.setVisibility(ScalableImageView.VISIBLE);
            }

//************************************USER NAME**************************************************
            userName.setText(currPost.get_userName());
            Log.e("post number: " + position + " userName", currPost.get_userName());
//************************************TIME**************************************************
            time.setText(currPost.get_date());
            Log.e("post number: " + position + " time", currPost.get_date() + "");
//************************************MAIN TEXT**************************************************
            mainText.setText(currPost.get_mainString());
            Log.e("post number: " + position + " mainText", currPost.get_mainString());


            numOfLikes = postsArrayList.get(position).get_numOfLikes();


            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    numOfLikes = postsArrayList.get(position).get_numOfLikes();

                    likeRedButton.setText(numOfLikes + 1 + " Likes");
                    likeButton.setText(numOfLikes + 1 + " Likes");
                    postsArrayList.get(position).set_numOfLikes(numOfLikes + 1);
                    likeRedButton.setVisibility(View.VISIBLE);
                    likeButton.setVisibility(View.GONE);


                    String _objectPostID = postsArrayList.get(position).getObjectID();
                    String _currUserName = Parse_model.getInstance().getUserClass().get_userName();
                    String _currUseId = Parse_model.getInstance().getUserClass().get_userId();

                    NotificationsOfPost notificationPost = new NotificationsOfPost();
                    notificationPost.setUserName_getNotification(postsArrayList.get(position).get_userName());
                    notificationPost.setRead(false);
                    notificationPost.setRecentAction("L");
                    notificationPost.setUserName_doAction(_currUserName);
                    notificationPost.setPostIdLiked(postsArrayList.get(position).getObjectID());
                    notificationPost.setPostPicture(postsArrayList.get(position).get_postPicture());
                    notificationPost.setProfilePicture(Parse_model.getInstance().getUserClass().get_userPic());

                    String dateNow = new CurrentDateTime().getDateTime();
                    notificationPost.set_date(dateNow);


                    new Like(finalConvertView, numOfLikes, notificationPost, _currUseId, _objectPostID).execute(_objectPostID, _currUserName);

                }
            });

            likeRedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numOfLikes = postsArrayList.get(position).get_numOfLikes();


                    likeRedButton.setText(numOfLikes - 1 + " Likes");
                    likeButton.setText(numOfLikes - 1 + " Likes");
                    postsArrayList.get(position).set_numOfLikes(numOfLikes - 1);
                    likeRedButton.setVisibility(View.GONE);
                    likeButton.setVisibility(View.VISIBLE);


                    String _objectPostID = postsArrayList.get(position).getObjectID();
                    String _currUserName = Parse_model.getInstance().getUserClass().get_userName();
                    String _currUseId = Parse_model.getInstance().getUserClass().get_userId();

                    NotificationsOfPost notificationPost = new NotificationsOfPost();
                    notificationPost.setUserName_getNotification(postsArrayList.get(position).get_userName());
                    notificationPost.setRead(false);
                    notificationPost.setRecentAction("L");
                    notificationPost.setUserName_doAction(_currUserName);
                    notificationPost.setPostIdLiked(postsArrayList.get(position).getObjectID());
                    notificationPost.setPostPicture(postsArrayList.get(position).get_postPicture());
                    notificationPost.setProfilePicture(Parse_model.getInstance().getUserClass().get_userPic());


                    new Like(finalConvertView, numOfLikes, notificationPost, _currUseId, _objectPostID).execute(_objectPostID, _currUserName);

                }
            });

            userName.setClickable(true);

            if (currPost.get_userName().equals(Parse_model.getInstance().getUserClass().get_userName())) {
                userName.setClickable(false);
            }
            if (HOMO_PAGE == 3) {
                userName.setClickable(true);
            }

            //is clickable checker
            boolean isUserNameClickable = userName.isClickable();

            if (userName.isClickable()) {
                userName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap userPicture = postsArrayList.get(position).get_userProfilePicture();
                        String userNameString = postsArrayList.get(position).get_userName();
                        ((MainActivity) getActivity()).performUserNameClick(userNameString, userPicture);

                    }
                });
            }


            return convertView;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void setHashTagsInPost(final PostClass currPost, View view) {
        final LinearLayout lm = (LinearLayout) view.findViewById(R.id.linear_main);
        if (((LinearLayout) lm).getChildCount() > 0)
            ((LinearLayout) lm).removeAllViews();

        final int maxCharsInARow = 25;

        String hashTags = currPost.get_hashTag();
        if (hashTags != null)
            if (!hashTags.equals("")) {

                String[] hashTagsArray = hashTags.split("#");

                //take every word and makes a char array
                ArrayList<char[]> charsList = new ArrayList<>();
                for (String currString : hashTagsArray) {
                    if (!currString.equals("")) {
                        charsList.add(currString.toCharArray());
                    }

                }
                //calculate how much words should be in a row by maxCharsInARow
                ArrayList<Integer> numOfWordsInARow = new ArrayList<>();
                int wordsCounterInARaw = 0;
                int charsCounter = 0;
                for (char[] currCharArray : charsList) {
                    charsCounter = charsCounter + currCharArray.length;
                    if (charsCounter > 30) {
                        numOfWordsInARow.add(wordsCounterInARaw);
                        wordsCounterInARaw = 1;
                        charsCounter = currCharArray.length;
                    } else {
                        wordsCounterInARaw++;
                    }
                }
                numOfWordsInARow.add(wordsCounterInARaw);

                int hashTagsArrayLengh = hashTagsArray.length;
                int hashTagsArrayCounter = 0;
                for (int j = 0; j < numOfWordsInARow.size() + 1; j++) {


                    LinearLayout rowLinearLayout = new LinearLayout(view.getContext());
                    rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    if (j == numOfWordsInARow.size()) {
                        final TextView tv = new TextView(view.getContext());
                        tv.setText("");
                        rowLinearLayout.addView(tv);

                        lm.addView(rowLinearLayout);
                    } else {
                        for (int i = 0; i < numOfWordsInARow.get(j) + 1; i++) {


                            // addPost text view
                            String word = hashTagsArray[hashTagsArrayCounter + 1];
//                        if(hashTagsArrayCounter < hashTagsArrayLengh){
//                            hashTagsArrayCounter++;
//                        }

                            final TextView tv = new TextView(view.getContext());
                            if (i == 0) {
                                tv.setText("      ");
                            } else {
                                tv.setText("#" + String.valueOf(word) + " ");
                                hashTagsArrayCounter++;
                                tv.setTypeface(null, Typeface.BOLD_ITALIC);
                                tv.setTextColor(Color.parseColor("#2FB9F5"));
                                tv.setTextSize(13);
                            }
                            tv.setId(i);
                            rowLinearLayout.addView(tv);


                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    HashTagFragment hashTagFragment = new HashTagFragment();
                                    int i = tv.getId();
                                    View n = tv.findViewById(i);
                                    TextView currentTextView = (TextView) n.findViewById(i);

                                    if (postInfoToHashDelegates != null) {
                                        postInfoToHashDelegates.infoToHashFragmentFunction(currentTextView.getText().toString(), currPost);
                                    }
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, hashTagFragment).addToBackStack(null).commit();
                                }
                            });
                        }


                        lm.addView(rowLinearLayout);
                    }
                }

                lm.setVisibility(View.VISIBLE);

            } else
                lm.setVisibility(View.GONE);
    }

    public static class Like extends AsyncTask<String, String, Integer> {

        View view;
        int numOfLikes;
        NotificationsOfPost notification;
        String currentUserId;
        boolean isRemoved;
        String postId;

        public Like(View view, int numOfLikes, NotificationsOfPost notification, String currentUserId, String _objectPostID) {
            this.view = view;
            this.numOfLikes = numOfLikes;
            this.notification = notification;
            this.currentUserId = currentUserId;
            isRemoved = false;
            postId = _objectPostID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            isRemoved = false;


        }

        @Override
        protected Integer doInBackground(String... params) {
            if (!Parse_model.getInstance().isLikedPostBefore(params[0], params[1])) {
                Parse_model.getInstance().addLikeToPost(params[0], params[1], notification.getUserName_getNotification(), currentUserId);
                Parse_model.getInstance().addLikeNotification(notification);

            } else {
                Parse_model.getInstance().removeLikeToPost(params[0], params[1], currentUserId);
                isRemoved = true;

            }
            int numOfLikesUpdated = Parse_model.getInstance().getNumOfLikes(params[0]);

            return numOfLikesUpdated;

        }

        protected void onPostExecute(Integer result) {

            Button isLikedRed = (Button) view.findViewById(R.id.likedButton);
            Button isLikes = (Button) view.findViewById(R.id.likeButton);

            isLikes.setText(result + " Likes");
            isLikedRed.setText(result + " Likes");


            if (isRemoved) {
                isLikedRed.setVisibility(View.GONE);
                isLikes.setVisibility(View.VISIBLE);
            }

        }
    }

    //****************************************************************************
    // show comment dialog function
    void showDialog(View view, String _postID) {
        // Create the fragment and show it as a dialog.
        dialogFragment = CommentDialogFragment.newInstance(view, _postID);
        dialogFragment.show(getFragmentManager(), "dialog");
    }
    //*******************************************************************************

    public void removeItemDialog(final int removedItemPosition) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Are you sure you want to remove this post?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //erase from parse
                new deleteFromDB(postsArrayList.get(removedItemPosition).getObjectID()).execute();

                //erase the current post in list
                postsArrayList.remove(removedItemPosition);
                listAdapter.notifyDataSetChanged();
                Log.e("editPostsButton", "PRESSED");
            }
        });

        alertDialog.show();
    }

    protected class deleteFromDB extends AsyncTask<Void, Void, Boolean> {
        String postID;

        public deleteFromDB(String postID) {
            this.postID = postID;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ModelSql.getInstance().removePostByID(postID);
            return Parse_model.getInstance().removePostById(postID);
        }
    }


    private void zoomImageFromThumb(final com.example.jpet.Home.TouchHighlightImageButton profileImageView, Bitmap profileBitmap) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) root.findViewById(R.id.expanded_image);
        expandedImageView.setImageBitmap(profileBitmap);

        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        profileImageView.getGlobalVisibleRect(startBounds);
        root.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        profileImageView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        profileImageView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        profileImageView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }


    public ArrayList<PostClass> getPostsByFollowers(ArrayList<PostClass> postsArrayList, ArrayList<String> followersArray) {

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

    public void setPostsImHomePage() {
        Home_Model.getInstance().setSetPostsInHomePageDelegate(new Home_Model.SetPostsInHomePage() {
            @Override
            public void setPosts(ArrayList<PostClass> postsArrayList, int WhichHomePage) {
                updatePostsArrayList(postsArrayList, WhichHomePage);
                DEBUG.trace(7);
            }
        });
    }


}