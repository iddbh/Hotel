package com.sym.hotel.pojo;

import lombok.Data;

@Data
public class Room {
    private Integer id;
    private Boolean isEmpty;
    private Boolean isBooked;
    private String roomType;
    private String location;
    private Double price;
    private Integer hotelId;
}
