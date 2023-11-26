package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.entity.ContactForm;
import com.CachWeb.Cach.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ContactController {
    @Autowired
  private ContactService service;

    @GetMapping("/contact")
    public String contact( Model model) {
       ContactForm contactForm = new ContactForm();

        model.addAttribute("contactForm", contactForm);

        return "user/contact";
    }

    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute ContactForm contactForm) {


     service.addContact(contactForm);
     return "user/confirmation";
    }
}
