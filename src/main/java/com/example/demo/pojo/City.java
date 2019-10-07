package com.example.demo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class City {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    @TableField("CountryCode")
    private String countryCode;

    private String district;

    private Integer population;
}
