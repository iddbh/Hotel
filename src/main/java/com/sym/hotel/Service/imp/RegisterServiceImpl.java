package com.sym.hotel.Service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sym.hotel.Service.RegisterService;
import com.sym.hotel.Util.JwtUtil;
import com.sym.hotel.Util.MailTest;
import com.sym.hotel.Util.RedisCache;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.mapper.GuestMapper;
import com.sym.hotel.mapper.ManagerMapper;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private GuestMapper guestMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ManagerMapper managerMapper;
    @Override
    public String sendCode(String email) throws MessagingException, IOException {
        Guest selectOne = guestMapper.selectOne(new LambdaQueryWrapper<Guest>()
                .eq(Guest::getEmail, email));
        if (selectOne != null) {
            return "用户名已存在";
        }
        return MailTest.sendMail(email);
    }

    @Override
    public ResponseResult register(Guest guest) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(guest.getPassword().toString());
        guest.setPassword(encode);
        guestMapper.insert(guest);
        String userId = guest.getId().toString();
        String jwt = JwtUtil.createJwt(userId);
        redisCache.setCacheObject("login:" + userId, guest);
        return new ResponseResult(200, "注册成功", Map.of("token", jwt));
    }

    @Override
    public ResponseResult registerAsManager(Manager manager) {
        Manager selectOne = managerMapper.selectOne(new LambdaQueryWrapper<Manager>()
                .eq(Manager::getName, manager.getName()));
        if (selectOne != null) {
            return new ResponseResult(400, "用户名已存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(manager.getPassword());
        manager.setPassword(encode);
        managerMapper.insert(manager);
        String managerId = manager.getId().toString();
        String jwt = JwtUtil.createJwt(managerId);
        redisCache.setCacheObject("login:" + managerId, manager);
        return new ResponseResult(200, "登录成功", Map.of("token", jwt));
    }

}
