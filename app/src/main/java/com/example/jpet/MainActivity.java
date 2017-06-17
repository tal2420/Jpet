package com.example.jpet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jpet.Camera.CameraFragment;
import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.ModelSql;
import com.example.jpet.DB_Model.Models.Home_Model;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.HashTags.HashTagFragment;
import com.example.jpet.Home.HomeFragment;
import com.example.jpet.LikeAndFollowing.LikeAndFollowingFragment;
import com.example.jpet.Search.SearchFragment;
import com.example.jpet.fragments.AnimalRegistrationFragment;
import com.example.jpet.fragments.AnimalsFragment;
import com.example.jpet.loginFragment.LoginFragment;
import com.example.jpet.loginFragment.SignUpFragment;
import com.example.jpet.profile.ProfileFragment;
import com.example.jpet.profile.UpdateUserFragment;
import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private static final int PERMISSION_REQUEST_CODE = 5245245;
    static int INDEX = R.string.tab_index;
    int currentTab = 0;
    LikeAndFollowingFragment likeAndFollowingFragment = new LikeAndFollowingFragment();
    HashTagFragment hashTagFragment = new HashTagFragment();

    ProfileFragment profileFragment = new ProfileFragment();
    ProfileFragment otherProfileFragment = new ProfileFragment();

    AnimalRegistrationFragment animalRegistrationFragment = new AnimalRegistrationFragment();

    static HomeFragment homeFragment = new HomeFragment();
    static SearchFragment searchFragment = new SearchFragment();
    static CameraFragment cameraFragment = new CameraFragment();
    static LoginFragment loginFragment = new LoginFragment();
    static SignUpFragment signUpFragment = new SignUpFragment();
    static UpdateUserFragment updateUserFragment = new UpdateUserFragment();
    static AnimalsFragment animalsFragment = new AnimalsFragment();

    Fragment currentFragment;
    Context mContext;

    Bitmap noPostImageBitmap;
    Bitmap noProfilePictureBitmap;

    Context context = this;

    public static HomeFragment getHomeFragment() {
        return homeFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        homeFragment.setHomoPage(1);

        noPostImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
        noProfilePictureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_picture);

        final Button home;
        final Button search;
        final Button camera;
        final Button notification;
        final Button profile;
        final Button home1;
        final Button search1;
        final Button camera1;
        final Button notification1;
        final Button profile1;


        //init the main buttons
        home = (Button) findViewById(R.id.home_page);
        search = (Button) findViewById(R.id.search);
        camera = (Button) findViewById(R.id.camera);
        notification = (Button) findViewById(R.id.like);
        profile = (Button) findViewById(R.id.profile);
        profile1 = (Button) findViewById(R.id.profile1);
        profile1.setTag(INDEX, 4);

        search1 = (Button) findViewById(R.id.search1);
        search1.setTag(INDEX, 1);

        camera1 = (Button) findViewById(R.id.camera1);
        camera1.setTag(INDEX, 2);

        notification1 = (Button) findViewById(R.id.like1);
        notification1.setTag(INDEX, 3);

        home1 = (Button) findViewById(R.id.home_page1);
        home1.setTag(INDEX, 0);


        profile.setVisibility(View.GONE);
        profile1.setVisibility(View.VISIBLE);

        search.setVisibility(View.GONE);
        search1.setVisibility(View.VISIBLE);

        camera.setVisibility(View.GONE);
        camera1.setVisibility(View.VISIBLE);

        notification.setVisibility(View.GONE);
        notification1.setVisibility(View.VISIBLE);

        home.setVisibility(View.VISIBLE);
        home1.setVisibility(View.GONE);


