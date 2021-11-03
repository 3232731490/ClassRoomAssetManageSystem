package com.nilu.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nilu.Common.Result;
import com.nilu.entity.Account;
import com.nilu.entity.Borrow;
import com.nilu.entity.Message;
import com.nilu.entity.User;
import com.nilu.mapper.AccountMapper;
import com.nilu.mapper.BorrowMapper;
import com.nilu.mapper.MessageMapper;
import com.nilu.mapper.UserMapper;
import com.nilu.pojo.Auth;
import com.nilu.pojo.Sex;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: 倪路
 * Time: 2021/10/31-15:54
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private BorrowMapper borrowMapper;

    @GetMapping("/getUsers")
    public Result<?> getUsers(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize , @RequestParam(defaultValue = "") String userid , @RequestParam(defaultValue = "") Integer permission){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if(StrUtil.isNotBlank((userid))){
            wrapper.like(User::getId, userid);
        }
        if(permission != null){
            wrapper.eq(User::getPermission, permission);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(number, pagesize), wrapper);
        if(userPage.getTotal()!=0){
            return Result.success(userPage);
        }else{
            return Result.error("-1","没有查找到符合条件的用户");
        }
    }



    @RequestMapping("/deleteUser")
    public void deleteUser(@RequestBody User user){
        Account account = new Account();
        account.setId(user.getId());
        messageMapper.delete(Wrappers.<Message>lambdaQuery().eq(Message::getUserid,user.getId()));
        accountMapper.deleteById(account);
        borrowMapper.delete(Wrappers.<Borrow>lambdaQuery().eq(Borrow::getUserid,user.getId()));
        userMapper.deleteById(user);
    }

    @RequestMapping("/updateUser")
    public void updateUser(@RequestBody User user){
        User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,user.getId()));
        Account account = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId,user.getId()));
        account.setPermission(user.getPermission());
        user1.setPermission(user.getPermission());
        user1.setUsername(user.getUsername());
        userMapper.updateById(user);
        accountMapper.updateById(account);
        
    }

    @RequestMapping("/getSexNum")
    public Sex getSexNum(){
        Long mannum = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getSex,1));
        Long womannum = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getSex,2));
        Long unkonwnum = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getSex,0));
        return new Sex(mannum,womannum,unkonwnum);
    }

    @RequestMapping("/getAuth")
    public Auth getAuth(){
        Long suadmin = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getPermission,0));
        Long admin = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getPermission,1));
        Long user = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getPermission,2));
        return new Auth(suadmin,admin,user);
    }
}
