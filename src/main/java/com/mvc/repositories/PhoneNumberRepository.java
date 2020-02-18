package com.mvc.repositories;

import com.mvc.entities.PhoneNumber;
import com.mvc.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, Long> {

}
