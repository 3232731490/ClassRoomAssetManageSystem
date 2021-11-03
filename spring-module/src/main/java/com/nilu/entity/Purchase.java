package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/10/31-15:55
 * StuNo: 1910400731
 * Class: 19104221
 * Description: 订购单实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase")
public class Purchase {
    private String id;
    private String userid;
    private String purchasetime;
    private Integer num;
    private Integer ishandle;
    private String kind;
    private String brand;
    private String username;
}
