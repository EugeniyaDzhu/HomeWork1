package com.mvc.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mvc.entities.PhoneCompany;
import com.mvc.repositories.PhoneCompanyRepository;
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
public class PhoneCompanyService {

    @Autowired
    private PhoneCompanyRepository repository;

    public PhoneCompanyService(PhoneCompanyRepository repository) {
        this.repository = repository;
    }

    public List<PhoneCompany> findByName(String name) {
        return repository.findByName(name);
    }

    public List<PhoneCompany> findAll() {
        return (List<PhoneCompany>) repository.findAll();
    }

    public PhoneCompany add(PhoneCompany phoneCompany) {
        return repository.save(phoneCompany);
    }

    @Transactional
    public void saveAll(List<PhoneCompany> phoneCompanies) {
        repository.saveAll(phoneCompanies);
    }

    public List<PhoneCompany> parsePhoneCompaniesFromFile(MultipartFile multipartFile) throws Exception {
        File tempFile = File.createTempFile("D:/WorkingDirectory/temp", multipartFile.getOriginalFilename());
        tempFile.deleteOnExit();
        multipartFile.transferTo(tempFile);
        List<PhoneCompany> phoneCompanies = Files.lines(Paths.get(tempFile.getAbsoluteFile().toString()), StandardCharsets.UTF_8)
                .map(this::toPhoneCompany).collect(Collectors.toList());
        tempFile.delete();
        return phoneCompanies;
    }

    private PhoneCompany toPhoneCompany(String jsonText ) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PhoneCompany phoneCompany = gson.fromJson(jsonText, PhoneCompany.class);
        return phoneCompany;
    }
}
