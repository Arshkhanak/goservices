package com.tecmanic.goservices.ModelClass;

/**
 * Created by wolfsoft5 on 8/1/19.
 */

public class HomeCategoryModelClass {

    Integer image;
    String title;

    public HomeCategoryModelClass(Integer image, String title) {
        this.image = image;
        this.title = title;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
