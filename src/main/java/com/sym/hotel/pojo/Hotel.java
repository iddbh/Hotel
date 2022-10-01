package com.sym.hotel.pojo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private Integer id;
    private Integer cityId;
    private String name;
}