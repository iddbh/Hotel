package com.sym.hotel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collecttable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer guestId;
    private Integer hotelId;
    private Integer status;
}
