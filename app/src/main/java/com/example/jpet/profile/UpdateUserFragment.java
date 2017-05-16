package com.example.jpet.profile;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jpet.DB_Model.Parse_model;
import com.example.jpet.MainActivity;
import com.example.jpet.R;
import com.example.jpet.loginFragment.UserClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateUserFragment extends Fragment {


    public UpdateUserFragment() {
        // Required empty public constructor
    }

    EditText updatePasswordEditText;
    EditText updateEmailEditText;
    Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_user, container, false);

        updatePasswordEditText = (EditText) root.findViewById(R.id.update_user_password);
        updateEmailEditText = (EditText) root.findViewById(R.id.update_user_email);
        cancel = (Button) root.findViewById(R.id.update_user_cancel);


        Button updateUserButton = (Button) root.findViewById(R.id.update_user_button);
        updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final UserClass currUser = new UserClass(
                        updatePasswordEditText.getText().toString(),
                        updateEmailEditText.getText().toString(),
                        Parse_model.getInstance().getUserClass().get_userId()
                );

                new UpdateUserProfile(currUser).execute();
                Log.e("updateUserButton", " PRESSED");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openProfileFragment();
            }
        });


        return root;
    }

    public class UpdateUserProfile extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        UserClass currUser;

        public UpdateUserProfile(UserClass currUser) {
            this.currUser = currUser;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading posts. Please wait...", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            boolean isSucceed = Parse_model.getInstance().updateUserDetails(currUser);

            if (isSucceed) {
                ((MainActivity)getActivity()).openProfileFragment();
//                Toast.makeText(getActivity(), "SUCCEED updating your profile", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getActivity(), "Failed updating your profile", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.hide();
            updatePasswordEditText.setText("");
            updateEmailEditText.setText("");
        }
    }

}
