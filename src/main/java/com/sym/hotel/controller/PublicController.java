package com.sym.hotel.controller;

import com.sym.hotel.Service.imp.GuestService;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private GuestService guestService;

}
