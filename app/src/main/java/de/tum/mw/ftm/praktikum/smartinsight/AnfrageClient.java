package de.tum.mw.ftm.praktikum.smartinsight;

/**
 * Created by Rebecca on 13.12.2015.
 */
public class AnfrageClient {
    public int matrikelnummer;
    public String question;
    public String taskNumber;
    public String taskSubNumber;


    public AnfrageClient(int matrikelnummer, String taskNumber, String taskSubNumber, String question) {
        this.matrikelnummer = matrikelnummer;
        this.question = question;
        this.taskNumber = taskNumber;
        this.taskSubNumber = taskSubNumber;
    }
}

