package de.tum.mw.ftm.praktikum.smartinsight;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rebecca on 02.01.2016.
 */
public class Calendar{
    public String date, name, room;

    public Calendar(String date,String name, String room) {
        this.date = date;
        this.name = name;
        this.room = room;
    }

}
