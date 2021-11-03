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
 * Description: 出入库实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("outinput")
public class OutInPut {
    private String id;
    private String indate;
    private String outdate;
}
