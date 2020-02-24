package com.mvc.models;

import com.mvc.entities.PhoneCompany;
import com.mvc.utils.PdfReport;

public class PhoneCompanyModel implements PdfReport {

    private String name;

    protected PhoneCompanyModel() {
    }

    public PhoneCompanyModel(String name) {
        this.name = name;
    }

    public PhoneCompanyModel(PhoneCompany entity) {
        this.name = entity.getName();
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

    @Override
    public String toReportString() {
        return name;
    }
}
