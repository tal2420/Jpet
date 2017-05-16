package com.example.jpet.loginFragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.jpet.DB_Model.ModelSql;
import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.R;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    Button SignUp;
    Button SignIn;

    EditText email;
    EditText userPassword;

    LinearLayout barButtons;


    //login fragment's progress dialog
    //ProgressDialog progressDialog = new ProgressDialog(getActivity().getApplicationContext());


    public LoginFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_login, container, false);

        ParseUser.getCurrentUser().logOut();

        getActivity().getActionBar().setTitle("LogIn");

        barButtons = (LinearLayout) getActivity().findViewById(R.id.bar_buttons);
        barButtons.setVisibility(View.GONE);


        email = (EditText) root.findViewById(R.id.textview_email);
        userPassword = (EditText) root.findViewById(R.id.textview_password);

        //initial buttons
        SignUp = (Button) root.findViewById(R.id.signup_button);
        SignIn = (Button) root.findViewById(R.id.button_signin);

        getActivity().findViewById(R.id.home_page).setVisibility(View.GONE);
        getActivity().findViewById(R.id.search).setVisibility(View.GONE);
        getActivity().findViewById(R.id.like).setVisibility(View.GONE);
        getActivity().findViewById(R.id.camera).setVisibility(View.GONE);
        getActivity().findViewById(R.id.profile).setVisibility(View.GONE);
        getActivity().findViewById(R.id.home_page1).setVisibility(View.GONE);
        getActivity().findViewById(R.id.search1).setVisibility(View.GONE);
        getActivity().findViewById(R.id.like1).setVisibility(View.GONE);
        getActivity().findViewById(R.id.camera1).setVisibility(View.GONE);
        getActivity().findViewById(R.id.profile1).setVisibility(View.GONE);


        //login with user name and password
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barButtons.setVisibility(View.VISIBLE);
                hideKeyboard(email);

                final String _email;
                final String _password;

                _email = email.getText().toString();
                _password = userPassword.getText().toString();

                String string[] = {_email, _password};
                new LoggingIn().execute(string);


                //final TextView login_Error = (TextView)root.findViewById(R.id.loginError);


            }
        });


        SignUp = (Button) root.findViewById(R.id.button_signup);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginToSignUpDelegate != null) {
                    loginToSignUpDelegate.GoToSignUpPage();
                }

            }
        });

//***************************************************************************************
        //  only for lazies
//        email.setText("1");
//        userPassword.setText("1");
//        SignIn.performClick();
//***************************************************************************************


        return root;
    }


    protected class LoggingIn extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading, please wait!", true);
//            progressDialog.setIcon(R.drawable.arrow);

        }

        @Override
        protected String doInBackground(String... params) {

//            Parse_model.getInstance().getUserClass().set_userName(params[0]);
            Parse_model.getInstance().getUserClass().set_email(params[0]);
            Parse_model.getInstance().getUserClass().set_password(params[1]);
            if (Parse_model.getInstance().loginWithUser()) {
                //logging in success

                ArrayList<UserClass> usersArray = ModelSql.getInstance().getAllUsers();

                boolean isUserExist = false;
                for (UserClass user : usersArray) {
                    if (user.get_email().equals(params[0])) {
                        isUserExist = true;
                    }
                }
                if (!isUserExist) {
                    ModelSql.getInstance().addUser(Parse_model.getInstance().getUserClass());
                }

                return "1";
            }
            return "0";
        }


        @Override
        protected void onPostExecute(String result) {
            if (result == "1") {
                Log.d("Login:", "Logged in successfully!");

//                //adding user class to local DB
                UserClass loggingUser = Parse_model.getInstance().getUserClass();
//                ArrayList<UserClass> usersArray = ModelSql.getInstance().getAllUsers();
//                boolean isUserExist = false;
//                for(UserClass currUser : usersArray){
//                    if(currUser.get_userName().equals(loggingUser.get_userName())){
//                        isUserExist = true;
//                    }
//                }
//                if(!isUserExist){
//                    ModelSql.getInstance().addUser(loggingUser);
//                }


                Parse_model.getInstance().getUserClass().set_isOn(true);

                getActivity().findViewById(R.id.home_page).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.search).setVisibility(View.GONE);
                getActivity().findViewById(R.id.like).setVisibility(View.GONE);
                getActivity().findViewById(R.id.camera).setVisibility(View.GONE);
                getActivity().findViewById(R.id.profile).setVisibility(View.GONE);
                getActivity().findViewById(R.id.home_page1).setVisibility(View.GONE);
                getActivity().findViewById(R.id.search1).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.like1).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.camera1).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.profile1).setVisibility(View.VISIBLE);

                if (loginToHomePageDelegate != null) {
                    loginToHomePageDelegate.goToHomePage();
                }
            } else {
                Log.e("Login:", "did NOT logged in!!!");
                Log.e("Login:", "result = " + result);
                Parse_model.getInstance().getUserClass().set_isOn(false);
                progressDialog.hide();
                errorDialog();
            }
            progressDialog.hide();
        }
    }


    public void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Login failed, please try again.")
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

    public void hideKeyboard(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

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

    //***********************************************************************************************

    //going from login page to commercial list page
    public interface LoginToHomePage {
        public void goToHomePage();
    }

    public void setLoginToHomePageDelegate(LoginToHomePage loginToHomePageDelegate) {
        this.loginToHomePageDelegate = loginToHomePageDelegate;
    }

    LoginToHomePage loginToHomePageDelegate;

    //*************************************************
    //Going from login page to signUp page
    public interface LoginToSignUp {
        public void GoToSignUpPage();
    }

    public void setLoginToSignUpDelegate(LoginToSignUp loginToSignUpDelegate) {
        this.loginToSignUpDelegate = loginToSignUpDelegate;
    }

    public LoginToSignUp loginToSignUpDelegate;


//***********************************************************************************************

}

