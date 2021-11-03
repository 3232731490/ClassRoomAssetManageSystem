package com.nilu.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nilu.Common.Result;
import com.nilu.entity.*;
import com.nilu.mapper.*;
import com.nilu.pojo.Auth;
import com.nilu.pojo.Sex;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
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
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private BorrowMapper borrowMapper;

    @Resource
    AssetsMapper assetsMapper;
    @Resource
    OutInPutMapper outInPutMapper;
    @Resource
    PlaceMapper placeMapper;

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
        List<Borrow> borrows = borrowMapper.selectList(Wrappers.<Borrow>lambdaQuery().eq(Borrow::getUserid,user.getId()));
        borrows.forEach(value->{
            if(value.getIshandle()==1){
                Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,value.getAssetsid()));
                Place place1 = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,assets.getPlace()));
                Place place2 = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,0));
                place2.setCount(place2.getCount()+1);
                place1.setCount(place1.getCount()-1);
                assets.setPlace(0);
                assets.setBorrowing(0);
                assets.setBorrowed(0);
                Calendar calendar = new GregorianCalendar();
                String date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
                OutInPut outInPut = outInPutMapper.selectOne(Wrappers.<OutInPut>lambdaQuery().eq(OutInPut::getId,assets.getId()));
                outInPut.setIndate(date);
                placeMapper.updateById(place1);
                placeMapper.updateById(place2);
                outInPutMapper.updateById(outInPut);
                assetsMapper.updateById(assets);
            }
            borrowMapper.deleteById(value);
        });
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
