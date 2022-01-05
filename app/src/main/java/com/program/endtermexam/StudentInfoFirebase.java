package com.program.endtermexam;

public class StudentInfoFirebase {

    private String firstName;
    private String lastName;
    private String email;
    private String type;
    private String status;

    public StudentInfoFirebase(){ }

    public StudentInfoFirebase(String firstName, String lastName, String email, String type, String status){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.type = type;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
