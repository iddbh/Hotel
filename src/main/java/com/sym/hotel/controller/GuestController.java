package com.sym.hotel.controller;

import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.Service.imp.returnClass.ReturnRecord;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @Autowired
    private GuestService guestService;

    @RequestMapping("/record")
    public List<ReturnRecord> viewRecord(){
        return guestService.viewRecord();
    }
}
