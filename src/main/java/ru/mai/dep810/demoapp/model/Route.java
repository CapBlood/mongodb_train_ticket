package ru.mai.dep810.demoapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {

    private static final long serialVersionUID = -2511130773997181001L;
    private ArrayList<String> route;
    private String id;

    public Route() {
    }

    public Route(String id, ArrayList<String> route) {
        this.route = route;
        this.id = id;
    }

    public ArrayList<String> getRoute() {
        return this.route;
    }

    public void setRoute(ArrayList<String> route) {
        this.route = route;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
