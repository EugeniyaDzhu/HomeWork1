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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mvc.NotFoundException;
import com.mvc.models.PhoneCompanyModel;
import com.mvc.services.PhoneCompanyService;
import com.mvc.utils.GeneratePdfReport;

@Controller
@RequestMapping("/phonecompany")
public class PhoneCompanyController {

    @Autowired
    private PhoneCompanyService phoneCompanyService;

    @GetMapping("/{name}")
    @ResponseBody
    public List<PhoneCompanyModel> gePhoneCompany(@PathVariable(name = "name") String name) {
        return phoneCompanyService.findByName(name);
    }

    @GetMapping()
    public String getPhoneCompanies(Model model) {
        model.addAttribute("names", phoneCompanyService.findAll().values());
        return "phonecompanies";
    }

    @PostMapping()
    public String addUsers(@RequestParam("name") String name) {
        PhoneCompanyModel phoneCompany = new PhoneCompanyModel(name);
        phoneCompanyService.add(phoneCompany);
        return "redirect:/phonecompany";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("path", "/phonecompany/upload");
        model.addAttribute("pdfpath", "/phonecompany/pdfreport");
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        List<PhoneCompanyModel> phoneCompanies = Collections.emptyList();
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

    @GetMapping(value = "/pdfreport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport() {

        Map<Long, PhoneCompanyModel> phoneCompanies = phoneCompanyService.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.report(phoneCompanies.values(), "Phone companies");

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
