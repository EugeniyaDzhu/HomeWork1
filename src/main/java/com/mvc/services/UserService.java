package com.mvc.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mvc.entities.User;
import com.mvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User add(User user) {
        return repository.save(user);
    }

    public List<User> findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Transactional
    public void saveAll(List<User> users) {
        repository.saveAll(users);
    }

    public List<User> parseUsersFromFile(MultipartFile multipartFile) throws Exception {
        File tempFile = File.createTempFile("D:/WorkingDirectory/temp", multipartFile.getOriginalFilename());
        tempFile.deleteOnExit();
        multipartFile.transferTo(tempFile);
        List<User> users = Files.lines(Paths.get(tempFile.getAbsoluteFile().toString()), StandardCharsets.UTF_8)
                .map(this::toUser).collect(Collectors.toList());
        tempFile.delete();
        return users;
    }

    private User toUser(String jsonText ) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        User user = gson.fromJson(jsonText, User.class);
        return user;
    }
}
