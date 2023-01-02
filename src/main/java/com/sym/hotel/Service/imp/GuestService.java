package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.Service.imp.returnClass.ReturnRecord;
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
        LambdaQueryWrapper<Guest> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Guest::getName,username);
        Guest guest=guestMapper.selectOne(lambdaQueryWrapper);
        if(Objects.isNull(guest)){
            throw new RuntimeException("用户名或者密码错误");
        }
        //如果没有查询到用户
        //TODO:查询权限信息
        List<String> list=new ArrayList<>(Arrays.asList("test","admin"));
        //把数据封装成UserDetails返回
        return new LoginGuest(guest,list);
    }

    public List<Integer> selectRoom(Integer hotelId,Double minMoney, Double maxMoney, Date startTime, Date endTime){
        List<Room> list=roomMapper.selectJoinList(Room.class,
                new MPJLambdaWrapper<Room>().selectAll(Room.class).leftJoin(Type.class,Type::getId,Room::getRoomTypeId).select(Type::getPrice)
                        .eq(Type::getHotelId,hotelId).gt(Type::getPrice,minMoney).le(Type::getPrice,maxMoney));
        List<Room> listDel=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listDel=roomMapper.selectJoinList(Room.class,new MPJLambdaWrapper<Room>().selectAll(Room.class).
                    leftJoin(Record.class,Record::getRoomId,Room::getId).
                    select(Record::getBookStartTime,Record::getBookEndTime).
                    ge(Record::getBookStartTime,startTime).lt(Record::getBookEndTime,startTime).or().lt(Record::getBookStartTime,endTime).ge(Record::getBookEndTime,endTime).or().gt(Record::getBookStartTime,startTime).lt(Record::getBookEndTime,endTime));
        }
        HashSet h1 = new HashSet(list);
        HashSet h2 = new HashSet(listDel);
        h1.removeAll(h2);
        list.clear();
        list.addAll(h1);
        List<Integer> listNum=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listNum.add(list.get(i).getRoomNum());
        }
        return listNum;
    }

    public List<ReturnRecord> viewRecord(){
        // 这里没有使用连表查询，可以改进。屎山！！！！！！！
        UsernamePasswordAuthenticationToken authentication;
        try {
            authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        }catch (Exception e){return null;}
        LoginGuest loginGuest  = (LoginGuest) authentication.getPrincipal();
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
            String hotelName = hotel.getName();
            int recordId = re.getId().hashCode();
            boolean isOver = (new Date().getTime() < re.getBookEndTime().getTime());
            rrl.add(new ReturnRecord(id, roomId, price, typeInfo, hotelName, re.getBookStartTime(), re.getBookEndTime(), recordId, isOver));
        }
        return rrl;
    }

    public ResponseResult bookRoom(Record recordNew){
       Record record=null ;
       record=recordMapper.selectOne(
                new MPJLambdaWrapper<Record>().selectAll(Record.class)
                        .lt(Record::getBookStartTime,recordNew.getBookEndTime()).ge(Record::getBookEndTime,recordNew.getBookEndTime())
                        .or().le(Record::getBookStartTime,recordNew.getBookStartTime()).gt(Record::getBookEndTime,recordNew.getBookStartTime()));
       if(record==null){
//           int count = userMapper.insert(userEntity);
           return new ResponseResult(200,"预定成功");
       }else {
           return new ResponseResult(200,"此房间该时段内已被预订，请您重新选择");
       }
    }
}