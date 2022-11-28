package com.sym.hotel.Service.imp;

import com.sym.hotel.Service.LoginService;
import com.sym.hotel.Util.JwtUtil;
import com.sym.hotel.Util.RedisCache;
import com.sym.hotel.domain.LoginGuest;
import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(Guest guest) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(guest.getName(),guest.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            LoginGuest loginGuest= (LoginGuest) authenticate.getPrincipal();
            String userId=loginGuest.getGuest().getId().toString();
            String jwt= JwtUtil.createJwt(userId);
            redisCache.setCacheObject("login:"+userId,loginGuest);
            return new ResponseResult(200,"登录成功", Map.of("token",jwt));
        }
        catch (Exception e){
            return new ResponseResult(200, "登陆失败");
        }
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginGuest loginGuest  = (LoginGuest) authentication.getPrincipal();
        Integer id = loginGuest.getGuest().getId();
        //删除redis中的值
        redisCache.deleteObject("login:"+id);
        return new ResponseResult(200,"注销成功");
    }
}
