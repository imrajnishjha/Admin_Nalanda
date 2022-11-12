package com.wormos.nalandaadmin;

public class UserVerificationModel {
    String id, name, purl,address,city,email,gender,hostel,phoneNo,state,university;
    int seater;

    public UserVerificationModel() {
    }

    public UserVerificationModel(String id, String name, String purl, String address, String city, String email, String gender, String hostel, String phoneNo, String state, String university, int seater) {
        this.id = id;
        this.name = name;
        this.purl = purl;
        this.address = address;
        this.city = city;
        this.email = email;
        this.gender = gender;
        this.hostel = hostel;
        this.phoneNo = phoneNo;
        this.state = state;
        this.university = university;
        this.seater = seater;
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

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public int getSeater() {
        return seater;
    }

    public void setSeater(int seater) {
        this.seater = seater;
    }
}
