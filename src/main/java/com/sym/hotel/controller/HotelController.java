package com.sym.hotel.controller;

import com.sym.hotel.Service.HotelService;

import com.sym.hotel.Service.HotelService;
import com.sym.hotel.domain.ResponseResult;

import com.sym.hotel.pojo.Location;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class HotelController {
    @Autowired
    private HotelService hotelService;
    @PostMapping("/user/book")
    public ResponseResult Book(@RequestBody Room room, @RequestBody Date date){
        return hotelService.book(room,date);
    }
    @PostMapping("user/selectHotel")
    public ResponseResult hotelsOfCity(@RequestBody Location location){
        return hotelService.hotelsOfCity(location);
    }


}
