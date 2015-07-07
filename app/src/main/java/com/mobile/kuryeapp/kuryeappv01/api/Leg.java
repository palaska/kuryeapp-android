package com.mobile.kuryeapp.kuryeappv01.api;


import java.util.List;

public class Leg {

    private Distance distance;
    private Duration duration;
    private List<Steps> steps;

    public Distance getDistance() { return distance; }
    public Duration getDuration() { return duration; }
    public List<Steps> getSteps() { return steps;    }
}