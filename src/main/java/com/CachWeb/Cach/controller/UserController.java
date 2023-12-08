package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.entity.*;
import com.CachWeb.Cach.repository.ImageRepository;
import com.CachWeb.Cach.service.CurrencyService;
import com.CachWeb.Cach.service.ExchangeRateService;
import com.CachWeb.Cach.service.ExchangeRequestService;
import com.CachWeb.Cach.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private UserService userService;
   @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    /// Preview
    @GetMapping("/preview")
    public String getPreview(Model model, HttpSession httpSession  ,Principal principal) {
        ExchangeRequest exchangeRequest = (ExchangeRequest) httpSession.getAttribute("exchangeRequest");

        if (exchangeRequest != null) {
            model.addAttribute("exchangeRequest", exchangeRequest);
        }
        model.addAttribute("wallet", new Wallet());

        AuthController.test(model, principal, userService);

        return "user/Preview";
    }

    @PostMapping("/preview")
    public String postPreview( ExchangeRequest exchangeRequest, HttpSession httpSession) {
        httpSession.setAttribute("exchangeRequest", exchangeRequest);

        return "redirect:/user/preview";
    }



    /// add Prove
       @GetMapping("/conferm")
       public String conferm(Model model,Principal principal) {
        model.addAttribute("image", new Image());

           AuthController.test(model, principal, userService);
           return "user/conferm";
    }



    @PostMapping("/upload-image")
    public String handleImageUpload(@ModelAttribute Image image, @RequestParam("file") MultipartFile file, HttpSession httpSession,Principal principal) {
        try {
            if (!file.isEmpty()) {
                image.setData(file.getBytes());
                imageRepository.save(image);
            }

            ExchangeRequest exchangeRequest = (ExchangeRequest) httpSession.getAttribute("exchangeRequest");
            exchangeRequest.setImage(image);

            // Save the exchangeRequest
            boolean isAuthenticated = principal != null;
            if (isAuthenticated) {
                String username = principal.getName();
                // Now you have the username, you can use it to fetch more user details from your user repository
                // For example, assuming you have a UserRepository
                User user = userService.findUserByEmail(username);
                exchangeRequest.setUser(user);
            }

            exchangeRequestService.Save(exchangeRequest);

            // Redirect after successful upload
            return "redirect:/user/thankyou";
        } catch (IOException e) {
            // Log the exception using a logging framework

            // Redirect to an error page
            return "index";
        }
    }


    @GetMapping("/thankyou")
    public String last( HttpSession httpSession  ,Model model , Principal principal){
        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        model.addAttribute("id",exchangeRequest.getId());
        AuthController.test(model, principal, userService);
        return "user/confirmation";
    }

    @PostMapping("/conferm")
    public String confermPost( @ModelAttribute("wallet")Wallet wallet ,HttpSession httpSession){


        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        exchangeRequest.setWallet(wallet);
        httpSession.setAttribute("exchangeRequest",exchangeRequest);


        return "redirect:/user/conferm";
    }



//     @PostMapping("/upload-image")
//    public String handleImageUpload(@ModelAttribute Image image, @RequestParam("file") MultipartFile file) {// Validate and process the uploaded image
//        if (!file.isEmpty()) {
//            try {
//                image.setData(file.getBytes());
//                imageRepository.save(image);
//            } catch (IOException e) {
//                e.printStackTrace(); // Handle the exception appropriately
//            }
//        }
//
//        return "redirect:/";
//    }
}

