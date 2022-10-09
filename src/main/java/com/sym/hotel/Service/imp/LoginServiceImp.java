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
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImp implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(Guest guest) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(guest.getName(),guest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        LoginGuest loginGuest= (LoginGuest) authenticate.getPrincipal();
        String userId=loginGuest.getGuest().getId().toString();
        String jwt= JwtUtil.createJwt(userId);
        redisCache.setCacheObject("login:"+userId,loginGuest);
        return new ResponseResult(200,"登录成功", Map.of("token",jwt));
    }
}