//        requestWindowFeature(FEA)


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(47, 185, 245)));
        }
        //**************listeners****************

        home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewFrag(homeFragment, leftOrRight((int) home1.getTag(INDEX)));
                currentFragment = homeFragment;

                homeFragment.setHomoPage(1);

                currentTab = 0;
                home.setVisibility(View.VISIBLE);
                home1.setVisibility(View.GONE);

                profile.setVisibility(View.GONE);
                profile1.setVisibility(View.VISIBLE);

                search.setVisibility(View.GONE);
                search1.setVisibility(View.VISIBLE);

                camera.setVisibility(View.GONE);
                camera1.setVisibility(View.VISIBLE);

                notification.setVisibility(View.GONE);
                notification1.setVisibility(View.VISIBLE);
            }
        });

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewFrag(searchFragment, leftOrRight((int) search1.getTag(INDEX)));
                currentTab = 1;
                currentFragment = searchFragment;

                search.setVisibility(View.VISIBLE);
                search1.setVisibility(View.GONE);

                profile.setVisibility(View.GONE);
                profile1.setVisibility(View.VISIBLE);

                home.setVisibility(View.GONE);
                home1.setVisibility(View.VISIBLE);

                camera.setVisibility(View.GONE);
                camera1.setVisibility(View.VISIBLE);

                notification.setVisibility(View.GONE);
                notification1.setVisibility(View.VISIBLE);


            }
        });

        camera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewFrag(cameraFragment, leftOrRight((int) camera1.getTag(INDEX)));
                currentFragment = cameraFragment;
                currentTab = 2;

                camera.setVisibility(View.VISIBLE);
                camera1.setVisibility(View.GONE);

                profile.setVisibility(View.GONE);
                profile1.setVisibility(View.VISIBLE);

                home.setVisibility(View.GONE);
                home1.setVisibility(View.VISIBLE);

                search.setVisibility(View.GONE);
                search1.setVisibility(View.VISIBLE);

                notification.setVisibility(View.GONE);
                notification1.setVisibility(View.VISIBLE);
            }
        });

        notification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewFrag(likeAndFollowingFragment, leftOrRight((int) notification1.getTag(INDEX)));
                currentFragment = likeAndFollowingFragment;
                currentTab = 3;

                notification.setVisibility(View.VISIBLE);
                notification1.setVisibility(View.GONE);

                profile.setVisibility(View.GONE);
                profile1.setVisibility(View.VISIBLE);

                home.setVisibility(View.GONE);
                home1.setVisibility(View.VISIBLE);

                camera.setVisibility(View.GONE);
                camera1.setVisibility(View.VISIBLE);

                search.setVisibility(View.GONE);
                search1.setVisibility(View.VISIBLE);
            }
        });

        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment = new ProfileFragment();
                String myUserName = Parse_model.getInstance().getUserClass().get_userName();
                Bitmap myProfilePicture = Parse_model.getInstance().getUserClass().get_userPic();
                profileFragment.setMyProfile(true, myUserName, myProfilePicture);
                openNewFrag(profileFragment, leftOrRight((int) profile1.getTag(INDEX)));
                currentFragment = profileFragment;
                currentTab = 4;


                profile.setVisibility(View.VISIBLE);
                profile1.setVisibility(View.GONE);

                search.setVisibility(View.GONE);
                search1.setVisibility(View.VISIBLE);

                camera.setVisibility(View.GONE);
                camera1.setVisibility(View.VISIBLE);

                notification.setVisibility(View.GONE);
                notification1.setVisibility(View.VISIBLE);

                home.setVisibility(View.GONE);
                home1.setVisibility(View.VISIBLE);


            }
        });

        //searchFragment.setPostsArray();

        parseInit();

        ModelSql.getInstance().init(this);

        //new getUserID().execute();


        // getActionBar().hide();

        //open the first fragment
        openNewFrag(loginFragment);
