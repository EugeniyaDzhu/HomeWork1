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
import com.mvc.NotFoundException;
import com.mvc.entities.User;
import com.mvc.models.UserModel;
import com.mvc.repositories.UserRepository;
import com.mvc.utils.ParseService;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void add(UserModel user) {
        repository.save(new User(user.getFirstName(), user.getLastName()));
    }

    public UserModel findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("No user with id"));
        return new UserModel(user);
    }

    public List<UserModel> findByLastName(String lastName) {
        return repository.findByLastName(lastName).stream()
                .map(user -> new UserModel(user))
                .collect(Collectors.toList());
    }

    public Map<Long, UserModel> findAll() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(user -> user.getId(), user -> new UserModel(user)));
    }

    @Transactional
    public void saveAll(List<UserModel> users) {
        repository.saveAll(users.stream()
                .map(user -> new User(user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList()));
    }

    public List<UserModel> parseUsersFromFile(MultipartFile multipartFile){
        List<UserModel> users = ParseService.parseUsersFromFile(multipartFile, this::toUser);
        return users;
    }

    private UserModel toUser(String jsonText ) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        UserModel user = gson.fromJson(jsonText, UserModel.class);
        return user;
    }
}
