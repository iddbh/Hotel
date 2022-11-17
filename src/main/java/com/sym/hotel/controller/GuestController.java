package com.sym.hotel.controller;

import com.sym.hotel.Service.imp.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @Autowired
    private GuestService guestService;

}
