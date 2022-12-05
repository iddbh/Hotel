package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Util.RedisCache;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.mapper.HotelMapper;
import com.sym.hotel.mapper.RecordMapper;
import com.sym.hotel.mapper.RoomMapper;
import com.sym.hotel.mapper.TypeMapper;
import com.sym.hotel.pojo.*;
import com.sym.hotel.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    TypeMapper typeMapper;
    @Autowired
    RedisCache redisCache;

    @Override
    public ResponseResult book(Integer roomId, Date start,Date end) {
        //Todo:Test
//        int guestId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int guestId=14;
        LambdaQueryWrapper<Room> roomWrapper = new LambdaQueryWrapper<Room>()
                .eq(Room::getId, roomId);
        Room room1 = roomMapper.selectOne(roomWrapper);
        if (Objects.isNull(room1)) {
            return new ResponseResult(200, "ok", "房间不存在");
        }
//        LambdaQueryWrapper<Record> recordWrapper = new LambdaQueryWrapper<Record>()
//                .eq(Record::getRoomId, room.getId())
//                .eq(Record::getBookTime, date);
        //Todo:Time
        Record selectedRecord=recordMapper.selectOne(
                new MPJLambdaWrapper<Record>().selectAll(Record.class)
                        .eq(Record::getRoomId, roomId)
                        .and(x->x.lt(Record::getBookStartTime,end).ge(Record::getBookEndTime,end)
                                .or(e->e.le(Record::getBookStartTime,start).gt(Record::getBookEndTime,start)).or(e->e.gt(Record::getBookStartTime,start).lt(Record::getBookEndTime,end))));

//        Record selectedRecord = recordMapper.selectOne(recordWrapper);
        if (!Objects.isNull(selectedRecord)) {
            return new ResponseResult(200, "ok", "时间冲突");
        }
        Record record=new Record();
        record.setGuestId(guestId);
        record.setRoomId(roomId);
        record.setBookStartTime(start);
        record .setBookEndTime(end);
        recordMapper.insert(record);
        return new ResponseResult(200, "ok");
    }

    @Override
    public ResponseResult hotelsOfCity(String location, String name) {
        //Todo:Test
        String province=location.split(",")[0];
        String city=location.split(",")[1];

        List<Hotel> allHotelsOfCity =hotelMapper.selectJoinList(Hotel.class, new MPJLambdaWrapper<Hotel>().selectAll(Hotel.class)
                .leftJoin(Location.class,Location::getId,Hotel::getLocationId).eq(Location::getCity, city).eq(Location::getProvince,province).like(Hotel::getName,name));
        if (allHotelsOfCity==null){
            return new ResponseResult(200,"该地区酒店不存在");
        }
        List<String> ID=new LinkedList<>();
        for (int i = 0; i < allHotelsOfCity.size(); i++) {
            ID.add(String.valueOf(allHotelsOfCity.get(i).getId()));
        }
        return new ResponseResult(200,"OK",ID);
    }

    public ResponseResult cancelOrder(Record record){
            recordMapper.deleteById(record);
        return new ResponseResult(200,"ok","删除成功");
    }
    public ResponseResult modifyOrder(Record record){

        return new ResponseResult(200,"ok","需要续交"+"元");
    }

    @Override
    public List<Type> hotelInfo(Hotel hotel) {
        int hotelId = hotel.getId();
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<Type>().eq(Type::getHotelId, hotelId);
        List<Type> allTypeOfHotel = typeMapper.selectList(typeLambdaQueryWrapper);
        return allTypeOfHotel;
    }
}
