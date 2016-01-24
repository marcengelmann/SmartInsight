package de.tum.mw.ftm.praktikum.smartinsight;

import java.io.Serializable;

/**
 * Klasse die die Klausureinsichttermine speichert
 */
public class Calendar implements Serializable {
    private String date, name, room, numbOfRegistration, responsiblePerson,mean_grade;

    public Calendar(String date,String name, String room, String numbOfRegistration, String responsiblePerson,String mean_grade) {
        this.date = date;
        this.name = name;
        this.room = room;
        this.numbOfRegistration = numbOfRegistration;
        this.responsiblePerson = responsiblePerson;
        this.mean_grade = mean_grade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNumbOfRegistration() {
        return numbOfRegistration;
    }

    public void setNumbOfRegistration(String numbOfRegistration) {
        this.numbOfRegistration = numbOfRegistration;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getMean_grade() {
        return mean_grade;
    }

    public void setMean_grade(String mean_grade) {
        this.mean_grade = mean_grade;
    }

    @Override
    public String toString() {
        return "Datum:"+date+" Name:"+name+" Raum:"+room;
    }
}
