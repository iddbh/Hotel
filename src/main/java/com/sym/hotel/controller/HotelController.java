package com.sym.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HotelController {

    @ResponseBody
    @RequestMapping("/hello")
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
