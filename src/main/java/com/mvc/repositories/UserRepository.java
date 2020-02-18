package com.mvc.repositories;

import java.util.List;

import com.mvc.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(String lastName);

    User findFirstByFirstNameAndLastName(String firstName, String lastName);
}
