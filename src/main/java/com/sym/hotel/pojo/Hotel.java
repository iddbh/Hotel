package com.sym.hotel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cityId;
    private String name;
}