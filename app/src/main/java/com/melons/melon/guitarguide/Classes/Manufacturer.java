package com.melons.melon.guitarguide;

public class Manufacturer {


    public String description;
    public String name;
    public String type;
    public String url;

    public Manufacturer() {
    }

    public Manufacturer(String description, String name, String type, String url) {
        this.description = description;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

}
