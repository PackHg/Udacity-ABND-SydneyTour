package com.oz_heng.apps.sydneyguide;

import android.graphics.drawable.Drawable;

/**
 * Class pertaining to a location.
 */
public class Location {
    private String name;
    private Drawable drawable;
    private String description;

    Location(String name) {
        this.name = name;
    }

    Location(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

}
