package com.sym.hotel.util;

import com.sym.hotel.Util.JwtUtil;
import org.junit.jupiter.api.Test;

public class JwtUtilTests {

    @Test
    void test(){
        String jwt = JwtUtil.createJwt("1");
        //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3MTJiZjg3ZmE3MzI0MDU5YTE1MzhkNmZmN2IyNWZmYiIsInN1YiI6IjUiLCJpc3MiOiJRdWFuUXVhbiIsImlhdCI6MTY2ODY5MzA3NywiZXhwIjoxNjY5Mjk3ODc3fQ.r32XPFKVq9YGs6u-US4RCblqm7RpuDLhWauKXot0FCY
        System.out.println(JwtUtil.parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3MTJiZjg3ZmE3MzI0MDU5YTE1MzhkNmZmN2IyNWZmYiIsInN1YiI6IjUiLCJpc3MiOiJRdWFuUXVhbiIsImlhdCI6MTY2ODY5MzA3NywiZXhwIjoxNjY5Mjk3ODc3fQ.r32XPFKVq9YGs6u-US4RCblqm7RpuDLhWauKXot0FCY").getSubject());
    }
}
