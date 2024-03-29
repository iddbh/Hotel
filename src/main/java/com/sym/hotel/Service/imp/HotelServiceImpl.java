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
    CollectTableMapper collectTableMapper;
    @Autowired
    EvaluationMapper evaluationMapper;
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

    public ResponseResult cancelOrder(int recordId) {
        recordMapper.deleteById(recordId);
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
            return new ResponseResult(200, "ok", "积分余额不足");
        }
        double left = integral - goods.getIntegral();
        guestMapper.update(guest, new LambdaUpdateWrapper<Guest>().eq(Guest::getId, guestId).set(Guest::getIntegral, left));
        return new ResponseResult(200, "ok", "成功购买");
    }

    public ResponseResult topUp(double money) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        guestMapper.update(guest, new LambdaUpdateWrapper<Guest>().eq(Guest::getId, guestId).set(Guest::getBalance, guest.getBalance() + money));
        return new ResponseResult(200, "充值成功", (guest.getBalance() + money));
    }

    public double lookUpMoney() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        return guest.getBalance();
    }

    public ResponseResult collect(int id) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        Collecttable collectTable = collectTableMapper.selectOne(new LambdaQueryWrapper<Collecttable>().eq(Collecttable::getGuestId, guestId).eq(Collecttable::getHotelId, id));
        if (Objects.isNull(collectTable)) {
            Collecttable collectTable1 = new Collecttable();
            collectTable1.setHotelId(id);
            collectTable1.setGuestId(guestId);
            collectTable1.setStatus(1);
            collectTableMapper.insert(collectTable1);
            return new ResponseResult(200, "ok", "已收藏");
        } else {
            Integer status = collectTable.getStatus();
            if (status == 1) {
                collectTableMapper.update(collectTable, new LambdaUpdateWrapper<Collecttable>().eq(Collecttable::getGuestId, guestId).eq(Collecttable::getHotelId, id).set(Collecttable::getStatus, 0));
                return new ResponseResult(200, "ok", "已取消收藏");
            } else {
                collectTableMapper.update(collectTable, new LambdaUpdateWrapper<Collecttable>().eq(Collecttable::getGuestId, guestId).eq(Collecttable::getHotelId, id).set(Collecttable::getStatus, 1));
                return new ResponseResult(200, "ok", "已收藏");
            }
        }
    }

    public List<Integer> showStars() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        List<Collecttable> collectTables = collectTableMapper.selectList(new LambdaQueryWrapper<Collecttable>().eq(Collecttable::getGuestId, guestId).eq(Collecttable::getStatus, 1));
        List<Integer> idList = new ArrayList<>();
        for (Collecttable c : collectTables) {
            idList.add(c.getHotelId());
        }
        return idList;
    }

    public String evaluate(String statement, String picture, String score, String video, int recordId) {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Record record = recordMapper.selectOne(new LambdaQueryWrapper<Record>().eq(Record::getId, recordId));
        Room room = roomMapper.selectOne(new LambdaQueryWrapper<Room>().eq(Room::getId, record.getRoomId()));
        Type type = typeMapper.selectOne(new LambdaQueryWrapper<Type>().eq(Type::getId, room.getRoomTypeId()));
        int hotelId = type.getHotelId();
        Evaluation evaluation = new Evaluation();
        evaluation.setHotelId(hotelId);
        evaluation.setGuestId(guestId);
        evaluation.setPicture(picture);
        evaluation.setVideo(video);
        evaluation.setScore(Double.parseDouble(score));
        evaluation.setRecordId(recordId);
        evaluation.setStatement(statement);
        evaluationMapper.insert(evaluation);
        return "评价成功";
    }

    public List<Map<String, String>> showEvaluation(int hotelId) {
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        List<Evaluation> evaluations = evaluationMapper.selectList(new LambdaQueryWrapper<Evaluation>().eq(Evaluation::getHotelId, hotelId));
        for (Evaluation e : evaluations) {
            Record record = recordMapper.selectOne(new LambdaQueryWrapper<Record>().eq(Record::getId, e.getRecordId()));
            Map<String, String> stringStringMap = new HashMap<>();
            String temp1 = "";
            String temp2 = "";
            if (record.getBookStartTime().getMonth() + 1 >= 10) {
                temp1 = "-";
            } else {
                temp1 = "-0";
            }
            if (record.getBookStartTime().getDate() >= 10) {
                temp2 = "-";
            } else {
                temp2 = "-0";
            }
            stringStringMap.put("startTime", (record.getBookStartTime().getYear() + 1900) + temp1 + (record.getBookStartTime().getMonth() + 1) + temp2 + record.getBookStartTime().getDate());
            if (record.getBookEndTime().getMonth() + 1 >= 10) {
                temp1 = "-";
            } else {
                temp1 = "-0";
            }
            if (record.getBookEndTime().getDate() >= 10) {
                temp2 = "-";
            } else {
                temp2 = "-0";
            }
            stringStringMap.put("endTime", (record.getBookEndTime().getYear() + 1900) + temp1 + (record.getBookEndTime().getMonth() + 1) + temp2 + record.getBookEndTime().getDate());
            Room room = roomMapper.selectOne(new LambdaQueryWrapper<Room>().eq(Room::getId, record.getRoomId()));
            stringStringMap.put("roomNum", room.getRoomNum().toString());
            stringStringMap.put("hotelId", e.getHotelId().toString());
            stringStringMap.put("statement", e.getStatement());
            stringStringMap.put("picture", e.getPicture());
            stringStringMap.put("score", e.getScore().toString());
            stringStringMap.put("video", e.getVideo());
            stringStringMap.put("guestId", e.getGuestId().toString());
            stringStringMap.put("record", e.getRecordId().toString());
            maps.add(stringStringMap);
        }
        return maps;
    }

    public double lookUpIntegral() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer guestId = loginGuest.getGuest().getId();
        Guest guest = guestMapper.selectOne(new LambdaQueryWrapper<Guest>().eq(Guest::getId, guestId));
        return guest.getIntegral();
    }
}

