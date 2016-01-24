package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Locale speicherung der aktueleln Anfragen von StudentRequest
 */
public class StudentRequestLocalStore  {

    public  static final String SP_NAME = "anfrageDetails";
    SharedPreferences anfrageLocalDatabase;

    public StudentRequestLocalStore(Context context){
        anfrageLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeAnfrageData(StudentRequest anfrage){
        SharedPreferences.Editor spEditor = anfrageLocalDatabase.edit();
        spEditor.putString("linked_student", anfrage.getLinked_student());
        spEditor.putString("linked_task",anfrage.getLinked_task());
        spEditor.putString("linked_subtask",anfrage.getLinked_subtask());
        spEditor.putString("linked_phd",anfrage.getLinked_phd());
        spEditor.putString("linked_exam",anfrage.getLinked_exam());
        spEditor.putString("type_of_question",anfrage.getType_of_question());
        spEditor.putString("id",anfrage.getId());
        spEditor.putBoolean("done",anfrage.getDone());
        spEditor.apply();
    }

    public StudentRequest getDataAnfrageClient(){
        String linked_student = anfrageLocalDatabase.getString("linked_student", "");
        String linked_task = anfrageLocalDatabase.getString("linked_task","");
        String linked_subtask = anfrageLocalDatabase.getString("linked_subtask", "");
        String linked_phd = anfrageLocalDatabase.getString("linked_phd", "");
        String linked_exam = anfrageLocalDatabase.getString("linked_exam", "");
        String id = anfrageLocalDatabase.getString("id", "");
        String type_of_question = anfrageLocalDatabase.getString("type_of_question","");
        boolean done = anfrageLocalDatabase.getBoolean("done",false);

        return new StudentRequest(id,linked_student, linked_task, linked_subtask , linked_phd,linked_exam,type_of_question,done);
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

