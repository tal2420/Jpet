package com.example.jpet.DB_Model.ParseDB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.jpet.ApplicationContextProvider;
import com.example.jpet.Camera.PostClass;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.loginFragment.UserClass;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liran on 01-Jul-15.
 */
public class Parse_User {
    private static Boolean isSucceedUpdatingHolder;

    public void addProfilePicture(final Bitmap pic) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        Log.e("addPost Profile Picture", "is ON");

        if (pic == null) {
            Log.e("", "picture is NULL");
        } else {
            Log.e("", "picture is NOT null");
        }

// Retrieve the object by id
        String _userID = Parse_model.getInstance().getUserClass().get_userId();
        //String _userID = "gL205za8qe";
        Log.e("UserID IS: ", _userID);
        query.getInBackground(_userID, new GetCallback<ParseObject>() {
            public void done(ParseObject User, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.


                    byte[] data = encodeToBase64(pic).getBytes();
                    ParseFile file = new ParseFile("pic.txt", data);

                    User.put("picture", file);
                    User.saveInBackground();


                    Log.e("profile picture: ", "added picture to parse");
                    Toast.makeText(ApplicationContextProvider.getContext(), "Pic added to parse",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.e("profile picture: ", "did NOT added picture to parse");
                }
            }
        });

    }

    public String getUserIdByUserName(String userName){
        ArrayList<PostClass> postsArrayList = new ArrayList<PostClass>();
        String UserID = "";
        ParseQuery<ParseObject> query = new ParseQuery("Users");
        query.whereEqualTo("UserName", userName);
        try {
            ParseObject result = query.getFirst();
            UserID = result.getObjectId();//String("objectId");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return UserID;

    }

    public boolean addNewUser() {
        ParseObject po = new ParseObject("Users");
        String UserName = Parse_model.getInstance().getUserClass().get_userName();
        po.put("UserName", UserName);
        po.put("Password", Parse_model.getInstance().getUserClass().get_password());
        po.put("Email", Parse_model.getInstance().getUserClass().get_email());
        po.put("numFollowers", 0);

        String UserID = getUserIdByUserName(UserName);

        Parse_model.getInstance().getUserClass().set_userId(UserID);

        //byte[] data = null;
        //ParseFile file = new ParseFile("pic.txt", data);
        //po.put("picture", file);


        try {
            po.save();
            Log.d("Parse SignUp:", "Signned Up Successfuly!");
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginWithUser() {

        Log.i("loginFragment:", "login button has been pressed");

        String currEmail = Parse_model.getInstance().getUserClass().get_email();
        String currPassword = Parse_model.getInstance().getUserClass().get_password();

        ParseQuery<ParseObject> query = new ParseQuery("Users");
        try {
            List<ParseObject> result = query.find();
            for (ParseObject po : result) {

                String email = po.getString("Email");
                String userPassword = po.getString("Password");

                if (currEmail.equals(email) && currPassword.equals(userPassword)) {

                    Parse_model.getInstance().getUserClass().set_userName(po.getString("UserName"));
                    Parse_model.getInstance().getUserClass().set_userId(po.getObjectId());

                    ParseFile applicantResume = (ParseFile) po.get("picture");
                    if (applicantResume != null) {
                        applicantResume.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    // data has the bytes for the resume
                                    String pic = new String(data);
                                    Bitmap bitmap = decodeBase64(pic);
                                    Parse_model.getInstance().getUserClass().set_userPic(bitmap);
                                    Log.e("Parse:", "got the profile picture");

                                } else {
                                    // something went wrong
                                    Log.e("PARSE:", "did NOT got the profile picture");
                                }
                            }
                        });
                    } else {
                        Log.e("Logging:", "User profile picture is NULL");
                        Parse_model.getInstance().getUserClass().set_userPic(null);
                    }


                    Parse_model.getInstance().getUserClass().set_isOn(true);
                    return true;
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //UserClass.getInstance().setIsOn(false);
        return false;
    }

    public boolean updateUserDetails(final UserClass currUser) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");

        isSucceedUpdatingHolder = false;

// Retrieve the object by id
        query.getInBackground(currUser.get_userId(), new GetCallback<ParseObject>() {
            public void done(ParseObject userObject, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.

                    String pass = currUser.get_password();
                    String email = currUser.get_email();

                    if(pass!="" && pass!=null){
                        userObject.put("Password", pass);
                    }

                    if(email!="" && email!=null){
                        userObject.put("Email", email);
                    }

                    userObject.saveInBackground();
                    Log.e("Updating User: ", "Succeed");
                    isSucceedUpdatingHolder = true;
                }
            }
        });

        return isSucceedUpdatingHolder;
    }


    //************************************************
    //encoding decoding functions for transferring pictures to parse
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

    //************************************************

}
