package de.tum.mw.ftm.praktikum.smartinsight;

import android.graphics.Bitmap;

/**
 * Speichern der datenstruktur vom Studenten
 */
public class User {
    private String name, email, password, exam, matrikelnummer, sitNumb;
    private boolean didChange;

    public User (String email, String password, String exam, String name, String matrikelnummer, String sitNumb, boolean didChange){
        this.email = email;
        this.password = password;
        this.exam = exam;
        this.matrikelnummer = matrikelnummer;
        this.name = name;
        this.sitNumb = sitNumb;
        this.didChange = didChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setMatrikelnummer(String matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public String getSitNumb() {
        return sitNumb;
    }

    public void setSitNumb(String sitNumb) {
        this.sitNumb = sitNumb;
    }

    public boolean getDidChange() {
        return didChange;
    }

    public void setDidChange(boolean didChange) {
        this.didChange = didChange;
    }
}
