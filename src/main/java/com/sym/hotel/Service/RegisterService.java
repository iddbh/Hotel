package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Manager;

public interface RegisterService {
    ResponseResult register(Guest guest);
    ResponseResult registerAsManager(Manager manager);
}
