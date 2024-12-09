package com.example.Library_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainControl {
    @GetMapping("/")
    public String home() {
        return "main";
    }
}
