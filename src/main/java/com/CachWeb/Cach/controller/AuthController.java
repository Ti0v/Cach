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

    public String home(Model model , Principal principal) {


        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        model.addAttribute("exchangeRate", exchangeRates);
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        model.addAttribute("exchangeRequest", exchangeRequest);

        // Check if the user is authenticated
        boolean isAuthenticated = principal != null;


        // Add a flag to the model to indicate whether the user is authenticated
        model.addAttribute("isAuthenticated", isAuthenticated);



        return "/index";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,BindingResult result, Model model){

        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){

            result.rejectValue("email", null, "There is already an account registered with the same email");
        }


        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/index";
    }


    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "admin/users";
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




