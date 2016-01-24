package de.tum.mw.ftm.praktikum.smartinsight;

/**
 * Hier werden die aktuellen Anfragen zu der Klausr gespeicher
 */
public class StudentRequest {
    private String linked_student, linked_task, linked_subtask, linked_phd, linked_exam,id, type_of_question;
    private boolean done;

    public StudentRequest(String id,String student, String task, String subtask, String phd, String exam,String type_of_question, boolean done) {
        this.linked_student = student;
        this.linked_task = task;
        this.id = id;
        this.linked_subtask = subtask;
        this.linked_phd = phd;
        this.linked_exam = exam;
        this.type_of_question = type_of_question;
        this.done = done;
    }

    public String getLinked_student() {
        return linked_student;
    }

    public void setLinked_student(String linked_student) {
        this.linked_student = linked_student;
    }

    public String getLinked_task() {
        return linked_task;
    }

    public void setLinked_task(String linked_task) {
        this.linked_task = linked_task;
    }

    public String getLinked_subtask() {
        return linked_subtask;
    }

    public void setLinked_subtask(String linked_subtask) {
        this.linked_subtask = linked_subtask;
    }

    public String getLinked_phd() {
        return linked_phd;
    }

    public void setLinked_phd(String linked_phd) {
        this.linked_phd = linked_phd;
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

    public String getType_of_question() {
        return type_of_question;
    }

    public void setType_of_question(String type_of_question) {
        this.type_of_question = type_of_question;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "ANFRAGE: ID: "+id+" Student:" + linked_student+" Task:" + linked_task+" Subtask:"+ linked_subtask+" Doktorand:"+ linked_phd+" Prüfung:"+ linked_exam+" Typ:"+ type_of_question;
    }
}
