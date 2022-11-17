package com.sym.hotel.controller;

import com.sym.hotel.Service.HotelService;

import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.domain.ResponseResult;

import com.sym.hotel.pojo.Location;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class HotelController {
    @Autowired
    private HotelService hotelService;
    @Autowired GuestService guestService;
    @PostMapping("/book")
    public ResponseResult Book(@RequestBody Room room, @RequestBody Date date){
        return hotelService.book(room,date);
    }
    @PostMapping("/selectHotel")
    public ResponseResult hotelsOfCity(@RequestBody Location location){
        return hotelService.hotelsOfCity(location);
    }

}
