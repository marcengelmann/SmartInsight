package de.tum.mw.ftm.praktikum.smartinsight;

import java.util.ArrayList;

/**
 * Created by marcengelmann on 15.12.15.
 */
public class Task {
    public int id, number, linked_phd;
    public String name, linked_exam;
    public ArrayList<SubTask> subtasks;

    public Task(String name, String linked_exam, int linked_phd, int id, int number) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.linked_exam = linked_exam;
        this.linked_phd = linked_phd;
    }

    public void addSubtask(SubTask subtask) {
        this.subtasks.add(subtask);
    }
}
