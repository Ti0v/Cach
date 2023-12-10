package com.CachWeb.Cach.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException ex ) throws UnsupportedEncodingException {
        String encodedError = URLEncoder.encode("يجب ان يكون ان تكون الصورة اصغر من 3 ميقا", StandardCharsets.UTF_8.toString());

        return "redirect:/user/conferm?error="+encodedError;

    }
}
