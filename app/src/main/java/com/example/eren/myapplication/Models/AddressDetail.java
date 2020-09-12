package com.example.eren.myapplication.Models;

/**
 * Created by oguzh on 30 Nis 2019.
 */

public class AddressDetail {
    String _id;
    String city;
    String district;
    String neighborhood;
    String description;

    public AddressDetail() {
    }

    public AddressDetail(String _id, String city, String district, String neighborhood, String description) {
        this.city = city;
        this._id=_id;
        this.district = district;
        this.neighborhood = neighborhood;
        this.description = description;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getDescription() {
        return description;
    }
}
