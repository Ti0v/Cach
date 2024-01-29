package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.dto.PasswordDto;
import com.CachWeb.Cach.dto.UserDto;
import com.CachWeb.Cach.entity.ExchangeRate;
import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.User;
import com.CachWeb.Cach.service.ExchangeRateService;
import com.CachWeb.Cach.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ExchangeRateService exchangeRateService;
    private final UserService userService;

    @Autowired
    public AuthController(UserDetailsService userDetailsService, ExchangeRateService exchangeRateService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.exchangeRateService = exchangeRateService;
        this.userService = userService;
    }

    @GetMapping({"/","/index"})

    public String home(Model model, Principal principal) {
        // Check if the user is authenticated
        isAuthontecated(model, principal);
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        model.addAttribute("exchangeRate", exchangeRates);

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        model.addAttribute("exchangeRequest", exchangeRequest);


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

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
        // Check for existing email
        if (userService.emailExists(user.getEmail())) {
            result.rejectValue("email", null, "This email already exists");
        }

        // Check for password confirmation
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            result.rejectValue("password", null, "The password and confirmation password do not match");
            result.rejectValue("passwordConfirmation", null, "The password and confirmation password do not match");
        }

        if (result.hasErrors()) {
            // Handle errors based on their presence in the BindingResult
            if (result.getFieldError("email") != null) {
                // Email error exists
                return "redirect:/register?error=" + encodeError("This email already exists");
            } else if (result.getFieldError("password") != null || result.getFieldError("passwordConfirmation") != null) {
                // Password or password confirmation error exists
                return "redirect:/register?error=" + encodeError("Password mismatch");
            }
        }

        // Save user if no errors
        userService.saveUser(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Redirect with a success message if needed
        return "redirect:/login";
    }

    @PostMapping("/deleteUser/{email}")
    public String deleteUser(@PathVariable("email") String email) {
        // Your logic to delete the user
        userService.deleteUser(email);
        return "redirect:/admin/users";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgot-password";
    }

    @GetMapping("/send-verification")
    public String sendPasswordResetTokenForm(Model model , Principal principal) {

        isAuthontecated(model, principal);
        return "send-token";
    }

    @PostMapping("/send-verification")
    public String sendPasswordResetTokenPost(@RequestParam("email") String email,
                                             HttpServletRequest request,
                                             Model model, Principal principal,
                                             final RedirectAttributes redirectAttributes) {
        isAuthontecated(model, principal);
            if (!userService.emailExists(email)) {
            model.addAttribute("emailNotExist", true);
            return "send-token";
        }

        User user = userService.findUserByEmail(email);
        userService.savePasswordResetToken(user);

        return "redirect:/send-verification/sent";
    }

    @GetMapping("/send-verification/sent")
    public String showResetLinkSentPage(Model model, Principal principal) {
        isAuthontecated(model, principal);
        return "reset-link-sent";
    }

    @GetMapping("/new-password")
    public String showNewPasswordPage(@RequestParam(name = "token", required = false) String resetToken, Model model , Principal principal) {
        isAuthontecated(model, principal);
        String tokenStatus = userService.validatePasswordResetToken(resetToken);

        if ("VALID_TOKEN".equals(tokenStatus)) {
            model.addAttribute("resetToken", resetToken);
            return "new-password";
        } else if ("INVALID_TOKEN".equals(tokenStatus)){
            model.addAttribute("tokenStatus", "Invalid Token");
            return "token-status";
        } else if("TOKEN_EXPIRED".equals(tokenStatus)){
            model.addAttribute("tokenStatus", "Token Expired");
            return "token-status";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("/new-password")
    public String processNewPassword(final PasswordDto passwordDto, @RequestParam("token") String resetToken, Principal principal,
                                     RedirectAttributes redirectAttributes, Model model){

        User user = userService.getUserByToken(resetToken);
        if (user == null) {
            return "redirect:/";
        }
        if (passwordDto.getNewPassword() == null || passwordDto.getNewPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("passwordError", "Password cannot be empty!");
            return "redirect:/new-password?token=" + resetToken;
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "Password does not match!");
            return "redirect:/new-password?token=" + resetToken;
        }

        userService.updatePassword(user, passwordDto.getNewPassword());

        model.addAttribute("tokenStatus", "Your password is successfully changed.");
        isAuthontecated(model, principal);
        return "token-status";
    }


    private void isAuthontecated(Model model, Principal principal) {
        test(model, principal, userService);
    }

    static void test(Model model, Principal principal, UserService userService) {
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            String username = principal.getName();
            String firstName = userService.findUserByEmail(username).getName();
            model.addAttribute("firstName", firstName);
        }
    }

    private String encodeError(String errorMessage) {
        try {
            return URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
            return "redirect:/register?error=unknownError";
        }
    }
}







