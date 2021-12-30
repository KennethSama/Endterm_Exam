package com.program.endtermexam;

public class ListButton {
    private String category;
    private String date;
    private String description;
    private String person;
    private String value;

    public ListButton(String category, String date, String description, String person, String value) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.person = person;
        this.value = value;
    }

    public String GetCategory() {
        return category;
    }

    public void SetCategory(String category) {
        this.category = category;
    }

    public String GetDate() {
        return date;
    }

    public void SetDate(String date) {
        this.date = date;
    }

    public String GetDescription() {
        return description;
    }

    public void SetDescription(String description) {
        this.description = description;
    }

    public String GetPerson() {
        return person;
    }

    public void SetPerson(String person) {
        this.person = person;
    }

    public String GetValue() {
        return value;
    }

    public void SetValue(String value) {
        this.value = value;
    }
}
