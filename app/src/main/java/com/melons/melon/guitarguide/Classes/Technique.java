package com.melons.melon.guitarguide;

public class Technique {
    public boolean isFav;
    public String mainTitle;
    public String subTitle;

    public Technique(boolean isFav, String mainTitle, String subTitle) {
        this.isFav = isFav;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
    }

    public Technique() {

    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }
}
