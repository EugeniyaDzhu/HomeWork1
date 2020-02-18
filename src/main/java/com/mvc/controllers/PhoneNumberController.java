package com.mvc.controllers;

import com.mvc.NotFoundException;
import com.mvc.entities.PhoneCompany;
import com.mvc.entities.PhoneNumber;
import com.mvc.entities.User;
import com.mvc.models.PhoneNumberModel;
import com.mvc.repositories.PhoneNumberRepository;
import com.mvc.services.PhoneCompanyService;
import com.mvc.services.PhoneNumberService;
import com.mvc.services.UserService;
import com.mvc.utils.GeneratePdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/phonenumbers")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService phoneNumberService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhoneCompanyService phoneCompanyService;

    @GetMapping()
    public String getPhoneNumbers(Model model) {
        model.addAttribute("phonenumber", new PhoneNumberModel());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("companies", phoneCompanyService.findAll());
        model.addAttribute("numbers", phoneNumberService.findAll());
        return "phonenumbers";
    }

    @PostMapping()
    public String addPhoneNumbers(@RequestParam("user") String user,
                                  Model model) {
        model.addAttribute("numbers", phoneNumberService.findAll());
        return "phonenumbers";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("path", "/phonenumbers/upload");
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<PhoneNumber> phoneNumbers = Collections.emptyList();
        try {
            phoneNumbers = phoneNumberService.parsePhoneNumbersFromFile(file);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
        if (!phoneNumbers.isEmpty()) {
            phoneNumberService.saveAll(phoneNumbers);
        }
        redirectAttributes.addFlashAttribute("names", phoneNumbers);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded phone numbers!");

        return "redirect:/phonenumbers/upload";
    }

    @GetMapping(value = "/pdfreport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport() {

        List<PhoneNumber> phoneNumbers = phoneNumberService.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.report(phoneNumbers, "Phone numbers");

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
}
