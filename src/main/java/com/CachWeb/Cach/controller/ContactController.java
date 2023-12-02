package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.entity.ContactForm;
import com.CachWeb.Cach.entity.User;
import com.CachWeb.Cach.service.ContactService;
import com.CachWeb.Cach.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class ContactController {
    @Autowired
  private ContactService service;
    @Autowired
    private UserService userService;
    @GetMapping("/contact")
    public String contact( Model model ,  Principal principal) {
       ContactForm contactForm = new ContactForm();

        model.addAttribute("contactForm", contactForm);
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        // Retrieve user information if authenticated
        if (isAuthenticated) {
            String username = principal.getName();
            // Now you have the username, you can use it to fetch more user details from your user repository
            // For example, assuming you have a UserRepository
            User user = userService.findUserByEmail(username);
            if (user != null) {
                Long userId = user.getId();
                String name = user.getName();
                // Add user information to the model
                model.addAttribute("userId", userId);
                model.addAttribute("username", name);
            }
        }
        return "user/contact";
    }

    @PostMapping("/submitForm")
    public String submitForm(@ModelAttribute ContactForm contactForm) {


     service.addContact(contactForm);
     return "user/confirmation";
    }
}
