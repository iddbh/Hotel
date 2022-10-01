package com.sym.hotel.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CityMapperTests {
    @Autowired
    private CityMapper cityMapper;

    @Test
    void test(){
        System.out.println(cityMapper.selectList(null));
    }
}
