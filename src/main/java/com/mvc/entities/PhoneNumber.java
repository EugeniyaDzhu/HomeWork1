package com.mvc.entities;

import com.mvc.utils.PdfReport;

import javax.persistence.*;

@Entity
public class PhoneNumber implements PdfReport {

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

    @Override
    public String toReportString() {
        return String.format(
                "User: '%s', Phone company: '%s', number - '%s'",
                user.toReportString(), phoneCompany.toReportString(), number);
    }
}

