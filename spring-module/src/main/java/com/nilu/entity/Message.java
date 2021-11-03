package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/10/31-16:53
 * StuNo: 1910400731
 * Class: 19104221
 * Description: 消息实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private String id;
    private String userid;
    private String date;
    private String content;
    private Integer status;
    private String fromuserid;
    private Integer isreply;
}
