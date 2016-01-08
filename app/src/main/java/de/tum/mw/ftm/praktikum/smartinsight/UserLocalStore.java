package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class UserLocalStore {

    public  static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("email", user.email);
        spEditor.putString("matrikelnummer", user.matrikelnummer);
        spEditor.putString("exam",user.exam);
        spEditor.putString("sitNumb", user.sitNumb);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

    public User getUserLogInUser(){
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email","");
        String matrikelnummer = userLocalDatabase.getString("matrikelnummer", "");
        String exam = userLocalDatabase.getString("exam", "");
        String sitNumb = userLocalDatabase.getString("sitNumb", "");
        String password = userLocalDatabase.getString("password","");

        User storedUser = new User(email,password, exam, name , matrikelnummer, sitNumb);

        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false) == true){
            return true;
        }
        return false;
    }

    public boolean getUserStatusProfilPic(){
        if(userLocalDatabase.getBoolean("statusProfilPic", false) == true){
            return true;
        }
        return false;
    }

    public void setUserStatusProfilPic(boolean status){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("statusProfilPic", status);
        spEditor.commit();
    }

    public Uri getUserProfilPic(){
        String profilPic = userLocalDatabase.getString("profilPic", "");

        return (Uri.parse(profilPic));
    }

    public void setUserProfilPic(Uri bitmap){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("profilPic", bitmap.toString());
        spEditor.commit();
    }


    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
