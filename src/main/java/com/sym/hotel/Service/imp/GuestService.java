package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.mapper.GuestMapper;
import com.sym.hotel.mapper.RecordMapper;
import com.sym.hotel.mapper.RoomMapper;
import com.sym.hotel.mapper.TypeMapper;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Record;
import com.sym.hotel.pojo.Room;
import com.sym.hotel.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sym.hotel.domain.ResponseResult;




import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public List<Room> selectRoomByPrice(Double minMoney,Double maxMoney){
        List<Room> list=roomMapper.selectJoinList(Room.class,
                new MPJLambdaWrapper<Room>().selectAll(Room.class).leftJoin(Type.class,Type::getId,Room::getRoomTypeId).select(Type::getPrice)
                        .gt(Type::getPrice,minMoney).le(Type::getPrice,maxMoney));
        return list;
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
