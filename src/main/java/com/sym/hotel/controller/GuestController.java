package com.sym.hotel.controller;

import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.Service.imp.returnClass.ReturnRecord;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/guest")
@CrossOrigin
public class GuestController {
    @Autowired
    private GuestService guestService;

    @RequestMapping("/record")
    public List<ReturnRecord> viewRecord() {
        return guestService.viewRecord();
    }

    @Autowired
    private HotelService hotelService;

    //    @PostMapping("/modifyorder")
//    public ResponseResult ModifyOrder(@RequestBody Record record){
//        return hotelService.modifyOrder(record);
//    }
    @PostMapping("/book")
    public ResponseResult Book(@RequestParam("room") Integer roomId, @RequestParam("startTime") String start, @RequestParam("endTime") String end) throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = fmt.parse(start);
        Date endTime = fmt.parse(end);
        return hotelService.book(roomId, startTime, endTime);
    }

    @PostMapping("/cancelorder")
    public ResponseResult CancelOrder(@RequestBody Record records) {
        return hotelService.cancelOrder(records);
    }
}
