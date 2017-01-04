package com.example.micon.entity;

import java.util.HashMap;

/**
 * Created by micron on 11/5/2015.
 */
public class ListModelDoctor extends HashMap<String, String> {
    String name,designation,address,time,day,hospital;
    Integer image;

    public ListModelDoctor(String dname, String hname, String address, String docexpert, String cday, String ctime) {
        name = dname;
        hospital = hname;
        this.address = address;
        designation = docexpert;
        day = cday;
        time = ctime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String name) {
        this.hospital = hospital;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
