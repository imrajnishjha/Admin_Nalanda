package com.wormos.nalandaadmin;

public class ReferModel {

    String phoneNo,college,name, referralEmail,referralName;

    public ReferModel(String phoneNo, String college, String name, String referralEmail, String referralName) {
        this.phoneNo = phoneNo;
        this.college = college;
        this.name = name;
        this.referralEmail = referralEmail;
        this.referralName = referralName;
    }

    public ReferModel() {
    }

    public String getReferralName() {
        return referralName;
    }

    public void setReferralName(String referralName) {
        this.referralName = referralName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferralEmail() {
        return referralEmail;
    }

    public void setReferralEmail(String referralEmail) {
        this.referralEmail = referralEmail;
    }
}
