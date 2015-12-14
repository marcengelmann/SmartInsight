package de.tum.mw.ftm.praktikum.smartinsight;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageProvider {
    public String endTime;
    public String startTime;
    public String editor;
    public String question;
    public String taskNumber;
    public String taskSubNumber;


    public AnfrageProvider(String startTime, String endTime, String taskNumber, String taskSubNumber, String question, String editor) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.editor = editor;
        this.question = question;
        this.taskNumber = taskNumber;
        this.taskSubNumber = taskSubNumber;
    }
}
