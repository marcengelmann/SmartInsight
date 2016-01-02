package de.tum.mw.ftm.praktikum.smartinsight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rebecca on 02.01.2016.
 */
public class CalendarProvider {
    public String date, name, room;

    public CalendarProvider(String date, String name, String room) {
        this.date = date;
        this.name = name;
        this.room = room;
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

    public static List<CalendarProvider> createAnfrageProviderList(ArrayList<Calendar> requests) {
        List<CalendarProvider> calendarProviders = new ArrayList<CalendarProvider>();

        for(Calendar request:requests) {
            calendarProviders.add(new CalendarProvider(request.date, request.name, request.name));
        }

        return calendarProviders;
    }
}
