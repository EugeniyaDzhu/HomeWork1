package com.mvc.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mvc.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(String lastName);

    User findFirstByFirstNameAndLastName(String firstName, String lastName);

    List<User> findAll();
}
