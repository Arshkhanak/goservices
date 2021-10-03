package com.tecmanic.goservices.ModelClass;

/**
 * Created by wolfsoft5 on 22/1/19.
 */

public class TimeModelClass {

    private String time,hours;

    public TimeModelClass(String time, String hours) {
        this.time = time;
        this.hours = hours;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
