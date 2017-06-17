package com.example.jpet.profile;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jpet.Camera.PostClass;
import com.example.jpet.CurrentDateTime;
import com.example.jpet.DB_Model.Models.Home_Model;
import com.example.jpet.DB_Model.ParseDB.Parse_Animals;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.DEBUG;
import com.example.jpet.Home.HomeFragment;
import com.example.jpet.Home.LoadPostBitmap;
import com.example.jpet.LikeAndFollowing.NotificationsOfPost;
import com.example.jpet.MainActivity;
import com.example.jpet.Network.Network;
import com.example.jpet.R;
import com.example.jpet.Search.SearchFragment;
import com.example.jpet.fragments.AnimalsFragment;
import com.example.jpet.objects.Animal;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    com.example.jpet.Home.TouchHighlightImageButton viewImage;

    RoundImageClass roundImageClass;

    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();

    DialogFragment dialogFragment;

    TextView numOfPostsTextView;
    TextView userNameTitle;
    TextView followerNum;
    TextView followingNum;

    Button animalsPageButton;

    Button followButton;
    Button editProfileButton;
    Button followingButton;
    Button feedBtn;
    Button galleryBtn;
    Button feedBtn_grey;
    Button galleryBtn_grey;

    Bitmap noPostImageBitmap;
    Bitmap noProfilePictureBitmap;
    ImageAdapter adapter;
    GridView gridview2;
    ArrayList<PostClass> postsArray = new ArrayList<>();
    FrameLayout gridLayout;


    /*
    There are 2 points of view to this fragment,
    when i enter to my profile, or when someone else enters into my profile
    myProfile says which case it is.
    true - i enter to my profile
    false - someone else enter to me profile
     */
    static boolean myProfile = true;
    static String userNamePosts;
    static Bitmap userNamePicture;


    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //keep track of gallery intent
    final int GALLERY = 3;
    //captured picture uri
    private Uri picUri;


    public static void setMyProfile(boolean Profile, String userName, Bitmap userPicture) {
        myProfile = Profile;
        userNamePicture = userPicture;
        userNamePosts = userName;
    }

    public void changeGridButtonOn() {
        galleryBtn_grey.setClickable(true);
        galleryBtn.setClickable(true);
    }

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Showing the fragment's optioned actionbar
        setHasOptionsMenu(true);


        userNameTitle = (TextView) root.findViewById(R.id.user_name_text);
        viewImage = (com.example.jpet.Home.TouchHighlightImageButton) root.findViewById(R.id.profileViewImage);

        followingNum = (TextView) root.findViewById(R.id.followingNum_textView);
        followerNum = (TextView) root.findViewById(R.id.followers_num);
        numOfPostsTextView = (TextView) root.findViewById(R.id.numOfPosts_TextView);

        editProfileButton = (Button) root.findViewById(R.id.editProfile_button);
        followButton = (Button) root.findViewById(R.id.follow_btn);
        followingButton = (Button) root.findViewById(R.id.following_btn);

        feedBtn = (Button) root.findViewById(R.id.feed_btn);
        galleryBtn = (Button) root.findViewById(R.id.gallery_btn);
        galleryBtn_grey = (Button) root.findViewById(R.id.gallery_btn_grey);
        feedBtn_grey = (Button) root.findViewById(R.id.feed_btn_grey);

        animalsPageButton = (Button) root.findViewById(R.id.animals_page_button);

        feedBtn.setVisibility(View.VISIBLE);
        feedBtn_grey.setVisibility(View.GONE);
        galleryBtn_grey.setVisibility(View.VISIBLE);
        galleryBtn.setVisibility(View.GONE);

        gridLayout = (FrameLayout) root.findViewById(R.id.gridLayoutContainer);
        //    gridview2 = (GridView)root.findViewById(R.id.gridView);
        root.findViewById(R.id.profileFragmentLoadingPanel).setVisibility(View.GONE);

        animalsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.runOnThreadWithProgressDialog(getActivity(), new Network.NetworkCallBack() {
                    @Override
                    public Object doInBackground() {
                        ArrayList<Animal> animals = Parse_Animals.getAllAnimalsByEmail(Parse_model.getInstance().getUserClass().get_email());
                        Parse_model.getInstance().getUserClass().setAnimals(animals);
                        DEBUG.MSG(getClass(), "animals size = " + animals.size());
                        return true;
                    }

                    @Override
                    public void onPostExecute(Object object) {
                        super.onPostExecute(object);
                        ((MainActivity)getActivity()).openNewFrag(new AnimalsFragment());
                    }
                });


            }
        });

        new IsFollowOrNotSetButton(userNamePosts, root).execute();


        boolean isViewImageClickable = viewImage.isClickable();

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);


        if (myProfile) {
            //Case 1: i enter into my profile

            animalsPageButton.setVisibility(View.VISIBLE);

            //sets current user's profile image
            if (Parse_model.getInstance().getUserClass().get_userPic() == null) {
                Bitmap noProfilePicture = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);
                roundImageClass = new RoundImageClass(noProfilePicture);
                viewImage.setImageDrawable(roundImageClass);
            } else {
                RoundImageClass roundImage = new RoundImageClass(Parse_model.getInstance().getUserClass().get_userPic());
                viewImage.setImageDrawable(roundImage);
            }


            //sets my posts
            userNamePosts = Parse_model.getInstance().getUserClass().get_userName();

            //sets my user name
            userNameTitle.setText(userNamePosts);

            //sets edit button visibility on
            editProfileButton.setVisibility(View.VISIBLE);

            // sets edit post button's visibility ON
        } else {
            //Case 2: someone else enter into my profile

            animalsPageButton.setVisibility(View.INVISIBLE);

            //sets the profile picture
            if (userNamePicture == null) {
                Bitmap noProfilePicture = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);
                roundImageClass = new RoundImageClass(noProfilePicture);
                viewImage.setImageDrawable(roundImageClass);
            } else {
                RoundImageClass roundImage = new RoundImageClass(userNamePicture);
                viewImage.setImageDrawable(roundImage);
            }

            //gets and sets all the posts of the owner of the profile picture
            //happening after this "if" below


            //sets user name
            userNameTitle.setText(userNamePosts);
             ((MainActivity)getActivity()).getSupportActionBar().setTitle(userNamePosts);


            //sets follow button visibility on
            if (!userNamePosts.equals(Parse_model.getInstance().getUserClass().get_userName())) {
                followButton.setVisibility(View.VISIBLE);
                followingButton.setVisibility(View.GONE);
            } else {
                followButton.setVisibility(View.GONE);
                followingButton.setVisibility(View.GONE);
                editProfileButton.setVisibility(View.VISIBLE);
            }
        }

        //open the pictures fragment
        //new openPhotoGallery().execute();


        //sets followers number by current user
        new getNumFollowersByUserName(userNamePosts, followerNum).execute();

        //sets following number by current user
        new getNumFollowingByUserName(userNamePosts, followingNum).execute();

        //sets num of posts by the enters user
        new getNumOfPostsByUserName(userNamePosts, numOfPostsTextView).execute();




        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myProfile){
                    //choosing a new profile pic
                    selectImage();
                }else{
                    //zooming profile pic
                    zoomImageFromThumb(viewImage, userNamePicture);
                }


            }
        });


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _currUserName = Parse_model.getInstance().getUserClass().get_userName();
                String _currentUserId = Parse_model.getInstance().getUserClass().get_userId();
                String _userIFollowName = String.valueOf(userNamePosts);

                followingButton.setVisibility(View.VISIBLE);
                followButton.setVisibility(View.GONE);
                new FollowingNewUser(_userIFollowName).execute(_currUserName, _currentUserId);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _currUserName = Parse_model.getInstance().getUserClass().get_userName();
                String _currentUserId = Parse_model.getInstance().getUserClass().get_userId();
                String _userIFollowName = String.valueOf(userNamePosts);

                followingButton.setVisibility(View.GONE);
                followButton.setVisibility(View.VISIBLE);
                new RemoveFollowing(_userIFollowName).execute(_currUserName, _currentUserId);
            }
        });

        followerNum = (TextView) root.findViewById(R.id.followers_num);
        String t = String.valueOf(Parse_model.getInstance().getNumOfFollowers(userNamePosts));
        followerNum.setText(t);


        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).profileToEditFragment();
            }
        });


        feedBtn_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedBtn.setVisibility(View.VISIBLE);
                feedBtn_grey.setVisibility(View.GONE);
                galleryBtn_grey.setVisibility(View.VISIBLE);
                galleryBtn.setVisibility(View.GONE);

                gridLayout.setVisibility(View.VISIBLE);

