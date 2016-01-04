package de.tum.mw.ftm.praktikum.smartinsight;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by marcengelmann on 15.12.15.
 */
public class Task implements Serializable {
    public String name, linked_exam,id, number, linked_phd;
    public ArrayList<SubTask> subtasks;

    public Task(String name, String linked_exam, String linked_phd, String id, String number) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.linked_exam = linked_exam;
        this.linked_phd = linked_phd;
        this.subtasks = new ArrayList<SubTask>();
    }

    public void addSubtask(SubTask subtask) {
        this.subtasks.add(subtask);
    }

    public ArrayList<SubTask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "TASK: ID"+id+" number" +number+" name"+name+ " linked_exam" +linked_exam+" linked_phd"+linked_phd+" SUBTASKS: "+subtasks.size();
    }
}
