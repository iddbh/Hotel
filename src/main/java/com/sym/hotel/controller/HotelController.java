package com.sym.hotel.controller;

import com.sym.hotel.Service.HotelService;

import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.domain.ResponseResult;

import com.sym.hotel.pojo.Hotel;
import com.sym.hotel.pojo.Location;
import com.sym.hotel.pojo.Room;
import com.sym.hotel.pojo.Type;
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
    public ResponseResult Book(@RequestBody Room room, @RequestBody Date start,@RequestBody Date end){
        return hotelService.book(room,start,end);
    }
    @PostMapping("/selectHotel")
    public ResponseResult hotelsOfCity(@RequestParam("local") String location,@RequestParam("name") String name){
        return hotelService.hotelsOfCity(location,name);
    }
    @PostMapping("hotelInfo")
    public List<Type> hotelInfo(@RequestBody Hotel hotel){
        return hotelService.hotelInfo(hotel);
    }

}
