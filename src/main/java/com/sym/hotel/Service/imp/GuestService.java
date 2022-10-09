package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.mapper.GuestMapper;
import com.sym.hotel.pojo.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class GuestService implements UserDetailsService {
    @Autowired
    private GuestMapper guestMapper;


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
}
