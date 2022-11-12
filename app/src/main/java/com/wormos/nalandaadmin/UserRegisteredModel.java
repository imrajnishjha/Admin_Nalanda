package com.wormos.nalandaadmin;

public class UserRegisteredModel {
    String id,purl,name,hostel;

    public UserRegisteredModel(String id, String purl, String name, String hostel) {
        this.id = id;
        this.purl = purl;
        this.name = name;
        this.hostel = hostel;
    }

    public UserRegisteredModel() {
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
