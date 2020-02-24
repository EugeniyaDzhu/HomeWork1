package com.mvc.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mvc.entities.PhoneCompany;

@Repository
public interface PhoneCompanyRepository  extends CrudRepository<PhoneCompany, Long> {

    List<PhoneCompany> findByName(String name);

    PhoneCompany findFirstByName(String name);

    List<PhoneCompany> findAll();

}
