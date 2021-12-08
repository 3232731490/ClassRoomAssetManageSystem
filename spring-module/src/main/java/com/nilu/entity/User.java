package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/10/31-15:54
 * StuNo: 1910400731
 * Class: 19104221
 * Description: 用户信息实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private String id;
    private String username;
    private String Telephone;
    private String Place;
    private String School;
    private String Address;
    private String Logintime;
    private Integer permission;
    private Integer Sex;
    private String avatarurl;
    private Integer imgid;
}
