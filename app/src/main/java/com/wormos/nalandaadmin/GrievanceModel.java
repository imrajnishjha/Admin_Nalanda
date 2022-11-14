package com.wormos.nalandaadmin;

public class GrievanceModel {
    String date,description,email,relation,status,subject;

    public GrievanceModel(String date, String description, String email, String relation, String status, String subject) {
        this.date = date;
        this.description = description;
        this.email = email;
        this.relation = relation;
        this.status = status;
        this.subject = subject;
    }

    public GrievanceModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
