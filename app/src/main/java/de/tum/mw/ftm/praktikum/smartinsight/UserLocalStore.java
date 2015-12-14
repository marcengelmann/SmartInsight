package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;

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
        spEditor.putInt("matrikelnummer", user.matrikelnummer);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

    public User getUserLogInUser(){
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email","");
        int matrikelnummer = userLocalDatabase.getInt("matrikelnummer", -1);
        String password = userLocalDatabase.getString("password", "");

        User storedUser = new User(email, password, name , matrikelnummer);

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

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
