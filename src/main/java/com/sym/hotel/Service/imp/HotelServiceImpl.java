package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Util.JwtUtil;
import com.sym.hotel.Util.RedisCache;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.mapper.RecordMapper;
import com.sym.hotel.mapper.RoomMapper;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    RecordMapper recordMapper;
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult book(Room room, Date date) {
        //Todo:Test
        int guestId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Room> roomWrapper = new LambdaQueryWrapper<Room>()
                .eq(Room::getId, room.getId());
        Room room1 = roomMapper.selectOne(roomWrapper);
        if (Objects.isNull(room1)) {
            throw new RuntimeException("房间号不存在");
        }
        LambdaQueryWrapper<Record> recordWrapper = new LambdaQueryWrapper<Record>()
                .eq(Record::getRoomId, room.getId())
                .eq(Record::getBookTime, date);
        //Todo:Time
        Record selectedRecord = recordMapper.selectOne(recordWrapper);
        if (Objects.isNull(selectedRecord)) {
            throw new RuntimeException("时间冲突");
        }
        Record record = new Record(null, guestId, room.getId(), date);
        recordMapper.insert(record);
        return new ResponseResult(200,"ok");
    }
}
