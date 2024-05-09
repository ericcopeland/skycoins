package com.copelando.skycoins.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping(value = {
        "/",
        "/products",
        "/products/**",
        "/auctions",
        "/auctions/**",
        "/statistics",
        "/favorites"
    })
    String index() {
        return "index";
    }
}
