package com.mvc.entities;

import com.mvc.utils.PdfReport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PhoneCompany implements PdfReport {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;

    protected PhoneCompany() {}

    public PhoneCompany(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format(
                "Phone company[id=%d, name='%s']",
                id, name);
    }

    @Override
    public String toReportString() {
        return name;
    }
}

