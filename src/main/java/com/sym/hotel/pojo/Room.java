package com.sym.hotel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Integer id;
    private Boolean isEmpty;
    private Boolean isBooked;
    private String roomType;
    private String location;
    private Double price;
    private Integer hotelId;
}
