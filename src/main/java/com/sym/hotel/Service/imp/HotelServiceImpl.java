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
    public ResponseResult book(Room room, Date start,Date end) {
        //Todo:Test
        int guestId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Room> roomWrapper = new LambdaQueryWrapper<Room>()
                .eq(Room::getId, room.getId());
        Room room1 = roomMapper.selectOne(roomWrapper);
        if (Objects.isNull(room1)) {
            throw new RuntimeException("房间号不存在");
        }
//        LambdaQueryWrapper<Record> recordWrapper = new LambdaQueryWrapper<Record>()
//                .eq(Record::getRoomId, room.getId())
//                .eq(Record::getBookTime, date);
        //Todo:Time
        Record selectedRecord=recordMapper.selectOne(
                new MPJLambdaWrapper<Record>().selectAll(Record.class)
                        .eq(Record::getRoomId, room.getId())
                        .and(x->x.lt(Record::getBookStartTime,end).ge(Record::getBookEndTime,end)
                                .or(e->e.le(Record::getBookStartTime,start).gt(Record::getBookEndTime,start))));

//        Record selectedRecord = recordMapper.selectOne(recordWrapper);
        if (Objects.isNull(selectedRecord)) {
            throw new RuntimeException("时间冲突");
        }
        Record record=new Record();
        record.setGuestId(guestId);
        record.setRoomId(room.getId());
        record.setBookStartTime(start);
        record .setBookEndTime(end);
        recordMapper.insert(record);
        return new ResponseResult(200, "ok");
    }

    @Override
    public ResponseResult hotelsOfCity(Location location) {
        //Todo:Test
        int locationId = location.getId();
        LambdaQueryWrapper<Hotel> hotelLambdaQueryWrapper = new LambdaQueryWrapper<Hotel>().eq(Hotel::getLocationId, locationId);
        List<Hotel> allHotelsOfCity = hotelMapper.selectList(hotelLambdaQueryWrapper);
        Map<String, String> maps = new HashMap<>();
        for (int i = 0; i < allHotelsOfCity.size(); i++) {
            maps.put("Hotels"+(i+1), String.valueOf(allHotelsOfCity.get(i)));
        }
        return new ResponseResult(200,"OK",maps);
    }

    @Override
    public List<Type> hotelInfo(Hotel hotel) {
        int hotelId = hotel.getId();
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<Type>().eq(Type::getHotelId, hotelId);
        List<Type> allTypeOfHotel = typeMapper.selectList(typeLambdaQueryWrapper);
        return allTypeOfHotel;
    }
}
