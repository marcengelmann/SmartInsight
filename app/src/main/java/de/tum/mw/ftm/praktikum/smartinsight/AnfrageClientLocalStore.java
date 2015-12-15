package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageClientLocalStore {

    public  static final String SP_NAME = "anfrageDetails";
    SharedPreferences anfrageLocalDatabase;

    public AnfrageClientLocalStore(Context context){
        anfrageLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeAnfrageData(Anfrage anfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putString("linked_student", anfrage.linked_student);
        spEditor.putString("linked_task",anfrage.linked_task);
        spEditor.putString("linked_subtask",anfrage.linked_subtask);
        spEditor.putString("linked_phd",anfrage.linked_phd);
        spEditor.putString("linked_exam",anfrage.linked_exam);
        spEditor.commit();
    }

    public Anfrage getDataAnfrageClient(){
        String linked_student = anfrageLocalDatabase.getString("linked_student", "");
        String linked_task = anfrageLocalDatabase.getString("linked_task","");
        String linked_subtask = anfrageLocalDatabase.getString("linked_subtask", "");
        String linked_phd = anfrageLocalDatabase.getString("linked_phd", "");
        String linked_exam = anfrageLocalDatabase.getString("linked_exam", "");

        Anfrage storedAnfrage = new Anfrage(linked_student, linked_task, linked_subtask , linked_phd,linked_exam);

        return storedAnfrage;
    }

    public void setStatusAnfrageClient(boolean setAnfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putBoolean("setAnfrage", setAnfrage);
        spEditor.commit();
    }

    public boolean getStatusAnfrageClient(){
        if(anfrageLocalDatabase.getBoolean("setAnfrage", false) == true){
            return true;
        }
        return false;
    }

    public void clearDataAnfrageClient(){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
