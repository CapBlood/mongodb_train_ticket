package ru.mai.dep810.demoapp.model;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {

    private static final long serialVersionUID = 3188156933445538333L;

    private String id;
    private boolean bought;
    private String id_train;


    public Ticket() {
    }

    public Ticket(String id, boolean bought, String id_train) {
        this.id = id;
        this.bought = bought;
        this.id_train = id_train;
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

    public String getId_train() {
        return id_train;
    }

    public void setId_train(String id_train) {
        this.id_train = id_train;
    }
}
