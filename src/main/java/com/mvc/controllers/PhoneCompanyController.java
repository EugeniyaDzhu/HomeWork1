package com.mvc.controllers;

import com.mvc.NotFoundException;
import com.mvc.entities.PhoneCompany;
import com.mvc.entities.User;
import com.mvc.repositories.PhoneCompanyRepository;
import com.mvc.services.PhoneCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/phonecompany")
public class PhoneCompanyController {

    @Autowired
    private PhoneCompanyService phoneCompanyService;

    @GetMapping("/{name}")
    @ResponseBody
    public List<PhoneCompany> getUser(@PathVariable(name = "name") String name) {
        return phoneCompanyService.findByName(name);
    }

    @GetMapping()
    public String getUsers(Model model) {
        model.addAttribute("names", phoneCompanyService.findAll());
        return "phonecompanies";
    }

    @PostMapping()
    public String addUsers(@RequestParam("name") String name) {
        PhoneCompany phoneCompany = new PhoneCompany(name);
        phoneCompanyService.add(phoneCompany);
        return "redirect:/phonecompany";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("path", "/phonecompany/upload");
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<PhoneCompany> phoneCompanies = Collections.emptyList();
        try {
            phoneCompanies = phoneCompanyService.parsePhoneCompaniesFromFile(file);
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
        if (!phoneCompanies.isEmpty()) {
            phoneCompanyService.saveAll(phoneCompanies);
        }
        redirectAttributes.addFlashAttribute("names", phoneCompanies);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded phone companies!");

        return "redirect:/phonecompany/upload";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleStorageFileNotFound(NotFoundException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("message", exc.getMessage());
        return "redirect:/exception";
    }
}
