package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.mapper.RecordMapper;
import com.sym.hotel.mapper.TypeMapper;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.pojo.Room;
import com.sym.hotel.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ManagerServiceImp {
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private RecordMapper recordMapper;

    public ResponseResult changeHotelInfo(int id, int hotelId, double price, String message) {
        //返回一个type
        Type oldType = typeMapper.selectOne(new LambdaQueryWrapper<Type>()
                .eq(Type::getId, id)
                .eq(Type::getHotelId, hotelId));
        if (Objects.isNull(oldType)) {
            return new ResponseResult(403, "数据不存在");
        }
        LambdaUpdateWrapper<Type> updateWrapper = new LambdaUpdateWrapper<Type>()
                .set(Type::getPrice, price)
                .set(Type::getService, message)
                .eq(Type::getId, id)
                .eq(Type::getHotelId, hotelId);
        typeMapper.update(oldType, updateWrapper);
        return new ResponseResult(200, "更改成功");
    }
    public List<Record> selectRecordInfo(int guestId, Date startTime, Date endTime) {
        LambdaQueryWrapper<Record> recordLambdaQueryWrapper;
        if(guestId == -1) {
            recordLambdaQueryWrapper = new LambdaQueryWrapper<Record>()
                    .le(Record::getBookStartTime, startTime)
                    .ge(Record::getBookEndTime, endTime);
        }
        else {
            recordLambdaQueryWrapper = new LambdaQueryWrapper<Record>()
                    .eq(Record::getGuestId, guestId)
                    .le(Record::getBookStartTime, startTime)
                    .ge(Record::getBookEndTime, endTime);
        }
        return recordMapper.selectList(recordLambdaQueryWrapper);
    }

    // 直接默认能走到这就是超级大管理员了，啥都返回吧，摆了
    public List<Record> recordByRoom(int roomNum, int hotelId, int guestId, Date startTime, Date endTime){
        //Todo: test
        List<Record> recordList;
        if(guestId == -1) {
            recordList = recordMapper.selectJoinList(Record.class, new MPJLambdaWrapper<Record>()
                    .selectAll(Record.class).leftJoin(Room.class, Room::getId, Record::getRoomId).eq(Room::getRoomNum, roomNum)
                    .le(Record::getBookStartTime, startTime)
                    .ge(Record::getBookEndTime, endTime));
        }
        else
            recordList = recordMapper.selectJoinList(Record.class, new MPJLambdaWrapper<Record>()
                .selectAll(Record.class).leftJoin(Room.class, Room::getId, Record::getRoomId).eq(Room::getRoomNum, roomNum)
                    .eq(Record::getGuestId, guestId)
                    .le(Record::getBookStartTime, startTime)
                    .ge(Record::getBookEndTime, endTime));
        return recordList;
    }
}
