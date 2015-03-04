package com.mobile.kuryeapp.kuryeappv01.Classes;

public class customLocation {
    Double x;
    Double y;

    public customLocation(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "customLocation{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
