package com.sym.hotel.controller;

import com.sym.hotel.Service.ManagerService;
import com.sym.hotel.Service.imp.ManagerServiceImp;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class ManagerController {
    @Autowired
    private ManagerServiceImp managerService;

    @PostMapping("ChangeHotelInfo")
    public ResponseResult changeHotelInfo(@RequestParam("id") int id, @RequestParam("hotelId") int hotelId, @RequestParam("price") double price, @RequestParam("service") String service) {
        return managerService.changeHotelInfo(id, hotelId, price, service);
    }

    @PostMapping("SelectRecordInfo")
    public List<Record> selectRecordInfo(@RequestParam("guestId") int guestId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        return managerService.selectRecordInfo(guestId, startTime, endTime);
    }
}
