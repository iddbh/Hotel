package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Type;

import java.util.List;

public interface ManagerService {
    ResponseResult  changeHotelInfo (int id, int hotelId, double price,String Service);
   List<Record> selectRecordInfo(int guestId,int startTime,int endTime);
}
