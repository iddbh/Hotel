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
public class Selected {
    private int recordId;
    private int guestId;
    private int roomNum;
    private Date startTime;
    private Date endTime;
    private double price;
    private boolean isOver;
}