//                view.findViewById(R.id.searchFragmentLoadingPanel).setVisibility(View.GONE);
                openUserPostFragment();


            }
        });

        galleryBtn_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //****************************** set the gellery side ******************************************
                feedBtn.setVisibility(View.GONE);
                feedBtn_grey.setVisibility(View.VISIBLE);
                galleryBtn_grey.setVisibility(View.GONE);
                galleryBtn.setVisibility(View.VISIBLE);


                openUserGalleryFragment();
                //*********************************************************************************************
            }
        });


        followingNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFollowing(userNamePosts);

            }
        });

        followerNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFollowers(userNamePosts);
            }
        });

        //open user's posts fragment by feeds
        openUserPostFragment();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.profile_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout_action: {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Are you sure you want to LogOut?");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).openLoggingPage();
                        Parse_model.getInstance().getUserClass().set_email("");
                        Parse_model.getInstance().getUserClass().set_password("");
                        Parse_model.getInstance().getUserClass().set_userName("");
                        Parse_model.getInstance().getUserClass().set_userId("");
                        Parse_model.getInstance().getUserClass().set_isOn(false);
                        Parse_model.getInstance().getUserClass().set_userPic(null);
                    }
                });

                alertDialog.show();

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected class getNumOfPostsByUserName extends AsyncTask<Void, Void, Integer> {

        String userName;
        TextView numOfPostsTextView;

        public getNumOfPostsByUserName(String userName, TextView numOfPostsTextView) {
            this.userName = userName;
            this.numOfPostsTextView = numOfPostsTextView;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return Parse_model.getInstance().getNumOfPostsByUserName(userName);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == null) {
                numOfPostsTextView.setText("0");
            } else {
                numOfPostsTextView.setText(String.valueOf(integer));
            }
        }
    }

    protected class getNumFollowersByUserName extends AsyncTask<Void, Void, Integer> {

        String userName;
        TextView numOfFollowersTextView;

        public getNumFollowersByUserName(String userName, TextView numOfFollowersTextView) {
            this.userName = userName;
            this.numOfFollowersTextView = numOfFollowersTextView;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return Parse_model.getInstance().getNumOfFollowers(userName);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == null) {
                numOfFollowersTextView.setText("0");
            } else {
                numOfFollowersTextView.setText(String.valueOf(integer));
            }

        }
    }


    protected class getNumFollowingByUserName extends AsyncTask<Void, Void, Integer> {

        String userName;
        TextView numOfFollowingTextView;

        public getNumFollowingByUserName(String userName, TextView numOfFollowersTextView) {
            this.userName = userName;
            this.numOfFollowingTextView = numOfFollowersTextView;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return Parse_model.getInstance().getNumOfFollowing(userName);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            numOfFollowingTextView.setText(String.valueOf(integer));

        }
    }


    protected class IsFollowOrNotSetButton extends AsyncTask<Void, Void, Boolean> {

        String userName;
        View view;

        public IsFollowOrNotSetButton(String userName, View view) {
            this.userName = userName;
            this.view = view;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Parse_model.getInstance().isFollowedUserBefore(Parse_model.getInstance().getUserClass().get_userName(), userName);
        }

        @Override
        protected void onPostExecute(Boolean integer) {
            super.onPostExecute(integer);
            followButton = (Button) view.findViewById(R.id.follow_btn);
            followingButton = (Button) view.findViewById(R.id.following_btn);

            if (integer) {
                followingButton.setVisibility(View.VISIBLE);
                followButton.setVisibility(View.GONE);
            } else {
                followingButton.setVisibility(View.GONE);
                followButton.setVisibility(View.VISIBLE);
            }

        }
    }

    //open followers dialog
    void showDialogFollowers(String userName) {
        // Create the fragment and show it as a dialog.
        dialogFragment = FollowerDialogFragment.newInstance(userName);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    //open followers dialog
    void showDialogFollowing(String userName) {
        // Create the fragment and show it as a dialog.
        dialogFragment = FollowingDialogFragment.newInstance(userName);
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        //use standard intent to capture an image
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //we will handle the returned data in onActivityResult
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    } catch (ActivityNotFoundException anfe) {
                        //display an error message
                        String errorMessage = "Whoops - your device doesn't support capturing images!";
//                            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//                            toast.show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {// || requestCode == GALLERY) {
                //get the Uri for the captured image

                Bundle extras = data.getExtras();
                //get the cropped bitmap
                userNamePicture = extras.getParcelable("data");

//                _thePic = decodeSampledBitmapFromPath(picturePath, 100, 100);
                Parse_model.getInstance().getUserClass().set_userPic(userNamePicture);
                new setProfilePic().execute(userNamePicture);

                //display the returned cropped image
                viewImage.setImageBitmap(userNamePicture);

//                picUri = data.getData();
                //carry out the crop operation
//                performCrop();
            }
            //user is returning from cropping the image
            else if (requestCode == PIC_CROP) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                userNamePicture = extras.getParcelable("data");

//                _thePic = decodeSampledBitmapFromPath(picturePath, 100, 100);
                Parse_model.getInstance().getUserClass().set_userPic(userNamePicture);
                new setProfilePic().execute(userNamePicture);

                //display the returned cropped image
                viewImage.setImageBitmap(userNamePicture);

            }
    }
    }

    /**
     * Helper method to carry out crop operation
     */
    private void performCrop() {
        //take care of exceptions
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        //respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected class setProfilePic extends AsyncTask<Bitmap, Void, Bitmap> {

        ProgressDialog progressDialog;


        protected void onPreExecute() {
            Bitmap noProfilePicture = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);
            roundImageClass = new RoundImageClass(noProfilePicture);
            viewImage.setImageDrawable(roundImageClass);

        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            Parse_model.getInstance().addProfilePicture(params[0]);
            return params[0];
        }

        protected void onPostExecute(Bitmap result) {
            RoundImageClass roundImage = new RoundImageClass(Parse_model.getInstance().getUserClass().get_userPic());
            viewImage.setImageDrawable(roundImage);

        }

    }

    public class FollowingNewUser extends AsyncTask<String, Void, String> {

        String userIFollowName;
        int followersCounter;
        Bitmap followerPic;

        public FollowingNewUser(String userIFollowName) {
            this.userIFollowName = userIFollowName;
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
            TextView followCounter = (TextView) getActivity().findViewById(R.id.followers_num);
            followersCounter = getFollowersCounter() + 1;
            followCounter.setText(String.valueOf(followersCounter));

        }

        @Override
        protected String doInBackground(String... params) {

            if (!Parse_model.getInstance().isFollowedUserBefore(params[0], userIFollowName)) {
                Parse_model.getInstance().addFollowerToUser(params[0], params[1], userIFollowName, followerPic);

                NotificationsOfPost notification = new NotificationsOfPost();
                notification.setUserName_getNotification(userIFollowName);
                notification.setRead(false);
                notification.setRecentAction("F");
                notification.setUserName_doAction(params[0]);
                notification.setProfilePicture(followerPic);
                notification.set_date(new CurrentDateTime().getDateTime());

                Parse_model.getInstance().addFollowNotification(notification);


            } else {
                Parse_model.getInstance().removeFollowFromUser(userIFollowName, params[0]);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }


    public class RemoveFollowing extends AsyncTask<String, Void, String> {

        String userIFollowName;
        int followersCounter;
        Bitmap followerPic;

        public RemoveFollowing(String userIFollowName) {
            this.userIFollowName = userIFollowName;
            followersCounter = Parse_model.getInstance().getNumOfFollowers(userIFollowName);
            followerPic = Parse_model.getInstance().getUserClass().get_userPic();
        }

        public int getFollowersCounter() {
            followersCounter = Parse_model.getInstance().getNumOfFollowers(userIFollowName);
            return followersCounter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            TextView followCounter = (TextView) getActivity().findViewById(R.id.followers_num);
            followersCounter = followersCounter - 1;
            followCounter.setText(String.valueOf(followersCounter));

        }

        @Override
        protected String doInBackground(String... params) {

            Parse_model.getInstance().removeFollowFromUser(userIFollowName, Parse_model.getInstance().getUserClass().get_userName());
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }


    public void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Failed connecting with parse. please try again later.")
                .setCancelable(false)
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //****************************************************************************
    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);


        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        if (input != null) {
            Log.d("decode pic:", "is NOT null, which means it got the pics");

            byte[] decodedByte = Base64.decode(input, 0);
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        } else {
            // if(dialogDelegate!=null){
            //    dialogDelegate.alert();
            //  }
            return null;

        }

    }

//****************************************************************************

    //******************************************************************
    //open new frag functions
    public void openNewFrag(Fragment newFrag) {
        getFragmentManager().beginTransaction().add(R.id.gridLayoutContainer, newFrag).commit();

    }

    boolean isLoadedPostsListBefore = false;

    //open user's posts fragment
    public void openUserPostFragment() {

        homeFragment.setHomoPage(2);
        if (!isLoadedPostsListBefore) {
            Home_Model.getInstance().setPostsArrayList(2, userNamePosts, null);
            isLoadedPostsListBefore = true;
        }
        getFragmentManager().beginTransaction().replace(R.id.gridLayoutContainer, homeFragment).commit();
    }

    //open gallery user's photo
    public void openUserGalleryFragment() {

        searchFragment.setSearchPage(2, userNamePosts);
        getFragmentManager().beginTransaction().replace(R.id.gridLayoutContainer, searchFragment).commit();
    }

    //******************************************************************

    private void zoomImageFromThumb(final com.example.jpet.Home.TouchHighlightImageButton profileImageView, Bitmap profileBitmap) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) root.findViewById(R.id.profile_expanded_image);
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

            if (postsArray.get(position).get_postPicture() == null) {
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


}