//        openNewFrag(loginFragment);
//        openNewFrag(animalRegistrationFragment);
//        openNewFrag(animalsFragment);


        LoginToHomePage();

        SignUpToHomePage();

        LoginToSignUpPage();

        SignUpToLoginPage();

        postToUserProfile();

        cameraFragmentToHomePage();

        setProfilePageGalleryButtonOn();

        openFragAndPassInfo();

        notificationFragToUserProfileFrag();

    }

    @Override
    protected void onResume() {
        super.onResume();
        askForPermissions();
    }

    public void askForPermissions() {
        confirmPermissions(new MainActivity.PermissionRequestCallback() {
            @Override
            public void onRequestResults(boolean hasPermissions, ArrayList<String> rejectedPermissions) {
                if (!hasPermissions) {
                    new AlertDialog.Builder(context)
                            .setTitle("הרשאות")
                            .setMessage("אנא אשר הרשאות על מנת להשתמש באפליקציה")
                            .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    askForPermissions();
                                }
                            })
                            .setNegativeButton("לך אל הגדרות", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();

                }
            }
        }, android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void hidePanelBar() {
        findViewById(R.id.home_page).setVisibility(View.GONE);
        findViewById(R.id.search).setVisibility(View.GONE);
        findViewById(R.id.like).setVisibility(View.GONE);
        findViewById(R.id.camera).setVisibility(View.GONE);
        findViewById(R.id.profile).setVisibility(View.GONE);
        findViewById(R.id.home_page1).setVisibility(View.GONE);
        findViewById(R.id.search1).setVisibility(View.GONE);
        findViewById(R.id.like1).setVisibility(View.GONE);
        findViewById(R.id.camera1).setVisibility(View.GONE);
        findViewById(R.id.profile1).setVisibility(View.GONE);

        findViewById(R.id.bar_buttons).setVisibility(View.GONE);
    }

    private int leftOrRight(int index) {
        //0 left
        //1 right
        if (index > currentTab)
            return 1;
        else
            return 0;
    }

    public void openFragAndPassInfo() {
        HomeFragment.setTextToFragment2Delegate(new HomeFragment.postToHashFragment() {
            @Override
            public void infoToHashFragmentFunction(String text, PostClass post) {
                HashTagFragment.setInfo(text, post);
                openNewFrag(hashTagFragment, 3);
            }
        });
    }

    public void notificationFragToUserProfileFrag() {
        LikeAndFollowingFragment.setnotificationFragToUserProfileDelegate(new LikeAndFollowingFragment.userPostToUserProfileInterface() {
                                                                              @Override
                                                                              public void userPostToUserProfile(String userName, Bitmap userPicture) {
                                                                                  ProfileFragment.setMyProfile(false, userName, userPicture);
                                                                                  openNewFrag(profileFragment, 3);
                                                                              }
                                                                          }
        );

    }


    public void parseInit() {
//        Parse.initialize(this, "GtPBrxcaTATmSsh4x9MoFJJg5U5ti29OandonYl8", "vXOLVE9RKbIg0VlzFX5m1moqeig7zoyFEml5lgl8");

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("APPLICATION_ID")
                .clientKey(null)
                .server("http://192.168.1.63:1337/parse/")
                .build()
        );

        ParseUser.enableRevocableSessionInBackground();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);


    }


    //******************************************************************
    //open new frag functions
    public void openNewFrag(Fragment newFrag) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.animation1, R.animator.animator2);
        ft.replace(R.id.fragment_container, newFrag, "detailFragment").addToBackStack(String.valueOf(currentFragment));
        ft.commit();
    }

    public void openNewFrag(Fragment newFrag, int index) {
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag).addToBackStack(null).commit();
//        overridePendingTransition(R.animator.animation1,R.animator.animator2);

        currentFragment = newFrag;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (index == 0) {
            ft.setCustomAnimations(R.animator.animation1, R.animator.animator2);
        } else {
            ft.setCustomAnimations(R.animator.animator3, R.animator.animator4);

        }
        ft.replace(R.id.fragment_container, newFrag).addToBackStack(String.valueOf(currentFragment));

// Start the animated transition.
        ft.commit();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        FragmentManager fragmentManager = getFragmentManager();
