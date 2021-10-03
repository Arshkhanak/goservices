package com.tecmanic.goservices.ModelClass;

/**
 * Created by wolfsoft5 on 30/1/19.
 */

public class AcSecondModelClass {

    String title,subtitle;

    public AcSecondModelClass(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
