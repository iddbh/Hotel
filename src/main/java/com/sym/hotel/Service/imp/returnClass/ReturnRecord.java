package com.sym.hotel.Service.imp.returnClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnRecord {
    private Integer guestId;
    private Integer roomId;
    private double price;
    private String roomType;
    private int hotelId;
    private Date bookStartTime;
    private Date bookEndTime;
    private int recordId;
    private boolean isOver;
    private int roomName;
}
