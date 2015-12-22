package de.tum.mw.ftm.praktikum.smartinsight;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by marcengelmann on 15.12.15.
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
                spEditor.putString("tasklist", ObjectSerializer.serialize(taskList));
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
                tasks = (ArrayList<Task>) ObjectSerializer.deserialize(taskLocalDatabase.getString("tasklist",""));
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
