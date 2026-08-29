package com.ehb.banking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/api/status")
    public String getStatus() {
        return "Hello, from Edinburgh Hanoverian Bank";
    }



}

