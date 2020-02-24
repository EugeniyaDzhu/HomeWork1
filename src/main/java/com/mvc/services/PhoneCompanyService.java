package com.mvc.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mvc.entities.PhoneCompany;
import com.mvc.models.PhoneCompanyModel;
import com.mvc.repositories.PhoneCompanyRepository;
import com.mvc.utils.ParseService;

@Service
public class PhoneCompanyService {

    @Autowired
    private PhoneCompanyRepository repository;

    public PhoneCompanyService(PhoneCompanyRepository repository) {
        this.repository = repository;
    }

    public List<PhoneCompanyModel> findByName(String name) {
        return repository.findByName(name).stream()
                .map(phoneCompany -> new PhoneCompanyModel(phoneCompany))
                .collect(Collectors.toList());
    }

    public Map<Long, PhoneCompanyModel> findAll() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(phoneCompany -> phoneCompany.getId(),
                        phoneCompany -> new PhoneCompanyModel(phoneCompany)));
    }

    public void add(PhoneCompanyModel phoneCompany) {
        repository.save(new PhoneCompany(phoneCompany.getName()));
    }

    @Transactional
    public void saveAll(List<PhoneCompanyModel> phoneCompanies) {
        repository.saveAll(phoneCompanies.stream()
                .map(phoneCompanyModel -> new PhoneCompany(phoneCompanyModel.getName()))
                .collect(Collectors.toList()));
    }

    public List<PhoneCompanyModel> parsePhoneCompaniesFromFile(MultipartFile multipartFile) throws Exception {
        List<PhoneCompanyModel> phoneCompanies = ParseService.parseUsersFromFile(multipartFile, this::toPhoneCompany);;
        return phoneCompanies;
    }

    private PhoneCompanyModel toPhoneCompany(String jsonText ) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PhoneCompanyModel phoneCompany = gson.fromJson(jsonText, PhoneCompanyModel.class);
        return phoneCompany;
    }
}
