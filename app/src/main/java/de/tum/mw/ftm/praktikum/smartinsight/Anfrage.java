package de.tum.mw.ftm.praktikum.smartinsight;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class Anfrage {
    public String linked_student, linked_task, linked_subtask, linked_phd, linked_exam,id, type_of_question;

    public Anfrage(String id,String student, String task, String subtask, String phd, String exam,String type_of_question) {
        this.linked_student = student;
        this.linked_task = task;
        this.id = id;
        this.linked_subtask = subtask;
        this.linked_phd = phd;
        this.linked_exam = exam;
        this.type_of_question = type_of_question;
    }

    @Override
    public String toString() {
        return "ANFRAGE: ID: "+id+" Student:" + linked_student+" Task:" + linked_task+" Subtask:"+ linked_subtask+" Doktorand:"+ linked_phd+" Prüfung:"+ linked_exam+" Typ:"+ type_of_question;
    }
}

