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

    @PostMapping("/selectRecordInfo")
    public List<Record> selectRecordInfo(@RequestParam("hotelId") Integer hotelId,
                                         @RequestParam(value = "guestId", defaultValue = "-1") int guestId,
                                         @RequestParam(value = "startTime", defaultValue = "1999-01-01") String start,
                                         @RequestParam(value = "endTime", defaultValue = "2100-12-31") String end,
                                         @RequestParam(value = "roomNum", defaultValue = "-1") Integer roomNum) throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = fmt.parse(start);
        Date endTime = fmt.parse(end);
        if(roomNum != -1)
            return managerService.selectRecordInfo(guestId, startTime, endTime);
        return managerService.recordByRoom(roomNum, hotelId, guestId, startTime, endTime);
    }

    @PostMapping("/moneyAnalyse")
    public List<Analyse> moneyGet(@RequestParam("hotelId") Integer hotelId, @RequestParam("startTime") String start, @RequestParam("endTime") String end) throws ParseException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = fmt.parse(start);
        Date endTime = fmt.parse(end);
        return managerService.moneyGet(hotelId, startTime, endTime);
    }
}
