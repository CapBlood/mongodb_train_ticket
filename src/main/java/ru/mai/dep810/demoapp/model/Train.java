package ru.mai.dep810.demoapp.model;

import java.io.Serializable;
import java.util.List;

public class Train implements Serializable {

    private static final long serialVersionUID = 3188156933445538333L;

    private String id;
    private String date;
    private String id_route;


    public Train() {
    }

    public Train(String id, String data, String id_route) {
        this.id = id;
        this.date = date;
        this.id_route = id_route;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String ddate) {
        this.date = date;
    }

    public String getId_route() {
        return id_route;
    }

    public void setId_route(String id_route) {
        this.id_route = id_route;
    }
}
