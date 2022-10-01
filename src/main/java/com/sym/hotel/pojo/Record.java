package com.sym.hotel.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Record {
    private Integer id;
    private Integer guestId;
    private Integer roomId;
    private Date bookTime;
}
