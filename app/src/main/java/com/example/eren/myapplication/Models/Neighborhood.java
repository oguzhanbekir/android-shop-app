package com.example.eren.myapplication.Models;

/**
 * Created by oguzh on 1 May 2019.
 */

public class Neighborhood {
    String id;
    String name;
    String district;

    public Neighborhood(String id, String name, String district) {
        this.id = id;
        this.name = name;
        this.district = district;
    }

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return name;
    }
}
