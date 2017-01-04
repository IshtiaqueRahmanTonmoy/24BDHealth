package com.example.micon.entity;

import java.util.HashMap;

/**
 * Created by micron on 11/5/2015.
 */
public class ListModel extends HashMap<String, String> {
    String hname,address;
    int imgid;

    public ListModel(String hname, String address, int imgid) {
        this.hname = hname;
        this.address = address;
        this.imgid = imgid;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}
