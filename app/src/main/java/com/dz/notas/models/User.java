package com.dz.notas.models;

import java.util.ArrayList;

/**
 * Created by herman on 12/09/2016.
 */
public class User {

    public String email;
    public String uid;
    public String cellphone;
    public String name;
    private ArrayList friends;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String uid, String cellphone,String name, ArrayList friends) {
        this.email = email;
        this.uid = uid;
        this.cellphone = cellphone;
        this.name = name;
        this.friends = friends;
    }

    public String getEmail(){return this.email;}

    public String getUid(){
        return this.uid;
    }

    public String getCellphone(){
        return this.cellphone;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList getFriends(){return this.friends;}

}
