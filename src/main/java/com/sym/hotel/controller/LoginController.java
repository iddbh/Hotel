package com.sym.hotel.controller;

import com.sym.hotel.Service.LoginService;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    

    @PostMapping("/user/login")
    public ResponseResult Login(@RequestParam("username") String username,@RequestParam("password") String password){
        return loginService.login(username, password);
    }
    @RequestMapping("/user/logout")
    public ResponseResult Logout(){
        return loginService.logout();
    }
}
