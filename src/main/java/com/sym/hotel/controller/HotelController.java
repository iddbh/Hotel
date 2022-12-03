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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public ResponseResult Book(@RequestParam("room") Integer roomId, @RequestParam("startTime") String start,@RequestParam("endTime") String end) throws ParseException {
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = fmt.parse(start);
        Date endTime = fmt.parse(end);
        return hotelService.book(roomId,startTime,endTime);
    }
    @PostMapping("/selectHotel")
    public ResponseResult hotelsOfCity(@RequestParam("local") String location,@RequestParam("name") String name){
        return hotelService.hotelsOfCity(location,name);
    }
    @PostMapping("/selectRoom")
    public List<Integer> hotelInMoneyRange(@RequestParam("minMoney") Double minMoney, @RequestParam("maxMoney") Double maxMoney,
                                        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) throws ParseException {
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
        Date start = fmt.parse(startTime);
        Date end = fmt.parse(endTime);
        return guestService.selectRoom(minMoney,maxMoney,start,end);

    }
    @PostMapping("hotelInfo")
    public List<Type> hotelInfo(@RequestBody Hotel hotel){

        return hotelService.hotelInfo(hotel);
    }

}
