package com.sym.hotel.Service;

import com.sym.hotel.domain.ResponseResult;
import com.sym.hotel.pojo.Guest;
import com.sym.hotel.pojo.Manager;

import javax.mail.MessagingException;
import java.io.IOException;

public interface RegisterService {
    String sendCode(String email) throws MessagingException, IOException;
    ResponseResult register(Guest guest) ;
    ResponseResult registerAsManager(Manager manager);
}
