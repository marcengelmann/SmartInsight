package de.tum.mw.ftm.praktikum.smartinsight;

import java.io.Serializable;

/**
 * Created by Rebecca on 02.01.2016.
 */
public class Calendar implements Serializable {
    public String date, name, room, numbOfRegistration, responsiblePerson, mean;

    public Calendar(String date,String name, String room, String numbOfRegistration, String responsiblePerson, String mean) {
        this.date = date;
        this.name = name;
        this.room = room;
        this.numbOfRegistration = numbOfRegistration;
        this.responsiblePerson = responsiblePerson;
        this.mean = mean;
    }

    @Override
    public String toString() {
        return "Datum:"+date+" Name:"+name+" Raum:"+room;
    }
}
