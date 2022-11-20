package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Hotel;
import com.sym.hotel.pojo.Location;
import com.sym.hotel.pojo.Room;

import java.util.Date;

public interface HotelService {
    ResponseResult book(Room room, Date date);
    ResponseResult hotelsOfCity(Location location);
    ResponseResult hotelInfo(Hotel hotel);
}