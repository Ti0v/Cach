package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.dto.UserDto;
import com.CachWeb.Cach.entity.ExchangeRate;
import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.User;
import com.CachWeb.Cach.service.ExchangeRateService;
import com.CachWeb.Cach.service.ExchangeRequestService;
import com.CachWeb.Cach.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping({"/index","/"})

    public String home(Model model, Principal principal) {
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        model.addAttribute("exchangeRate", exchangeRates);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        model.addAttribute("exchangeRequest", exchangeRequest);

        // Check if the user is authenticated
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

        return "/index";
    }


    @GetMapping("/login")
    public String login(Model model , Principal principal){


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
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model , Principal principal){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        // Retrieve user information if authenticated
        if (isAuthenticated) {
            String username = principal.getName();
            // Now you have the username, you can use it to fetch more user details from your user repository
            // For example, assuming you have a UserRepository
            User userr = userService.findUserByEmail(username);
            if (user != null) {
                Long userId = userr.getId();
                String name = userr.getName();
                // Add user information to the model
                model.addAttribute("userId", userId);
                model.addAttribute("username", name);
            }
        }
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,BindingResult result, Model model){

        User existingUser = userService.findUserByEmail(user.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){

            result.rejectValue("email", null, "There is already an account registered with the same email");
        }


        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "/register";
        }

        userService.saveUser(user);
        return "redirect:/index";
    }




    @PostMapping("/deleteUser/{email}")
    public String deleteUser(@PathVariable("email") String email) {
        // Your logic to delete the user
        userService.deleteUser(email);
        return "redirect:/users";
    }

    @GetMapping("/user-role")
    public String userrole(){

        return "user-role";
    }
}




