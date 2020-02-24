package com.mvc.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mvc.NotFoundException;
import com.mvc.models.UserModel;
import com.mvc.services.UserService;
import com.mvc.utils.GeneratePdfReport;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/lastname/{lastname}")
    @ResponseBody
    public List<UserModel> getUser(@PathVariable(name = "lastname") String lastname) {
        return userService.findByLastName(lastname);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserModel getUserById(@PathVariable(name = "id") Long id) {
        return userService.findById(id);
    }

    @GetMapping()
    public String getUsers(Model model) {
        model.addAttribute("names", userService.findAll().values());
        return "users";
    }

    @PostMapping()
    public String addUsers(@RequestParam("firstname") String firstName,
                           @RequestParam("lastname") String lastName) {
        UserModel user = new UserModel(firstName, lastName);
        userService.add(user);
        return "redirect:/users";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("path", "/users/upload");
        model.addAttribute("pdfpath", "/users/pdfreport");
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<UserModel> users = Collections.emptyList();
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

        Map<Long, UserModel> users = userService.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.report(users.values(), "Users");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

//    @ExceptionHandler(NotFoundException.class)
//    public String handleStorageFileNotFound(NotFoundException exc, Model model) {
//        model.addAttribute("message", exc.getMessage());
//        return "/exception";
//    }
}
