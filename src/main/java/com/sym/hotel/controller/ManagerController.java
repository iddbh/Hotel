package com.sym.hotel.controller;

import com.sym.hotel.Service.ManagerService;
import com.sym.hotel.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @PostMapping("ChangeHotelINfo")
    public ResponseResult changeHotelInfo(@RequestParam("id") int id, @RequestParam("hotelId") int hotelId, @RequestParam("price") double price, @RequestParam("service") String service) {
        return managerService.changeHotelInfo(id, hotelId, price, service);
    }
}
