package de.tum.mw.ftm.praktikum.smartinsight;

import java.io.Serializable;

/**
 * Created by marcengelmann on 15.12.15.
 */
public class SubTask implements Serializable {
    public String name, letter, id, linked_phd;

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
