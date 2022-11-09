package com.wormos.nalandaadmin;

public class UserRegisteredModel {
    String id,purl,name;

    public UserRegisteredModel(String id, String purl, String name) {
        this.id = id;
        this.purl = purl;
        this.name = name;
    }

    public UserRegisteredModel() {
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
