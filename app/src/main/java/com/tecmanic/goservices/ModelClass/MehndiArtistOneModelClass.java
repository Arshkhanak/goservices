package com.tecmanic.goservices.ModelClass;

/**
 * Created by wolfsoft5 on 29/1/19.
 */

public class MehndiArtistOneModelClass {

    String title,subtitle;

    public MehndiArtistOneModelClass(String title, String subtitle) {
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
