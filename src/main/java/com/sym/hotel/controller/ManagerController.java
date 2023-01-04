package com.sym.hotel.controller;

import com.sym.hotel.Service.ManagerService;
import com.sym.hotel.Service.imp.ManagerServiceImp;
import com.sym.hotel.Service.imp.returnClass.Analyse;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/manager")
@CrossOrigin(origins = "*")
public class ManagerController {
    @Autowired
    private ManagerServiceImp managerService;

    @PostMapping("/ChangeHotelInfo")
    public ResponseResult changeHotelInfo(@RequestParam("id") int id, @RequestParam("hotelId") int hotelId, @RequestParam("price") double price, @RequestParam("service") String service) {
        return managerService.changeHotelInfo(id, hotelId, price, service);
    }
}
