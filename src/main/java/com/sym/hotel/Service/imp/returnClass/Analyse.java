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
public class Analyse {
    private Date day;
    private int hotelId;
    private double money;
}
