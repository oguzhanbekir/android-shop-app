package com.example.eren.myapplication.Models;

/**
 * Created by oguzh on 1 May 2019.
 */

public class City {
    String id;
    String name;
    String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }

    public City(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
