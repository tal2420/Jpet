package com.example.jpet.DB_Model.Models;

/**
 * Created by Liran on 31-Aug-15.
 */
public class Login_Model {

    Login_Model(){}

    static final Login_Model instance = new Login_Model();

    public static Login_Model getInstance() {
        return instance;
    }


}
