package com.sym.hotel.controller;

import com.sym.hotel.Service.imp.MessageService;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class MessageController {

//    @PostMapping("/delete")
//    public void messageSender(@RequestParam("send") int sendId, @RequestParam("receive") int receiveId){
//        messageService.getMessage(sendId,receiveId);
//    }
}
