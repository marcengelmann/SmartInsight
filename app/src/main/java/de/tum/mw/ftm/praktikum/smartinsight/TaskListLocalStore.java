package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Der Student ist in eien bestimmte Klausur eingeschreiben mit Hauptaufgaben von 1 bis x und
 * Unteraufgaben von a bis z
 * In dieser Klasse sollen die Aufgaben der Klasuur gespeichert werden
 */
public class TaskListLocalStore {

    public  static final String SP_NAME = "tasks";
        SharedPreferences taskLocalDatabase;

    public TaskListLocalStore(Context context){
            taskLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        }

    public void storeTaskList(ArrayList<Task> taskList){
            SharedPreferences.Editor spEditor = taskLocalDatabase.edit();

            try {
                spEditor.putString(String.valueOf(R.string.takslist), ObjectSerializer.serialize(taskList));
            } catch (IOException e) {
                e.printStackTrace();
            }
            spEditor.commit();
        System.out.println("tasks stored!");
        }

    @SuppressWarnings("unchecked")
    public ArrayList<Task> getTaskList(){
            ArrayList<Task> tasks = null;
            try {
                tasks = (ArrayList<Task>) ObjectSerializer.deserialize(taskLocalDatabase.getString(String.valueOf(R.string.takslist),""));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return tasks;
    }

    public void clearTaskStore(){
            SharedPreferences.Editor spEditor = taskLocalDatabase.edit();
            spEditor.clear();
            spEditor.commit();
    }
}
