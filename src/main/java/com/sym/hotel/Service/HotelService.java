package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Hotel;
import com.sym.hotel.pojo.Location;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.pojo.Room;
import com.sym.hotel.pojo.Type;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HotelService {
    ResponseResult book(Integer roomNum,Integer hotelId, Date date1,Date date2);
    ResponseResult hotelsOfCity(String location,String name);

    List<Type> hotelInfo(Hotel hotel);
    ResponseResult cancelOrder(int recordId);
    ResponseResult modifyOrder(Record record);
    ResponseResult topUp(double money);
    ResponseResult pointShopping(int id);
    ResponseResult collect(int hotelId);
    double lookUpMoney();
    List<Integer> showStars();
    String evaluate(String statement, String picture, String score, String video, int recordId);
    public List<Map<String, String>> showEvaluation(int hotelId);
}