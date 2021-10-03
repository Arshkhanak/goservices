package com.tecmanic.goservices.ModelClass;

/**
 * Created by wolfsoft5 on 22/1/19.
 */

public class DateDayModelClass {


    String date,day;


    public DateDayModelClass(String date, String day) {
        this.date = date;
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
