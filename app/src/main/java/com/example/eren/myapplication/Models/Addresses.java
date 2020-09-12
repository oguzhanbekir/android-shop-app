package com.example.eren.myapplication.Models;



/**
 * Created by oguzh on 30 Nis 2019.
 */

public class Addresses {
    String id;
    String name;
    String user;
    String address;
    String addressId;

    public Addresses(String id, String name, String user, String address,String addressId) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.address = address;
        this.addressId=addressId;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressId(){return addressId;}

}
