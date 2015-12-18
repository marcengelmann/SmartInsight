package de.tum.mw.ftm.praktikum.smartinsight;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class User {
    String name, email, password, matrikelnummer, sitNumb;

    public User (String email, String password, String name, String matrikelnummer, String sitNumb){
        this.email = email;
        this.password = password;
        this.matrikelnummer = matrikelnummer;
        this.name = name;
        this.sitNumb = sitNumb;
    }
}
