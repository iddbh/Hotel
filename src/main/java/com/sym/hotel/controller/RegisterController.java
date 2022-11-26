package com.sym.hotel.controller;

import com.sym.hotel.Service.RegisterService;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @PostMapping("/user/register/verify")
    public String sendVerifyCode(@RequestParam("email") String code) throws MessagingException, IOException {
        return registerService.sendCode(code);
    }
    @PostMapping("/user/register/set")
    public ResponseResult register(@RequestBody Guest guest)  {
        return registerService.register(guest);
    }
    @PostMapping("/user/manager")
    public ResponseResult registerAsManager(@RequestBody Manager manager)
    {
        return registerService.registerAsManager(manager);
    }
}
