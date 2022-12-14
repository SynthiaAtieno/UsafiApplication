package com.example.usafiapp.model;

public class Users {
    String name, search, id, email, type, phoneNo, idNumber, interest, profilepictureuri;

    public Users() {
    }

    public Users(String name,String search, String id, String email, String type, String phoneNo, String idNumber, String interest, String profilepictureuri) {
        this.name = name;
        this.search = search;
        this.id = id;
        this.email = email;
        this.type = type;
        this.phoneNo = phoneNo;
        this.idNumber = idNumber;
        this.interest = interest;
        this.profilepictureuri = profilepictureuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String search) {
        this.interest = interest;
    }
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getProfilepictureuri() {
        return profilepictureuri;
    }

    public void setProfilepictureuri(String profilePictureuri) {
        this.profilepictureuri = profilePictureuri;
    }

}
