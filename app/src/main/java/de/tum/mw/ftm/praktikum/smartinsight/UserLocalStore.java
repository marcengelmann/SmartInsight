package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * KLasse die den Aktuellen nutzer lokal speichert
 */
public class UserLocalStore {

    public  static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString(String.valueOf(R.string.name), user.getName());
        spEditor.putString(String.valueOf(R.string.email), user.getEmail());
        spEditor.putString(String.valueOf(R.string.registrationNumb), user.getMatrikelnummer());
        spEditor.putString(String.valueOf(R.string.exam),user.getExam());
        spEditor.putString(String.valueOf(R.string.sitNumb), user.getSitNumb());
        spEditor.putString(String.valueOf(R.string.password), user.getPassword());
        spEditor.putBoolean(String.valueOf(R.string.didChange), user.getDidChange());
        spEditor.commit();
    }

    public User getUserLogInUser(){
        String name = userLocalDatabase.getString(String.valueOf(R.string.name), "");
        String email = userLocalDatabase.getString(String.valueOf(R.string.email),"");
        String matrikelnummer = userLocalDatabase.getString(String.valueOf(R.string.registrationNumb), "");
        String exam = userLocalDatabase.getString(String.valueOf(R.string.exam), "");
        String sitNumb = userLocalDatabase.getString(String.valueOf(R.string.sitNumb), "");
        String password = userLocalDatabase.getString(String.valueOf(R.string.password),"");
        Boolean didChange = userLocalDatabase.getBoolean(String.valueOf(R.string.didChange),false);

        User storedUser = new User(email,password, exam, name , matrikelnummer, sitNumb,didChange);

        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean(String.valueOf(R.string.loggedIn), loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean(String.valueOf(R.string.loggedIn), false) == true){
            return true;
        }
        return false;
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
