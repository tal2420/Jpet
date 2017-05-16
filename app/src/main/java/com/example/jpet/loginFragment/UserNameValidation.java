package com.example.jpet.loginFragment;

/**
 * Created by Liran on 15-Apr-15.
 */
public class UserNameValidation {
    String _userName;
    String _password;
    String _email;


    public UserNameValidation(String _userName, String _password, String _email) {
        this._userName = _userName;
        this._password = _password;
        this._email = _email;
    }

    public Boolean UserNameValidation() {
        String[] userNameArray = _userName.split(" ");
        String[] passwordArray = _password.split(" ");
        String[] emailArray = _email.split(" ");

        if (userNameArray.length == 1 && passwordArray.length == 1 && emailArray.length == 1) {
            return true;
        } else {
            return false;
        }
    }

}
