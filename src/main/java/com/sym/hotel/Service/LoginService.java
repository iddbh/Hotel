package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;

public interface LoginService {
    ResponseResult login(Guest guest);
    ResponseResult logout();
}
