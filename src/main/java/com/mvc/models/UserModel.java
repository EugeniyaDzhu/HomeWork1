package com.mvc.models;

import com.mvc.entities.User;
import com.mvc.utils.PdfReport;

public class UserModel implements PdfReport {
    private String firstName;
    private String lastName;

    protected UserModel() {}

    public UserModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserModel(User entity) {
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
    }

    @Override
    public String toString() {
        return String.format(
                "User[firstName='%s', lastName='%s']",
                firstName, lastName);
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toReportString() {
        return String.format(
                "%s %s",
                firstName, lastName);
    }
}
