package com.mvc.controllers;

import com.mvc.NotFoundException;
import com.mvc.entities.User;
import com.mvc.repositories.UserRepository;
import com.mvc.services.UserService;
import com.mvc.utils.GeneratePdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{lastname}")
    @ResponseBody
    public List<User> getUser(@PathVariable(name = "lastname") String lastname) {
        return userService.findByLastName(lastname);
    }

    @GetMapping()
    public String getUsers(Model model) {
        model.addAttribute("names", userService.findAll());
        return "users";
    }

    @PostMapping()
    public String addUsers(@RequestParam("firstname") String firstName,
                           @RequestParam("lastname") String lastName) {
        User user = new User(firstName, lastName);
        userService.add(user);
        return "redirect:/users";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("path", "/users/upload");
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<User> users = Collections.emptyList();
        try {
             users = userService.parseUsersFromFile(file);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
        if (!users.isEmpty()) {
            userService.saveAll(users);
        }
        redirectAttributes.addFlashAttribute("names", users);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded users!");

        return "redirect:/users/upload";
    }

    @GetMapping(value = "/pdfreport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport() {

        List<User> users = userService.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.report(users, "Users");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleStorageFileNotFound(NotFoundException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("message", exc.getMessage());
        return "redirect:/exception";
    }

//    @GetMapping
//    public String startPage(Model model) {
//        return "index";
//    }
}
