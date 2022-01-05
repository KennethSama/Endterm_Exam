package com.program.endtermexam;

public class StudentInfo {
    private String name;
    private String email;
    private String status;
    private String userID;

    public StudentInfo(String name, String email, String status, String userID) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
