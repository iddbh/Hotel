package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.Service.imp.returnClass.Analyse;
import com.sym.hotel.Service.imp.returnClass.ReturnRecord;
import com.sym.hotel.Service.imp.returnClass.Selected;
import com.sym.hotel.Service.imp.returnClass.SerAndPri;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.mapper.*;
import com.sym.hotel.pojo.*;
import com.sym.hotel.pojo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sym.hotel.domain.ResponseResult;


import java.util.*;

@Service
public class GuestService implements UserDetailsService {
    @Autowired
    private GuestMapper guestMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private HotelMapper hotelMapper;
//    private UserinfoMapper userinfoMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<Guest> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Guest::getName, username);
        Guest guest = guestMapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(guest)) {
            throw new RuntimeException("用户名或者密码错误");
        }
        //如果没有查询到用户
        //TODO:查询权限信息
        List<String> list = new ArrayList<>(Arrays.asList("test", "admin"));
        //把数据封装成UserDetails返回
        return new LoginGuest(guest, list);
    }

    public List<Integer> selectRoom(Integer hotelId, Double minMoney, Double maxMoney, Date startTime, Date endTime) {
        List<Room> list = roomMapper.selectJoinList(Room.class,
                new MPJLambdaWrapper<Room>().selectAll(Room.class).leftJoin(Type.class, Type::getId, Room::getRoomTypeId).select(Type::getPrice)
                        .eq(Type::getHotelId, hotelId).gt(Type::getPrice, minMoney).le(Type::getPrice, maxMoney));
        List<Room> listDel = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listDel = roomMapper.selectJoinList(Room.class, new MPJLambdaWrapper<Room>().selectAll(Room.class).
                    leftJoin(Record.class, Record::getRoomId, Room::getId).
                    select(Record::getBookStartTime, Record::getBookEndTime).
                    ge(Record::getBookStartTime, startTime).lt(Record::getBookEndTime, startTime).or().lt(Record::getBookStartTime, endTime).ge(Record::getBookEndTime, endTime).or().gt(Record::getBookStartTime, startTime).lt(Record::getBookEndTime, endTime));
        }
        HashSet h1 = new HashSet(list);
        HashSet h2 = new HashSet(listDel);
        h1.removeAll(h2);
        list.clear();
        list.addAll(h1);
        List<Integer> listNum = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listNum.add(list.get(i).getRoomNum());
        }
        return listNum;
    }

    public List<ReturnRecord> viewRecord() {
        // 这里没有使用连表查询，可以改进。屎山！！！！！！！
        UsernamePasswordAuthenticationToken authentication;
        try {
            authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            return null;
        }
        LoginGuest loginGuest = (LoginGuest) authentication.getPrincipal();
        Integer id = loginGuest.getGuest().getId();
        LambdaQueryWrapper<Record> recordLambdaQueryWrapper = new LambdaQueryWrapper<Record>().eq(Record::getGuestId, id);
        List<Record> recordList = recordMapper.selectList(recordLambdaQueryWrapper);
        List<ReturnRecord> rrl = new ArrayList<>();
        for (Record re : recordList) {
            int roomId = re.getRoomId();
            LambdaQueryWrapper<Room> roomLambdaQueryWrapper = new LambdaQueryWrapper<Room>().eq(Room::getId, roomId);
            Room room = roomMapper.selectOne(roomLambdaQueryWrapper);
            int typeId = room.getRoomTypeId();
            LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<Type>().eq(Type::getId, typeId);
            Type type = typeMapper.selectOne(typeLambdaQueryWrapper);
            String typeInfo = type.getRoomType();
            double price = type.getPrice();
            int hotelId = type.getHotelId();
            LambdaQueryWrapper<Hotel> hotelLambdaQueryWrapper = new LambdaQueryWrapper<Hotel>().eq(Hotel::getId, hotelId);
            Hotel hotel = hotelMapper.selectOne(hotelLambdaQueryWrapper);
            int recordId = re.getId();
            boolean isOver = (new Date().getTime() < re.getBookEndTime().getTime());
            rrl.add(new ReturnRecord(id, roomId, price, typeInfo, hotelId, re.getBookStartTime(), re.getBookEndTime(), recordId, isOver, room.getRoomNum()));
        }
        return rrl;
    }

    public SerAndPri serAndPri(int roomNum, int hotelId) {
        // 连表查询！
        Type type = typeMapper.selectJoinOne(Type.class, new MPJLambdaWrapper<Type>().selectAll(Type.class).leftJoin(Room.class, Room::getRoomTypeId, Type::getId).eq(Room::getRoomNum, roomNum).eq(Type::getHotelId, hotelId));
        return new SerAndPri(type.getService(), type.getPrice());
    }

    // 管理员修改酒店信息
    public ResponseResult modifyRoom(int roomNum, int hotelId, double price, String service) {
        Type type = typeMapper.selectJoinOne(Type.class, new MPJLambdaWrapper<Type>().selectAll(Type.class).leftJoin(Room.class, Room::getRoomTypeId, Type::getId).eq(Room::getRoomNum, roomNum).eq(Type::getHotelId, hotelId));
        typeMapper.update(type, new LambdaUpdateWrapper<Type>().eq(Type::getId, type.getId()).set(Type::getPrice, price).set(Type::getService, service));
        return new ResponseResult(200, "修改成功");
    }

    public ResponseResult bookRoom(Record recordNew) {
        Record record = null;
        record = recordMapper.selectOne(
                new MPJLambdaWrapper<Record>().selectAll(Record.class)
                        .lt(Record::getBookStartTime, recordNew.getBookEndTime()).ge(Record::getBookEndTime, recordNew.getBookEndTime())
                        .or().le(Record::getBookStartTime, recordNew.getBookStartTime()).gt(Record::getBookEndTime, recordNew.getBookStartTime()));
        if (record == null) {
//           int count = userMapper.insert(userEntity);
            return new ResponseResult(200, "预定成功");
        } else {
            return new ResponseResult(200, "此房间该时段内已被预订，请您重新选择");
        }
    }

    // 直接默认能走到这就是超级大管理员了，啥都返回吧，摆了
    public List<Selected> recordByRoom(int roomNum, int hotelId, int guestId, Date startTime, Date endTime){
        List<Record> recordList;
        if(roomNum == -1){
            LambdaQueryWrapper<Record> recordLambdaQueryWrapper;
            if(guestId == -1) {
                recordLambdaQueryWrapper = new LambdaQueryWrapper<Record>()
                        .le(Record::getBookStartTime, endTime)
                        .ge(Record::getBookEndTime, startTime);
            }
            else {
                recordLambdaQueryWrapper = new LambdaQueryWrapper<Record>()
                        .eq(Record::getGuestId, guestId)
                        .le(Record::getBookStartTime, endTime)
                        .ge(Record::getBookEndTime, startTime);
            }
            recordList =  recordMapper.selectList(recordLambdaQueryWrapper);
        }
        else {
            if (guestId == -1) {
                recordList = recordMapper.selectJoinList(Record.class, new MPJLambdaWrapper<Record>()
                        .selectAll(Record.class).leftJoin(Room.class, Room::getId, Record::getRoomId)
                        .leftJoin(Type.class, Type::getId, Room::getRoomTypeId)
                        .leftJoin(Hotel.class, Hotel::getId, Type::getHotelId).eq(Room::getRoomNum, roomNum)
                        .eq(Hotel::getId, hotelId)
                        .le(Record::getBookStartTime, endTime)
                        .ge(Record::getBookEndTime, startTime));
            } else
                recordList = recordMapper.selectJoinList(Record.class, new MPJLambdaWrapper<Record>()
                        .selectAll(Record.class).leftJoin(Room.class, Room::getId, Record::getRoomId)
                        .leftJoin(Type.class, Type::getId, Room::getRoomTypeId)
                        .leftJoin(Hotel.class, Hotel::getId, Type::getHotelId).eq(Room::getRoomNum, roomNum)
                        .eq(Hotel::getId, hotelId)
                        .eq(Record::getGuestId, guestId)
                        .le(Record::getBookStartTime, endTime)
                        .ge(Record::getBookEndTime, startTime));
        }
        List<Selected> selectedList = new ArrayList<>();
        for(Record r : recordList){
            Room room = roomMapper.selectOne(new LambdaQueryWrapper<Room>().eq(Room::getId, r.getRoomId()));
            boolean isOver = (new Date().getTime() < r.getBookEndTime().getTime());
            Type type = typeMapper.selectOne(new LambdaQueryWrapper<Type>().eq(Type::getId, room.getRoomTypeId()));
            selectedList.add(new Selected(r.getId(), r.getGuestId(), room.getRoomNum(), r.getBookStartTime(), r.getBookEndTime(),type.getPrice(), isOver));
        }
        return selectedList;
    }

    // 营业额分析，摆了
    public List<Analyse> moneyGet(int hotelId, Date startTime, Date endTime){
        List<Analyse> returnList = new ArrayList<>();
        List<Type> roomTypes = typeMapper.selectList(new LambdaQueryWrapper<Type>().eq(Type::getHotelId, hotelId));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startTime);
        for(Date d = startTime; d.before(endTime);){
            double money = 0.0;
            for(Type t : roomTypes){
                String typeName = t.getRoomType();
                List<Record> recordList = recordMapper.selectJoinList(Record.class, new MPJLambdaWrapper<Record>()
                        .selectAll(Record.class).leftJoin(Room.class, Room::getId, Record::getRoomId)
                        .leftJoin(Type.class, Type::getId, Room::getRoomTypeId)
                        .leftJoin(Hotel.class, Hotel::getId, Type::getHotelId)
                        .eq(Hotel::getId, hotelId).eq(Type::getRoomType, typeName)
                        .le(Record::getBookStartTime, d)
                        .ge(Record::getBookEndTime, d));
                money += t.getPrice() * recordList.size();
            }
            if(money != 0.0)
                returnList.add(new Analyse(d, hotelId, money));
            calendar.add(Calendar.DATE, 1);
            d = calendar.getTime();
        }
        return returnList;
    }

    public Map<String, Double> moneyDay(int hotelId, Date day){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(day);
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime();
        HashMap<String, Double> returnMap = new HashMap<>();
        List<Type> typeList = typeMapper.selectList(new LambdaQueryWrapper<Type>().eq(Type::getHotelId, hotelId));
        for(Type t : typeList){
            List<Record> recordList = recordMapper.selectJoinList(Record.class, new MPJLambdaWrapper<Record>()
                    .selectAll(Record.class).leftJoin(Room.class, Room::getId, Record::getRoomId)
                    .eq(Room::getRoomTypeId, t.getId())
                    .le(Record::getBookStartTime, day)
                    .ge(Record::getBookEndTime, tomorrow));
            returnMap.put(t.getRoomType(), recordList.size() * t.getPrice());
        }
        return returnMap;
    }
}