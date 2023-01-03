package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.Service.HotelService;
import com.sym.hotel.Util.RedisCache;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.mapper.*;
import com.sym.hotel.pojo.*;
import com.sym.hotel.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    GuestMapper guestMapper;
    @Autowired
    PointshopMapper pointshopMapper;
    @Autowired
    RedisCache redisCache;

    public Map selectUserInfo(Integer userId) {
        //Token:userId
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, userId));
        Map<String, Number> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("balance", guest.getBalance());
        userInfo.put("Integral", guest.getIntegral());
        return userInfo;
    }

    @Override
    public ResponseResult book(Integer roomNum, Integer hotelId, Date start, Date end) {
//        int guestId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Room room = roomMapper.selectOne(
                new MPJLambdaWrapper<Room>().selectAll(Room.class)
                        .eq(Room::getRoomNum, roomNum).leftJoin(Type.class, Type::getId, Room::getRoomTypeId).eq(Type::getHotelId, hotelId));

//        LambdaQueryWrapper<Room> roomWrapper = new LambdaQueryWrapper<Room>()
//                .eq(Room::getId, roomId);
//        Room room1 = roomMapper.selectOne(roomWrapper);
        if (Objects.isNull(room)) {
            return new ResponseResult(200, "ok", "房间不存在");
        }
        Integer roomId = room.getId();
//        LambdaQueryWrapper<Record> recordWrapper = new LambdaQueryWrapper<Record>()
//                .eq(Record::getRoomId, room.getId())
//                .eq(Record::getBookTime, date);
        List<Record> selectedRecord = recordMapper.selectList(
                new MPJLambdaWrapper<Record>().selectAll(Record.class)
                        .eq(Record::getRoomId, roomId)
                        .and(x -> x.lt(Record::getBookStartTime, end).ge(Record::getBookEndTime, end)
                                .or(e -> e.le(Record::getBookStartTime, start).gt(Record::getBookEndTime, start)).or(e -> e.gt(Record::getBookStartTime, start).lt(Record::getBookEndTime, end))));

//        Record selectedRecord = recordMapper.selectOne(recordWrapper);
        if (selectedRecord.size() != 0) {
            return new ResponseResult(200, "ok", "时间冲突");
        }
        Record record = new Record();
        record.setGuestId(guestId);
        record.setRoomId(roomId);
        record.setBookStartTime(start);
        record.setBookEndTime(end);
        recordMapper.insert(record);
        Integer roomTypeId = room.getRoomTypeId();
        Type type = typeMapper.selectOne(new LambdaQueryWrapper<Type>().eq(Type::getId, roomTypeId));
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        long l = (end.getTime() - start.getTime()) / 1000 / 3600 / 24 + 1;
        if (guest.getBalance() < type.getPrice() * l) {
            return new ResponseResult(200, "ok", "余额不足");
        }

        double nowBalance = guest.getBalance() - type.getPrice() * l;
        double nowIntegral = guest.getIntegral() + type.getPrice() * l;
        guestMapper.update(guest, new LambdaUpdateWrapper<Guest>().eq(Guest::getId, guestId).set(Guest::getBalance, nowBalance).set(Guest::getIntegral, nowIntegral));
        return new ResponseResult(200, "ok", "成功预定");
    }

    @Override
    public ResponseResult hotelsOfCity(String location, String name) {
        //Todo:Test
        String province = location.split(",")[0];
        String city = location.split(",")[1];

        List<Hotel> allHotelsOfCity = hotelMapper.selectJoinList(Hotel.class, new MPJLambdaWrapper<Hotel>().selectAll(Hotel.class)
                .leftJoin(Location.class, Location::getId, Hotel::getLocationId).eq(Location::getCity, city).eq(Location::getProvince, province).like(Hotel::getName, name));
        if (allHotelsOfCity == null) {
            return new ResponseResult(200, "该地区酒店不存在");
        }
        List<String> ID = new LinkedList<>();
        for (int i = 0; i < allHotelsOfCity.size(); i++) {
            ID.add(String.valueOf(allHotelsOfCity.get(i).getId()));
        }
        return new ResponseResult(200, "OK", ID);
    }

    public ResponseResult cancelOrder(Record record) {
        recordMapper.deleteById(record);
        return new ResponseResult(200, "ok", "删除成功");
    }

    public ResponseResult modifyOrder(Record record) {

        return new ResponseResult(200, "ok", "需要续交" + "元");
    }

    @Override
    public List<Type> hotelInfo(Hotel hotel) {
        int hotelId = hotel.getId();
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<Type>().eq(Type::getHotelId, hotelId);
        List<Type> allTypeOfHotel = typeMapper.selectList(typeLambdaQueryWrapper);
        return allTypeOfHotel;
    }

    public ResponseResult pointShopping(int id) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        Double integral = guest.getIntegral();
        Pointshop goods = pointshopMapper.selectOne(new LambdaQueryWrapper<Pointshop>().eq(Pointshop::getId, id));
        if (goods.getIntegral() > integral) {
            return new ResponseResult(200,"ok","积分余额不足");
        }
        double left=integral-goods.getIntegral();
        guestMapper.update(guest,new LambdaUpdateWrapper<Guest>().set(Guest::getIntegral,left));
        return new ResponseResult(200,"ok","成功购买");
    }
    public ResponseResult topUp(double money){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        guestMapper.update(guest,new LambdaUpdateWrapper<Guest>().eq(Guest::getId,guestId).set(Guest::getBalance,guest.getBalance()+money));
        return new ResponseResult(200,"ok","充值成功，余额为"+(guest.getBalance()+money));
    }
}
