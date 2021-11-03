package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/10/31-15:55
 * StuNo: 1910400731
 * Class: 19104221
 * Description: 用户账户实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account")
public class Account {
    @TableId(type = IdType.AUTO)
    private String id;
    private String password;
    private Integer permission;
}
