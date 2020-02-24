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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mvc.NotFoundException;
import com.mvc.models.PhoneNumberModel;
import com.mvc.services.PhoneCompanyService;
import com.mvc.services.PhoneNumberService;
import com.mvc.services.UserService;
import com.mvc.utils.GeneratePdfReport;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addPhoneNumbers(@RequestParam("user") Long userId,
                                  @RequestParam("phoneCompany") Long phoneCompanyId,
                                  @RequestParam("number") String number,
                                  Model model) {
        phoneNumberService.saveByIds(userId, phoneCompanyId, number);
        return "redirect:/phonenumbers";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("path", "/phonenumbers/upload");
        model.addAttribute("pdfpath", "/phonenumbers/pdfreport");
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<PhoneNumberModel> phoneNumbers = Collections.emptyList();
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

        Map<Long, PhoneNumberModel> phoneNumbers = phoneNumberService.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.report(phoneNumbers.values(), "Phone numbers");

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
