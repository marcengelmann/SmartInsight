package de.tum.mw.ftm.praktikum.smartinsight;

import java.io.Serializable;

/**
 * Der Student ist in eien bestimmte Klausur eingeschreiben mit Hauptaufgaben von 1 bis x und
 * Unteraufgaben von a bis z
 * In dieser Klasse sollen die Unteraufgaben geladen/bearbeitet werden
 */
public class SubTask implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinked_phd() {
        return linked_phd;
    }

    public void setLinked_phd(String linked_phd) {
        this.linked_phd = linked_phd;
    }

    private String name, letter, id, linked_phd;

    public SubTask(String name, String letter, String id, String linked_phd){
        this.name = name;
        this.letter = letter;
        this.id = id;
        this.linked_phd = linked_phd;
    }

    @Override
    public String toString() {
        return "Name:"+name+"Letter:"+letter+"ID:"+id+"phd:"+linked_phd;
    }
}
