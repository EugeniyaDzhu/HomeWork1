package com.mvc.models;

import com.mvc.entities.PhoneNumber;
import com.mvc.utils.PdfReport;

public class PhoneNumberModel implements PdfReport {

    private String number;
    private UserModel user;
    private PhoneCompanyModel phoneCompany;


    public PhoneNumberModel() {}

    public PhoneNumberModel(UserModel user, PhoneCompanyModel phoneCompany, String number) {
        this.user = user;
        this.phoneCompany = phoneCompany;
        this.number = number;
    }

    public PhoneNumberModel(PhoneNumber entity) {
        this.user = new UserModel(entity.getUser());
        this.phoneCompany = new PhoneCompanyModel(entity.getPhoneCompany());
        this.number = entity.getNumber();
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PhoneCompanyModel getPhoneCompany() {
        return phoneCompany;
    }

    public void setPhoneCompany(PhoneCompanyModel phoneCompany) {
        this.phoneCompany = phoneCompany;
    }

    @Override
    public String toReportString() {
        return String.format(
                "User: '%s', Phone company: '%s', number - '%s'",
                user.toReportString(), phoneCompany.toReportString(), number);
    }
}
