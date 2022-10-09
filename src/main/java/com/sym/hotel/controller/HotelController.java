package com.sym.hotel.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelController {

    @ResponseBody
    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('test')")
    public String hello(){
        return "Hello!";
    }

    @ResponseBody
    @RequestMapping("/")
    public String mainPage(){
        return "This is the hotel";
    }

    @RequestMapping("/pay")
    public String pay(){
        return "pay";
    }




}
