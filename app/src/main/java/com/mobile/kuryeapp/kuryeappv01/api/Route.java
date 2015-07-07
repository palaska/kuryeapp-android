package com.mobile.kuryeapp.kuryeappv01.api;

import java.util.List;

/**
 * See https://developers.google.com/maps/documentation/directions/
 */
public class Route {
    private List<Leg> legs;
    private OvPoly overview_polyline;

    public OvPoly getOvPoly() { return overview_polyline; }

    @Override
    public String toString() {
        return "Route{" +
                "legs=" + legs +
                ", overview_polyline=" + overview_polyline +
                '}';
    }

    public List<Leg> getLegs() { return legs; }
}
