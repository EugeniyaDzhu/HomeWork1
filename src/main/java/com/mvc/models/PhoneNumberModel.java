package com.mvc.models;

import com.mvc.entities.PhoneCompany;
import com.mvc.entities.User;

import javax.persistence.ManyToOne;

public class PhoneNumberModel {

    private String number;
    private User user;
    private PhoneCompany phoneCompany;


    public PhoneNumberModel() {}

    public PhoneNumberModel(User user, PhoneCompany phoneCompany, String number) {
        this.user = user;
        this.phoneCompany = phoneCompany;
        this.number = number;
    }


    @Override
    public String toString() {
        return String.format(
                "Phone number[user='%s', phone company='%s', number='%s']",
                user, phoneCompany, number);
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PhoneCompany getPhoneCompany() {
        return phoneCompany;
    }

    public void setPhoneCompany(PhoneCompany phoneCompany) {
        this.phoneCompany = phoneCompany;
    }
}
