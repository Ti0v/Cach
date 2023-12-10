package com.CachWeb.Cach.controller;

import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.CachWeb.Cach.entity.*;
import com.CachWeb.Cach.repository.ImageRepository;
import com.CachWeb.Cach.service.CurrencyService;
import com.CachWeb.Cach.service.ExchangeRateService;
import com.CachWeb.Cach.service.ExchangeRequestService;
import com.CachWeb.Cach.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Value("${your.application.max-file-size}")
    private long maxFileSize;
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

    @PostMapping("/conferm")
    public String confermPost( @ModelAttribute("wallet")Wallet wallet ,HttpSession httpSession){


        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        exchangeRequest.setWallet(wallet);
        httpSession.setAttribute("exchangeRequest",exchangeRequest);


        return "redirect:/user/conferm";
    }
    @GetMapping("/conferm")
    public String conferm(Model model, Principal principal) {
        model.addAttribute("image", new Image());

        AuthController.test(model, principal, userService);
        return "user/conferm";
    }



    @PostMapping("/upload-image")
    public String handleImageUpload(@ModelAttribute Image image,
                                    @RequestParam("file") MultipartFile file,
                                    HttpSession httpSession,
                                    Principal principal ) throws UnsupportedEncodingException {
        try {
            if (!file.isEmpty()) {
                long fileSize = file.getSize();
                if (fileSize > maxFileSize) {
                    String encodedError = URLEncoder.encode("يجب ان يكون ان تكون الصورة اصغر من 3 ميقا", StandardCharsets.UTF_8.toString());
                    return "redirect:/user/conferm?error="+encodedError;
                }

                image.setData(file.getBytes());
                imageRepository.save(image);
            }

            ExchangeRequest exchangeRequest = (ExchangeRequest) httpSession.getAttribute("exchangeRequest");
            exchangeRequest.setImage(image);

            // Save the exchangeRequest
            boolean isAuthenticated = principal != null;
            if (isAuthenticated) {
                String username = principal.getName();
                User user = userService.findUserByEmail(username);
                exchangeRequest.setUser(user);
            }

            exchangeRequestService.Save(exchangeRequest);

            // Redirect after successful upload
            return "redirect:/user/thankyou";
        } catch (IOException e) {
            String encodedError = URLEncoder.encode("خطأ في المدخلات", StandardCharsets.UTF_8.toString());
            return "redirect:/user/conferm?error="+encodedError;
        }


    }



    @GetMapping("/thankyou")
    public String last( HttpSession httpSession  ,Model model , Principal principal){
        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        model.addAttribute("id",exchangeRequest.getId());
        AuthController.test(model, principal, userService);
        return "user/confirmation";
    }






//    private String handleImageUploadError(Model model) throws UnsupportedEncodingException {
//        String encodedError = URLEncoder.encode("حذث خطأ في رفع الصورة الرجاء التأكد من حجم الصورة", StandardCharsets.UTF_8.toString());
//        model.addAttribute("error", encodedError);
//        // Redirect to an error page
//        return "user/conferm";
//    }
    
//    @PostMapping("/upload-image")
//    public String handleImageUpload(@ModelAttribute Image image, @RequestParam("file") MultipartFile file, HttpSession httpSession, Principal principal, Model model) {
//        try {
//            // Your existing code
//
//            // Set success message if needed
//            model.addAttribute("successMessage", "File uploaded successfully.");
//
//            // Redirect after successful upload
//            return "redirect:/user/thankyou";
//        } catch (MaxUploadSizeExceededException ex) {
//            // Handle file size limit exceeded exception
//            model.addAttribute("errorMessage", "File size limit exceeded. Please choose a smaller file.");
//        } catch (Exception e) {
//            // Handle other exceptions
//            // Log the exception using a logging framework
//
//            // Set a generic error message
//            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
//        }
//
//        // Return to the same page to display error message
//        return "user/conferm";
//    }
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


