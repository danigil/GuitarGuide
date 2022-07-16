package com.melons.melon.guitarguide;

public class Guitarist {
    public String description;
    public String name;
    public String url;

    public Guitarist() {
    }

    public Guitarist(String description, String name, String url) {
        this.description = description;
        this.name = name;
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
