package de.tum.mw.ftm.praktikum.smartinsight;

import android.graphics.Bitmap;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class User {
    String name, email, password, exam, matrikelnummer, sitNumb;

    public User (String email, String password, String exam, String name, String matrikelnummer, String sitNumb){
        this.email = email;
        this.password = password;
        this.exam = exam;
        this.matrikelnummer = matrikelnummer;
        this.name = name;
        this.sitNumb = sitNumb;
    }
}
