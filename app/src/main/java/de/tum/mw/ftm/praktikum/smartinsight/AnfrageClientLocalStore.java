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

    public void storeAnfrageData(AnfrageClient anfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putInt("matrikelnummer", anfrage.matrikelnummer);
        spEditor.putString("question",anfrage.question);
        spEditor.putString("taskNumber",anfrage.taskNumber);
        spEditor.putString("taskSubNumber",anfrage.taskSubNumber);

        spEditor.commit();
    }

    public AnfrageClient getDataAnfrageClient(){
        String taskNumber = anfrageLocalDatabase.getString("taskNumber", "");
        String taskSubNumber = anfrageLocalDatabase.getString("taskSubNumber","");
        int matrikelnummer = anfrageLocalDatabase.getInt("matrikelnummer", -1);
        String question = anfrageLocalDatabase.getString("question", "");

        AnfrageClient storedAnfrage = new AnfrageClient(matrikelnummer, taskNumber, taskSubNumber , question);

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
