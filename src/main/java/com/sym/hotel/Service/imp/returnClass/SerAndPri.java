package com.sym.hotel.Service.imp.returnClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerAndPri {
    private String service;
    private double price;
}
