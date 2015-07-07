package com.mobile.kuryeapp.kuryeappv01.api;

import java.util.List;

/**
 * See https://developers.google.com/maps/documentation/directions/
 */
public class Directions {

    private static final String OK = "OK";

    private String status; // OK
    private List<Route> routes;

    public boolean isOk() {
        // A poor man's custom deserializer
        return status.equalsIgnoreCase(OK);
    }

    @Override
    public String toString() {
        return "Directions{" +
                "status='" + status + '\'' +
                ", routes=" + routes +
                '}';
    }

    public List<Route> getRoutes() { return routes; }
}
