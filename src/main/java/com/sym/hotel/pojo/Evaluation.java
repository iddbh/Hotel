package com.sym.hotel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evaluation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer recordId;
    private String statement;
    private String picture;
    private String video;
    private Double score;
    private Integer hotelId;
    private Integer guestId;
}
