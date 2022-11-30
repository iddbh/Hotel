package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Hotel;
import com.sym.hotel.pojo.Location;
import com.sym.hotel.pojo.Room;
import com.sym.hotel.pojo.Type;

import java.util.Date;
import java.util.List;

public interface HotelService {
    ResponseResult book(Room room, Date date1,Date date2);
    ResponseResult hotelsOfCity(String location,String name);
    List<Type> hotelInfo(Hotel hotel);
}