package com.nilu.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 倪路
 * Time: 2021/11/2-8:40
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
    private Long suadmin;
    private Long admin;
    private Long user;
}
