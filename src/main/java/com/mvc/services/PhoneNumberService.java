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
import com.mvc.entities.PhoneNumber;
import com.mvc.entities.User;
import com.mvc.models.PhoneCompanyModel;
import com.mvc.models.PhoneNumberModel;
import com.mvc.models.UserModel;
import com.mvc.repositories.PhoneCompanyRepository;
import com.mvc.repositories.PhoneNumberRepository;
import com.mvc.repositories.UserRepository;
import com.mvc.utils.ParseService;

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

    public Map<Long , PhoneNumberModel> findAll() {
        return phoneNumberRepository.findAll()
                .stream()
                .collect(Collectors.toMap(phoneNumber -> phoneNumber.getId(),
                        phoneNumber -> new PhoneNumberModel(phoneNumber)));
    }

    @Transactional
    public void saveAll(List<PhoneNumberModel> phoneNumbers) {
        for (PhoneNumberModel phoneNumber : phoneNumbers) {
            UserModel user = phoneNumber.getUser();
            User persistedUser = userRepository.findFirstByFirstNameAndLastName(user.getFirstName(), user.getLastName());
            if (persistedUser == null) {
                persistedUser = userRepository.save(new User(user.getFirstName(), user.getLastName()));
            }
            PhoneCompanyModel phoneCompany = phoneNumber.getPhoneCompany();
            PhoneCompany persistedPhoneCompany = phoneCompanyRepository.findFirstByName(phoneCompany.getName());
            if (persistedPhoneCompany == null) {
                persistedPhoneCompany = phoneCompanyRepository.save(new PhoneCompany(phoneCompany.getName()));
            }
            phoneNumberRepository.save(new PhoneNumber(persistedUser, persistedPhoneCompany, phoneNumber.getNumber()));
        }
    }

    public void saveByIds(Long userId, Long phoneCompanyId, String number) {
        User user = userRepository.findById(userId).get();
        PhoneCompany phoneCompany = phoneCompanyRepository.findById(phoneCompanyId).get();
        phoneNumberRepository.save(new PhoneNumber(user, phoneCompany, number));

    }

    public List<PhoneNumberModel> parsePhoneNumbersFromFile(MultipartFile multipartFile) throws Exception {
        List<PhoneNumberModel> phoneNumbers = ParseService.parseUsersFromFile(multipartFile, this::toPhoneNumber);
        return phoneNumbers;
    }

    private PhoneNumberModel toPhoneNumber(String jsonText ) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PhoneNumberModel phoneNumber = gson.fromJson(jsonText, PhoneNumberModel.class);
        return phoneNumber;
    }
}

