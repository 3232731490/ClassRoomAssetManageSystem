package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/11/2-20:10
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("place")
public class Place {
    private Integer id;
    private String name;
    private Integer count;
    private Integer maxcount;
}
