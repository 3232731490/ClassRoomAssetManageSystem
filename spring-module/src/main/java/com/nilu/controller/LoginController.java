package com.nilu.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nilu.Common.Result;
import com.nilu.entity.Account;
import com.nilu.entity.Message;
import com.nilu.entity.User;
import com.nilu.mapper.AccountMapper;

import com.nilu.mapper.MessageMapper;
import com.nilu.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author: 倪路
 * Time: 2021/10/31-15:54
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@RestController
public class LoginController {
    @Resource
    AccountMapper accountMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    MessageMapper messageMapper;

    @RequestMapping("/login")
    public Result<?> Login(@RequestBody Account account){
        Account res = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId,account.getId()).eq(Account::getPassword,account.getPassword()));
        if(res==null){
            return Result.error("-1","用户名或密码错误");
        }
        return Result.success(res);
    }

    @RequestMapping("/register")
    public Result<?> Register(@RequestBody Account account){
        Account res = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId,account.getId()));
        if(res==null){
            accountMapper.insert(account);
            User user = new User();
            user.setId(account.getId());
            user.setUsername("user");
            user.setAddress("暂无信息");
            user.setTelephone("暂无信息");
            user.setPlace("暂无信息");
            user.setPermission(2);
            Calendar curdate = new GregorianCalendar();
            String date = ""+curdate.get(Calendar.YEAR)+"-"+(curdate.get(Calendar.MONTH)+1)+"-"+(curdate.get(Calendar.DATE)<=9?"0"+curdate.get(Calendar.DATE):curdate.get(Calendar.DATE));
            user.setLogintime(date);
            userMapper.insert(user);
            return Result.success(account);
        }
        return Result.error("-1","当前账户在数据库中已存在");
    }

    @RequestMapping("/getUserInfo")
    public User getInfo(@RequestBody String id){
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,id));
        return user;
    }

    @RequestMapping("/getPermission")
    public int getPermission(@RequestBody String id){
        Account res = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId,id));
        return res.getPermission();
    }

    @RequestMapping("/getMessageNum")
    public Long getMessageNum(@RequestBody String id){
        Long res = messageMapper.selectCount(Wrappers.<Message>lambdaQuery().eq(Message::getUserid,id).eq(Message::getStatus,0));
        return res;
    }

    @RequestMapping("/getMessage")
    public List getMessage(@RequestBody String id){
        List<Message> res = messageMapper.selectList(Wrappers.<Message>lambdaQuery().eq(Message::getUserid,id));
        return res;
    }

    @RequestMapping("/deleteMessage")
    public Result<?> deleteMessage(@RequestBody String id){
        int i = messageMapper.delete(Wrappers.<Message>lambdaQuery().eq(Message::getId, id));
        return i==1?Result.success():Result.error("-1","系统中没有找到当前消息喔~");
    }

    @RequestMapping("/setReaded")
    public void setReaded(@RequestBody String id){
        Message msg = messageMapper.selectOne(Wrappers.<Message>lambdaQuery().eq(Message::getId, id));
        msg.setStatus(1);
        messageMapper.updateById(msg);
    }

    @RequestMapping("/setIsreply")
    public void setIsreply(@RequestBody String id){
        Message msg = messageMapper.selectOne(Wrappers.<Message>lambdaQuery().eq(Message::getId, id));
        msg.setIsreply(1);
        messageMapper.updateById(msg);
    }

    @RequestMapping("/sendMessage")
    public void sendMessage(@RequestBody Message msg){
        messageMapper.insert(msg);
    }

    @RequestMapping("/alterPass")
    public void alterPass(@RequestBody Account account){
        Account account1 = accountMapper.selectOne(Wrappers.<Account>lambdaQuery().eq(Account::getId, account.getId()));
        account1.setPassword(account.getPassword());
        accountMapper.updateById(account1);
    }

    @RequestMapping("/alterInfo")
    public void alterInfo(@RequestBody User user){
        User newuser = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,user.getId()));
        newuser.setUsername(user.getUsername());
        newuser.setSex(user.getSex());
        newuser.setPlace(user.getPlace());
        newuser.setTelephone(user.getTelephone());
        newuser.setSchool(user.getSchool());
        newuser.setAddress(user.getAddress());
        userMapper.updateById(newuser);
    }

    @RequestMapping("/getUsername")
    public String getUsername(@RequestBody String id){
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId, id));
        return user.getUsername();
    }

}
