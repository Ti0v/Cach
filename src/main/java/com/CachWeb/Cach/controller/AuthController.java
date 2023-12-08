package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.dto.UserDto;
import com.CachWeb.Cach.entity.ExchangeRate;
import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.User;
import com.CachWeb.Cach.service.ExchangeRateService;
import com.CachWeb.Cach.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private ExchangeRateService exchangeRateService;


    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request

    @GetMapping({"/","/index"})

    public String home(Model model, Principal principal) {
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        model.addAttribute("exchangeRate", exchangeRates);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        model.addAttribute("exchangeRequest", exchangeRequest);

        // Check if the user is authenticated
        isAuthontecated(model, principal);

        return "index";
    }


    @GetMapping("/login")
    public String login(Model model , Principal principal){


        isAuthontecated(model, principal);
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model, Principal principal) {

        UserDto user = new UserDto();
        model.addAttribute("user", user);

        isAuthontecated(model, principal);


        return "register";
    }

    private void isAuthontecated(Model model, Principal principal) {
        test(model, principal, userService);
    }

    static void test(Model model, Principal principal, UserService userService) {
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String username = principal.getName();
            User userEntity = userService.findUserByEmail(username);

            if (userEntity != null) {
                Long userId = userEntity.getId();
                String name = userEntity.getName();
                model.addAttribute("userId", userId);
                model.addAttribute("username", name);
            }
        }
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user, BindingResult result) {
        User existingUser = userService.findUserByEmail(user.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "هذا الايميل موجود بالفعل");
        }

        if (result.hasErrors()) {
            try {
                // URL encode the error message
                String encodedError = URLEncoder.encode("هذا الايميل موجود بالفعل", StandardCharsets.UTF_8.toString());

                // Use the encoded error message in the redirect URL
                return "redirect:/register?error=" + encodedError;
            } catch (UnsupportedEncodingException e) {
                // Handle the encoding exception, e.g., log the error
                e.printStackTrace();
            }
        }

        userService.saveUser(user);

        // Redirect with a success message if needed

        return "redirect:/index";
    }




    @PostMapping("/deleteUser/{email}")
    public String deleteUser(@PathVariable("email") String email) {
        // Your logic to delete the user
        userService.deleteUser(email);
        return "redirect:/admin/users";
    }

}




