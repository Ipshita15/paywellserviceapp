package com.cloudwell.paywell.services.activity.myFavorite;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
public enum MenuStatus {

    UnFavorite("unFavourite"),
    Favourite("Favourite");

    private String text;

    MenuStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
