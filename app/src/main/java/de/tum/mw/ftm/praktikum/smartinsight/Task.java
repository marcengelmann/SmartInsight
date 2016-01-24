package de.tum.mw.ftm.praktikum.smartinsight;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Der Student ist in eien bestimmte Klausur eingeschreiben mit Hauptaufgaben von 1 bis x und
 * Unteraufgaben von a bis z
 * In dieser Klasse sollen die Hauptaufgaben geladen/bearbeitet werden
 */
public class Task implements Serializable {
    private String name, linked_exam,id, number, linked_phd;

    public void setSubtasks(ArrayList<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinked_exam() {
        return linked_exam;
    }

    public void setLinked_exam(String linked_exam) {
        this.linked_exam = linked_exam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLinked_phd() {
        return linked_phd;
    }

    public void setLinked_phd(String linked_phd) {
        this.linked_phd = linked_phd;
    }

    private ArrayList<SubTask> subtasks;

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
