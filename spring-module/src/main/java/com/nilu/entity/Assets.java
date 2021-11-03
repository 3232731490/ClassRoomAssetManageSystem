package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/10/31-15:54
 * StuNo: 1910400731
 * Class: 19104221
 * Description: 资产实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("assets")
public class Assets {
    private String id;
    private String kind;
    private String brand;
    private Integer Status;
    private Integer usetime;
    private Integer place;
    private String purchaseid;
    private Integer fixcount;
    private Integer isinstore;
    private Integer borrowed;
    private Integer borrowing;
}
