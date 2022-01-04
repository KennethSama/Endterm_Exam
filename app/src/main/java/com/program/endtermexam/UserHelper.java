package com.program.endtermexam;

public class UserHelper {
    private String Email, FirstName, MiddleName, LastName, Password, AcademicProgram, Location, Type;
    private String quizNumber, numberOfItems;


    public UserHelper(String email, String firstName, String middleName, String lastName, String password, String academicProgram, String location, String type) {
        Email = email;
        FirstName = firstName;
        MiddleName = middleName;
        LastName = lastName;
        Password = password;
        AcademicProgram = academicProgram;
        Location = location;
        Type = type;
    }

    public UserHelper() { }

    public void CreateQuiz(String quizNumber,String numberOfItems){
        this.quizNumber =quizNumber;
        this.numberOfItems = numberOfItems;
    }
    public String getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber(String quizNumber) {
        this.quizNumber = quizNumber;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(String numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAcademicProgram() {
        return AcademicProgram;
    }

    public void setAcademicProgram(String academicProgram) {
        AcademicProgram = academicProgram;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
