package com.sym.hotel.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class CityMapperTests {
    @Autowired
    private CityMapper cityMapper;
    @Test
    public void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

        String encode=bCryptPasswordEncoder.encode("123456");
//        String encode2=bCryptPasswordEncoder.encode(encode);
        System.out.println(encode);
    }
    @Test
    void test(){
        System.out.println(cityMapper.selectList(null));
    }
}
