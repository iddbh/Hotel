package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.mapper.TypeMapper;
import com.sym.hotel.pojo.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ManagerServiceImp {
    @Autowired
    private TypeMapper typeMapper;
    ResponseResult changeHotelInfo(int id,int hotelId, double price, String message) {
        //返回一个type
        Type oldType = typeMapper.selectOne(new LambdaQueryWrapper<Type>()
                .eq(Type::getId, id)
                .eq(Type::getHotelId,hotelId));
        if (Objects.isNull(oldType)) {
            return new ResponseResult(403, "数据不存在");
        }
        LambdaUpdateWrapper<Type> updateWrapper = new LambdaUpdateWrapper<Type>()
                .set(Type::getPrice,price)
                .set(Type::getService,message)
                .eq(Type::getId,id)
                .eq(Type::getHotelId,hotelId);
        typeMapper.update(oldType,updateWrapper);
        return new ResponseResult(200,"更改成功");
    }
}
