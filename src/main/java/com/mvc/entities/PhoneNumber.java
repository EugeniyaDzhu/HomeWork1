package com.mvc.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PhoneNumber implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String number;
    @ManyToOne
    private User user;
    @ManyToOne
    private PhoneCompany phoneCompany;


    public PhoneNumber() {}

    public PhoneNumber(User user, PhoneCompany phoneCompany, String number) {
        this.user = user;
        this.phoneCompany = phoneCompany;
        this.number = number;
    }


    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public User getUser() {
        return user;
    }

    public PhoneCompany getPhoneCompany() {
        return phoneCompany;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPhoneCompany(PhoneCompany phoneCompany) {
        this.phoneCompany = phoneCompany;
    }

    @Override
    public String toString() {
        return String.format(
                "Phone number[id=%d, user='%s', phone company='%s', number='%s']",
                id, user, phoneCompany, number);
    }
}

