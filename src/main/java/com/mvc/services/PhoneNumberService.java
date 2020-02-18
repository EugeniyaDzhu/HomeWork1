package com.mvc.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mvc.entities.PhoneCompany;
import com.mvc.entities.PhoneNumber;
import com.mvc.entities.User;
import com.mvc.repositories.PhoneCompanyRepository;
import com.mvc.repositories.PhoneNumberRepository;
import com.mvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneNumberService {

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private PhoneCompanyRepository phoneCompanyRepository;
    @Autowired
    private UserRepository userRepository;

    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository,
                              PhoneCompanyRepository phoneCompanyRepository,
                              UserRepository userRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
        this.phoneCompanyRepository = phoneCompanyRepository;
        this.userRepository = userRepository;
    }

    public List<PhoneNumber> findAll() {
        return (List<PhoneNumber>) phoneNumberRepository.findAll();
    }

    @Transactional
    public void saveAll(List<PhoneNumber> phoneNumbers) {
        for (PhoneNumber phoneNumber : phoneNumbers) {
            User user = phoneNumber.getUser();
            User persistedUser = userRepository.findFirstByFirstNameAndLastName(user.getFirstName(), user.getLastName());
            if (persistedUser == null) {
                persistedUser = userRepository.save(user);
            }
            PhoneCompany phoneCompany = phoneNumber.getPhoneCompany();
            PhoneCompany persistedPhoneCompany = phoneCompanyRepository.findFirstByName(phoneCompany.getName());
            if (persistedPhoneCompany == null) {
                persistedPhoneCompany = phoneCompanyRepository.save(phoneCompany);
            }
            phoneNumber.setPhoneCompany(persistedPhoneCompany);
            phoneNumber.setUser(persistedUser);
            phoneNumberRepository.save(phoneNumber);
        }
    }

    public List<PhoneNumber> parsePhoneNumbersFromFile(MultipartFile multipartFile) throws Exception {
        File tempFile = File.createTempFile("D:/WorkingDirectory/temp", multipartFile.getOriginalFilename());
        tempFile.deleteOnExit();
        multipartFile.transferTo(tempFile);
        List<PhoneNumber> phoneNumbers = Files.lines(Paths.get(tempFile.getAbsoluteFile().toString()), StandardCharsets.UTF_8)
                .map(this::toPhoneNumber).collect(Collectors.toList());
        tempFile.delete();
        return phoneNumbers;
    }

    private PhoneNumber toPhoneNumber(String jsonText ) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PhoneNumber phoneNumber = gson.fromJson(jsonText, PhoneNumber.class);
        return phoneNumber;
    }
}

