package com.sym.hotel.controller;

import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pubic")
public class PublicController {
    @Autowired
    GuestService guestService;
    @PostMapping("/selectHotel/money")
    public ResponseResult hotelInMoneyRange(@RequestParam("minMoney") Double minMoney, @RequestParam("maxMoney") Double maxMoney){
        return new ResponseResult(200, "查询成功", Map.of("个数", guestService.selectRoomByPrice(minMoney,maxMoney).size()));
    }
}
