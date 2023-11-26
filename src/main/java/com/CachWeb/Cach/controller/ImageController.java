//package com.CachWeb.Cach.controller;
//
//import com.CachWeb.Cach.entity.Image;
//import com.CachWeb.Cach.repository.ImageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;

//@Controller
//public class ImageController {
//
//    @Autowired
//    private ImageRepository imageRepository;

//    @GetMapping("/upload-image")
//    public String showUploadImageForm(Model model) {
//        model.addAttribute("image", new Image());
//        return "upload-image";
//    }
//
////    @PostMapping("/upload-image")
////    public String handleImageUpload(@ModelAttribute Image image, @RequestParam("file") MultipartFile file) {// Validate and process the uploaded image
////        if (!file.isEmpty()) {
////            try {
////                image.setData(file.getBytes());
////                imageRepository.save(image);
////            } catch (IOException e) {
////                e.printStackTrace(); // Handle the exception appropriately
////            }
////        }
////
////        return "redirect:/";
////    }
//
//    @GetMapping("/upload-image-success")
//    public String showUploadImageSuccess(Model model) {
//        List<Image> images = imageRepository.findAll();
//        model.addAttribute("images", images);
//        return "upload-image-success";
//    }
//
//    @GetMapping("/images/{id}")
//    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
//        Optional<Image> imageOptional = imageRepository.findById(id);
//
//        if (imageOptional.isPresent()) {
//            Image image = imageOptional.get();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG); // Change as per your image type
//            return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }


