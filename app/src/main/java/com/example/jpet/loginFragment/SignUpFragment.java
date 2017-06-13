package com.example.jpet.loginFragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.Home.HomeFragment;
import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    EditText username, password, email;
    LoginFragment loginFragment = new LoginFragment();
    HomeFragment homeFragment = new HomeFragment();

    LinearLayout barButtons;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

         ((MainActivity)getActivity()).getSupportActionBar().setTitle("SignUp");

        barButtons = (LinearLayout) getActivity().findViewById(R.id.bar_buttons);
        barButtons.setVisibility(View.GONE);

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


        username = (EditText) root.findViewById(R.id.signup_username);
        password = (EditText) root.findViewById(R.id.signup_password);
        email = (EditText) root.findViewById(R.id.signup_email);

        Button signUp = (Button) root.findViewById(R.id.signup_button);
        Button cancel = (Button) root.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();

//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).addToBackStack(null).commit();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = String.valueOf(username.getText());
                if (!username.getText().equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("you need to fill User Name field");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


//                    alertDialog.show();
                }


                hideKeyboard(username);

                final String _userName = username.getText().toString();
                final String _password = password.getText().toString();
                final String _email = email.getText().toString();

                if (_userName.equals("") || _password.equals("") || _email.equals("")) {
                    errorDialog();
                }


                ParseUser user = new ParseUser();
                user.setUsername(_userName);
                user.setPassword(_password);
                user.setEmail(_email);

                if (_userName.equals("") || _password.equals("") || _email.equals("")) {
                    errorDialog();
                } else
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Parse_model.getInstance().setSignUp(_userName, _password, _email);
                                new SignUpNow().execute();
                            } else {
                                e.printStackTrace();
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                errorDialog();
                            }
                        }

                    });


            }
        });
        return root;
    }

    //************************************************************************************

    public class SignUpNow extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "", "Signing up, please wait..", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (Parse_model.getInstance().addNewUser()) {

                return "1";
            } else {
                return "0";
            }
        }

        protected void onPostExecute(String result) {
            if (result == "1") {
                Log.d("Parse:", "SUCCESSFUL SIGNUP!");

                barButtons.setVisibility(View.VISIBLE);

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

                //going to main screen
                if (signUpToHomePageDelegate != null) {
                    (new Thread() {
                        public void run() {
                            String UserID =  Parse_model.getInstance().getUserIdByUserName(Parse_model.getInstance().getUserClass().get_userName());
                            Parse_model.getInstance().getUserClass().set_userId(UserID);
                        }
                    }).start();
                    signUpToHomePageDelegate.GoToHomePage();
                }
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                progressDialog.dismiss();
                errorDialog();
                Log.e(getTag(), "Signing Up failed!");
            }
            progressDialog.dismiss();
        }
    }

    //************************************************************************************
    public interface SignUpToHomePage {
        public void GoToHomePage();
    }


    public void setSignUpToHomePageDelegate(SignUpToHomePage signUpToHomePageDelegate) {
        this.signUpToHomePageDelegate = signUpToHomePageDelegate;
    }

    SignUpToHomePage signUpToHomePageDelegate;

    //*********************************************************
    public interface SignUpToLoginPage {
        public void GoToLoginPage();
    }

    public void setSignUpToLoginPageDelegate(SignUpToLoginPage signUpToLoginPageDelegate) {
        this.signUpToLoginPageDelegate = signUpToLoginPageDelegate;
    }

    SignUpToLoginPage signUpToLoginPageDelegate;

    //************************************************************************************

    public void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Signing Up failed, please try again.")
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
}
