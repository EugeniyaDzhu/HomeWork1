package com.mvc;

import com.mvc.entities.PhoneCompany;
import com.mvc.entities.PhoneNumber;
import com.mvc.entities.User;
import com.mvc.repositories.PhoneCompanyRepository;
import com.mvc.repositories.PhoneNumberRepository;
import com.mvc.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(PhoneNumberRepository phoneNumberRepository,
                                    PhoneCompanyRepository phoneCompanyRepository,
                                    UserRepository userRepository) {
        return (args) -> {
            // save a few customers
            User user = new User("Jack", "Bauer");
            userRepository.save(user);

            PhoneCompany phoneCompany = new PhoneCompany("one");
            phoneCompanyRepository.save(phoneCompany);

            PhoneNumber phoneNumber = new PhoneNumber(user, phoneCompany, "1234567");
            phoneNumberRepository.save(phoneNumber);
//            repository.save(new User("Chloe", "O'Brian"));
//            repository.save(new User("Kim", "Bauer"));
//            repository.save(new User("David", "Palmer"));
//            repository.save(new User("Michelle", "Dessler"));
//
//            // fetch all customers
//            log.info("Customers found with findAll():");
//            log.info("-------------------------------");
            for (PhoneNumber customer : phoneNumberRepository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");
//
//            // fetch an individual customer by ID
//            User customer = repository.findById(1L);
//            log.info("Customer found with findById(1L):");
//            log.info("--------------------------------");
//            log.info(customer.toString());
//            log.info("");
//
//            // fetch customers by last name
//            log.info("Customer found with findByLastName('Bauer'):");
//            log.info("--------------------------------------------");
//            repository.findByLastName("Bauer").forEach(bauer -> {
//                log.info(bauer.toString());
//            });
//            // for (Customer bauer : repository.findByLastName("Bauer")) {
//            //  log.info(bauer.toString());
//            // }
//            log.info("");
        };
    }

}