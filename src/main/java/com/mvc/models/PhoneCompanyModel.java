package com.mvc.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PhoneCompanyModel {

    private String name;

    protected PhoneCompanyModel() {
    }

    public PhoneCompanyModel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Phone company[name='%s']",
                name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
