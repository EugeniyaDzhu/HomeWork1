package com.mvc.repositories;

import com.mvc.entities.PhoneCompany;
import com.mvc.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneCompanyRepository  extends CrudRepository<PhoneCompany, Long> {

    List<PhoneCompany> findByName(String name);

    PhoneCompany findFirstByName(String name);

}
