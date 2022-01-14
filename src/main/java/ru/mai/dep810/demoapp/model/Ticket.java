package ru.mai.dep810.demoapp.model;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {

    private static final long serialVersionUID = 3188156933445538333L;

    private String id;
    private boolean bought;
    private String train;


    public Ticket() {
    }

    public Ticket(String id, boolean bought, String train) {
        this.id = id;
        this.bought = bought;
        this.train = train;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getBought() {
        return this.bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String id_train) {
        this.train = id_train;
    }
}