//        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()+1 ).getName();
//        Fragment currentFragment = getFragmentManager()
//                .findFragmentByTag(fragmentTag);
//        getFragmentManager().popBackStack();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.animator.animation1, R.animator.animator2);
//
//        ft.replace(R.id.fragment_container, currentFragment, "detailFragment").addToBackStack(null);
//
//// Start the animated transition.
//        ft.commit();
//    }

    public void reTouchFrag(Fragment frag) {
        getFragmentManager().popBackStack();
        getFragmentManager().beginTransaction().detach(frag).attach(frag).commit();
    }

    //******************************************************************
    //go to home page from SignIn Page
    public void LoginToHomePage() {
        loginFragment.setLoginToHomePageDelegate(new LoginFragment.LoginToHomePage() {
            @Override
            public void goToHomePage() {
                homeFragment.setHomoPage(1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();

                Home_Model.getInstance().setPostsArrayList(1, null, null);

                getSupportActionBar().show();
            }
        });
    }

    public void SignUpToHomePage() {
        signUpFragment.setSignUpToHomePageDelegate(new SignUpFragment.SignUpToHomePage() {
            @Override
            public void GoToHomePage() {
                homeFragment.setHomoPage(1);
                openNewFrag(homeFragment, 3);
                Home_Model.getInstance().setPostsArrayList(1, null, null);
                getSupportActionBar().show();
            }
        });
    }

    public void LoginToSignUpPage() {
        loginFragment.setLoginToSignUpDelegate(new LoginFragment.LoginToSignUp() {
            @Override
            public void GoToSignUpPage() {
                homeFragment.setHomoPage(1);
                openNewFrag(signUpFragment, 3);
            }
        });
    }

    public void SignUpToLoginPage() {
        signUpFragment.setSignUpToLoginPageDelegate(new SignUpFragment.SignUpToLoginPage() {
            @Override
            public void GoToLoginPage() {
                openNewFrag(loginFragment, 3);
            }
        });
    }


    public void postToUserProfile() {
        homeFragment.setUserPostToUserProfileDelegate(new HomeFragment.userPostToUserProfileInterface() {
            @Override
            public void userPostToUserProfile(String userName, Bitmap userPicture) {
                profileFragment.setMyProfile(false, userName, userPicture);
                openNewFrag(profileFragment);
            }
        });

    }


    public void cameraFragmentToHomePage() {
        cameraFragment.setCameraToHomePageDelegate(new CameraFragment.cameraToHomePageInterface() {
            @Override
            public void GoToHomePage() {
//                homeFragment.
                homeFragment.setHomoPage(1);
                Home_Model.getInstance().setPostsArrayList(4, null, null);
                Home_Model.getInstance().setPostsArrayList(2, Parse_model.getInstance().getUserClass().get_userName(), null);
                openNewFrag(homeFragment, 3);
            }
        });
    }

    public void performUserNameClick(String userName, Bitmap userPicture) {
        otherProfileFragment = new ProfileFragment();
        otherProfileFragment.setMyProfile(false, userName, userPicture);
        openNewFrag(otherProfileFragment, 1);
    }

    public void openProfileFragment() {
        profileFragment = new ProfileFragment();
        String myUserName = Parse_model.getInstance().getUserClass().get_userName();
        Bitmap myProfilePicture = Parse_model.getInstance().getUserClass().get_userPic();
        profileFragment.setMyProfile(true, myUserName, myProfilePicture);
        openNewFrag(profileFragment, leftOrRight((int) findViewById(R.id.profile1).getTag(INDEX)));
        currentFragment = profileFragment;
        currentTab = 4;
    }

//    public void setPostsImHomePage(ArrayList<PostClass> postsArrayList){
//        homeFragment.updatePostsArrayList(postsArrayList);
//    }

    public void setProfilePageGalleryButtonOn() {
        homeFragment.setChangeToGridViewButtonOnDelegate(new HomeFragment.ChangeToGridViewButtonOn() {
            @Override
            public void changeGridViewButtonOn() {
                profileFragment.changeGridButtonOn();
            }
        });
    }

    public void profileToEditFragment() {
        openNewFrag(updateUserFragment, 3);
    }

    public void openUserProfileFragment() {
        openNewFrag(profileFragment, 0);
    }

    public void openLoggingPage() {
        searchFragment = new SearchFragment();
        openNewFrag(loginFragment, 0);
    }

    public static LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public static Bitmap getBitmapFromSource(int id) {
        return BitmapFactory.decodeResource(ApplicationContextProvider.getContext().getResources(), id);
    }

    PermissionRequestCallback mPermissionRequestCallback;

    /**
     * This function checks for permissions  and ask for them if not granted before
     * @param callback for handling the result of the permissions's requests
     * @param permissions a list of permissions to be requested
     */
    @SuppressLint("NewApi")
    public void confirmPermissions(@NonNull PermissionRequestCallback callback,
                                   @NonNull String... permissions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onRequestResults(true, null);
            return;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionRequestCallback = callback;
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;
            }
        }

        callback.onRequestResults(true, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0) {
                ArrayList<String> rejectedPermissions = new ArrayList<>();

                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        rejectedPermissions.add(permissions[i]);
                    }
                }

                mPermissionRequestCallback.onRequestResults(
                        rejectedPermissions.isEmpty(), rejectedPermissions);
            } else {
                mPermissionRequestCallback.onRequestResults(false, null);
            }
        }
    }

    public interface PermissionRequestCallback {
        void onRequestResults(boolean isEmpty, ArrayList<String> rejectedPermissions);
    }

    public void onPermissionDenied() {
//        new ConnectAlertDialog.Builder(this)
//                .setTitle(getString(R.string.all_permissions))
//                .setMessage(getString(R.string.dialog_no_permissions))
//                .setNegativeButton(getString(R.string.all_settings), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        IntentHelper.launchApplicationSettingsForPackageName(BrandedActivity.this, BuildConfig.APPLICATION_ID);
//                    }
//                })
//                .setPositiveButton(getString(R.string.all_confirm), null)
//                .show();
    }
}