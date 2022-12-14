package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.sym.hotel.mapper.RoomMapper;
import com.sym.hotel.pojo.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class addData {
    @Autowired
    RoomMapper roomMapper;
    public void add(){
        int index=69;
        for (int i = 1; i <= 4; i++) {

            Room room=new Room();

            room.setRoomTypeId(i);

            for (int j = 1; j <= 3; j++) {

                room.setId(index);
                index+=1;
                room.setRoomNum(200+j);
                roomMapper.insert(room);
            }
        }
    }

}
