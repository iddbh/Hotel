package com.sym.hotel.controller;

import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.Service.imp.MessageService;
import com.sym.hotel.Service.imp.addData;
import com.sym.hotel.Service.imp.returnClass.SerAndPri;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Message;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.Service.imp.returnClass.ReturnRecord;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
@CrossOrigin(origins = "*")
public class GuestController {
    @Autowired
    private GuestService guestService;

    @RequestMapping("/record")
    public List<ReturnRecord> viewRecord() {
        return guestService.viewRecord();
    }
    @RequestMapping("/chooseRoom")
    public SerAndPri chooseRoom(@RequestParam("room") Integer roomNum, @RequestParam("hotel") Integer hotelId){
        return guestService.serAndPri(roomNum, hotelId);
    }
    @RequestMapping("/modify")
    public ResponseResult modifyRecord(@RequestParam("record") Integer recordId, @RequestParam("room") Integer roomId){
        return guestService.modifyRecord(recordId, roomId);
    }
    @Autowired
    public com.sym.hotel.Service.imp.addData adddata;

    @Autowired
    private HotelService hotelService;

    //    @PostMapping("/modifyorder")
//    public ResponseResult ModifyOrder(@RequestBody Record record){
//        return hotelService.modifyOrder(record);
//    }
    @GetMapping("/add")
    public ResponseResult add(){
        adddata.add();
        return new ResponseResult(200,"ok","insert");
    }
    @PostMapping("/book")
    public ResponseResult Book(@RequestParam("room") Integer roomNum,@RequestParam("hotel") Integer hotelId, @RequestParam("startTime") String start, @RequestParam("endTime") String end) throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = fmt.parse(start);
        Date endTime = fmt.parse(end);
        return hotelService.book(roomNum,hotelId, startTime, endTime);
    }

    @PostMapping("/cancelorder")
    public ResponseResult CancelOrder(@RequestBody Record records) {
        return hotelService.cancelOrder(records);
    }
    @Autowired
    MessageService messageService;
    @GetMapping("/message/select")
    public List<Message> messageSelect(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest  = (LoginGuest) authentication.getPrincipal();
        Integer sendId = loginGuest.getGuest().getId();
        return messageService.getMessage(sendId);
    }
    @PostMapping("/message/gusetSend")
    public void messageInsert1( @RequestParam("message") String message, @RequestParam("time") String time) throws ParseException {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest  = (LoginGuest) authentication.getPrincipal();
        Integer sendId = loginGuest.getGuest().getId();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Time = fmt.parse(time);
        messageService.addMessage(sendId,message,Time);
    }
    @PostMapping("/message/manageSend")
    public void messageInsert2( @RequestParam("message") String message, @RequestParam("time") String time) throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Time = fmt.parse(time);
        messageService.addMessage(message,Time);
    }
}
