package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageLocalStore {

    public  static final String SP_NAME = "anfrageDetails";
    SharedPreferences anfrageLocalDatabase;

    public AnfrageLocalStore(Context context){
        anfrageLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeAnfrageData(Anfrage anfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putString("linked_student", anfrage.linked_student);
        spEditor.putString("linked_task",anfrage.linked_task);
        spEditor.putString("linked_subtask",anfrage.linked_subtask);
        spEditor.putString("linked_phd",anfrage.linked_phd);
        spEditor.putString("linked_exam",anfrage.linked_exam);
        spEditor.putString("type_of_question",anfrage.type_of_question);
        spEditor.putString("id",anfrage.id);
        spEditor.putBoolean("done",anfrage.done);
        spEditor.apply();
    }

    public Anfrage getDataAnfrageClient(){
        String linked_student = anfrageLocalDatabase.getString("linked_student", "");
        String linked_task = anfrageLocalDatabase.getString("linked_task","");
        String linked_subtask = anfrageLocalDatabase.getString("linked_subtask", "");
        String linked_phd = anfrageLocalDatabase.getString("linked_phd", "");
        String linked_exam = anfrageLocalDatabase.getString("linked_exam", "");
        String id = anfrageLocalDatabase.getString("id", "");
        String type_of_question = anfrageLocalDatabase.getString("type_of_question","");
        boolean done = anfrageLocalDatabase.getBoolean("done",false);

        return new Anfrage(id,linked_student, linked_task, linked_subtask , linked_phd,linked_exam,type_of_question,done);
    }

    public void setStatusAnfrageClient(boolean setAnfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putBoolean("setAnfrage", setAnfrage);
        spEditor.apply();
    }

    public boolean getStatusAnfrageClient(){
        return anfrageLocalDatabase.getBoolean("setAnfrage", false);
    }

    public void clearDataAnfrageClient(){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
