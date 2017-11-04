package com.oz_heng.apps.sydneyguide;

/**
 * Class pertaining to a location.
 */
public class Location {
    private String name;
    private int drawableId;
    private String description;
    private String address;
    private String mapUrl;
    private String webAddress;


    Location(String name) {
        this.name = name;
    }

    Location(String name, int id, String description, String address, String mapUrl, String webAddress) {
        this.name = name;
        this.drawableId = id;
        this.description = description;
        this.address = address;
        this.mapUrl = mapUrl;
        this.webAddress = webAddress;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getDrawableId() {
        return drawableId;
    }

    void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    String getMapUrl() {
        return mapUrl;
    }

    void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    String getWebAddress() {
        return webAddress;
    }

    void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

}
