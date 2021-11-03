package com.nilu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/11/3-7:32
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("borrow")
public class Borrow {
    @TableId(type = IdType.AUTO)
    private String id;
    private Integer originplace;
    private Integer dstplace;
    private String date;
    private String assetsid;
    private Integer isreturn;
    private String userid;
    private Integer ishandle;
}
