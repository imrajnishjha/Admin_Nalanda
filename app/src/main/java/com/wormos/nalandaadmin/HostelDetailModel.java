package com.wormos.nalandaadmin;

public class HostelDetailModel {

    String address,addressLink,category,description,name;

    public HostelDetailModel(String address, String addressLink, String category, String description, String name) {
        this.address = address;
        this.addressLink = addressLink;
        this.category = category;
        this.description = description;
        this.name = name;
    }

    public HostelDetailModel() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressLink() {
        return addressLink;
    }

    public void setAddressLink(String addressLink) {
        this.addressLink = addressLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
