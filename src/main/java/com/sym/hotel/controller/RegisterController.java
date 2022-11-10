package com.sym.hotel.controller;

import com.sym.hotel.Service.RegisterService;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @PostMapping("/user/register")
    public ResponseResult register(@RequestBody Guest guest)
    {
        return registerService.register(guest);
    }
    @PostMapping("/user/manager")
    public ResponseResult registerAsManager(@RequestBody Manager manager)
    {
        return registerService.registerAsManager(manager);
    }
}
