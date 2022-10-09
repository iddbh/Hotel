package com.sym.hotel.controller;

import com.sym.hotel.Service.LoginService;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/user/login")
    public ResponseResult Login(@RequestBody Guest guest){
        return loginService.login(guest);
    }
}
