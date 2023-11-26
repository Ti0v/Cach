package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.entity.*;
import com.CachWeb.Cach.repository.ImageRepository;
import com.CachWeb.Cach.service.CurrencyService;
import com.CachWeb.Cach.service.ExchangeRateService;
import com.CachWeb.Cach.service.ExchangeRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CurrencyService currencyService;
   @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    /// Preview
    @GetMapping("/preview")
    public String getPreview(Model model, HttpSession httpSession ) {
        ExchangeRequest exchangeRequest = (ExchangeRequest) httpSession.getAttribute("exchangeRequest");

        if (exchangeRequest != null) {
            model.addAttribute("exchangeRequest", exchangeRequest);
        }
        model.addAttribute("wallet", new Wallet());

        return "user/Preview";
    }

    @PostMapping("/preview")
    public String postPreview( ExchangeRequest exchangeRequest, HttpSession httpSession) {
        httpSession.setAttribute("exchangeRequest", exchangeRequest);

        return "redirect:/user/preview";
    }



    /// add Prove
       @GetMapping("/conferm")
       public String conferm(Model model) {
        model.addAttribute("image", new Image());
        return "user/conferm";
    }



    @PostMapping("/upload-image")
    public String handleImageUpload(@ModelAttribute Image image, @RequestParam("file") MultipartFile file ,HttpSession httpSession ,Model model) {
        if (!file.isEmpty()) {
            try {
                image.setData(file.getBytes());
                imageRepository.save(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        exchangeRequest.setImage(image);
        httpSession.setAttribute("exchangeRequest",exchangeRequest);

        exchangeRequestService.Save(exchangeRequest);

        return "redirect:/user/thankyou";
    }

    @GetMapping("/thankyou")
    public String last( HttpSession httpSession  ,Model model){
        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        model.addAttribute("id",exchangeRequest.getId());
        return "user/confirmation";
    }

    @PostMapping("/conferm")
    public String confermPost( @ModelAttribute("wallet")Wallet wallet ,HttpSession httpSession){


        ExchangeRequest exchangeRequest = (ExchangeRequest)  httpSession.getAttribute("exchangeRequest");
        exchangeRequest.setWallet(wallet);
        httpSession.setAttribute("exchangeRequest",exchangeRequest);


        return "redirect:/user/conferm";
    }

//    @GetMapping("last")
//    public String finishReq( ){
//
//        return "";
//    }
//    @PostMapping("/initiate")
//    public String initiateExchange(@ModelAttribute ExchangeRequest exchangeRequest) {
//        exchangeRequestService.initiateExchange(exchangeRequest);
//        return "redirect:/user/form";
//    }




}

