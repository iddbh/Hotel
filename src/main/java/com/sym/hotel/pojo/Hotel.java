package com.sym.hotel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer locationId;
}