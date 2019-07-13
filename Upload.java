package com.example.myapplication;

public class Upload {

    public String name;
    public String url;

    // constructor for calls
public Upload(){}
    public Upload(String name, String url) {
        this.name = name;
this.url= url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}