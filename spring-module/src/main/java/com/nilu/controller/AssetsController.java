package com.nilu.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nilu.Common.Result;
import com.nilu.entity.*;
import com.nilu.mapper.*;
import com.nilu.pojo.ClassData;
import com.nilu.pojo.StatusData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: 倪路
 * Time: 2021/11/2-18:28
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@RestController
public class AssetsController {
    @Resource
    AssetsMapper assetsMapper;

    @Resource
    OutInPutMapper outInPutMapper;

    @Resource
    PlaceMapper placeMapper;

    @Resource
    BorrowMapper borrowMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    MessageMapper messageMapper;

    @GetMapping("/getAssets")
    public Result<?> getAssets(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize , @RequestParam(defaultValue = "") String kindName, @RequestParam(defaultValue = "") Integer status){
        LambdaQueryWrapper<Assets> wrapper = Wrappers.<Assets>lambdaQuery();
        if(StrUtil.isNotBlank(kindName)){
            wrapper.like(Assets::getKind,kindName);
        }
        if(status!=null){
            wrapper.eq(Assets::getStatus,status);
        }
        // 将在仓库中的排除
        wrapper.ne(Assets::getPlace,0);
        Page<Assets> purchasePage = assetsMapper.selectPage(new Page<>(number,pagesize),wrapper);
        if(purchasePage.getTotal()!=0) return Result.success(purchasePage);
        return Result.error("-1","未查询到任何资产信息");
    }

    @PostMapping("/getOutDate")
    public String getOutDate(@RequestBody String id){
        String outdate = outInPutMapper.selectOne(Wrappers.<OutInPut>lambdaQuery().eq(OutInPut::getId,id)).getOutdate();;
        return outdate;
    }

    @PostMapping("/updateAssets")
    public Result<?> updateAssets(@RequestBody String id){
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,id));
        if(assets.getFixcount()==3){
            assetsMapper.deleteById(assets);
            return Result.error("-1","当前资产维修次数过多，已报废...");
        }
        assets.setStatus(0);
        assets.setFixcount(assets.getFixcount()+1);
        assetsMapper.updateById(assets);
        return Result.success();
    }

    @PostMapping("/deleteAssets")
    public void deleteAssets(@RequestBody String id){
        // 先修改对应地点表中的数据
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,id));
        Integer id1 = assets.getPlace();
        Place place = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,id1));
        place.setCount(place.getCount()-1);
        placeMapper.updateById(place);
        // 再删除对应记录
        outInPutMapper.delete(Wrappers.<OutInPut>lambdaQuery().eq(OutInPut::getId,id));
        assetsMapper.delete(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,id));
    }

    @PostMapping("/getPlaces")
    public List<Place> getPlaces(){
        return placeMapper.selectList(Wrappers.lambdaQuery());
    }

    @GetMapping("/getAllAssets")
    public Result<?> getAllAssets(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize , @RequestParam(defaultValue = "") String ID, @RequestParam(defaultValue = "") Integer status, @RequestParam(defaultValue = "") String kindName, @RequestParam(defaultValue = "") Integer place ){
        LambdaQueryWrapper<Assets> wrapper = Wrappers.<Assets>lambdaQuery();
        if(StrUtil.isNotBlank(ID)){
            wrapper.like(Assets::getId,ID);
        }
        if(status != null){
            wrapper.eq(Assets::getStatus,status);
        }
        if(StrUtil.isNotBlank(kindName)){
            wrapper.like(Assets::getKind,kindName);
        }
        if(place != null){
            wrapper.eq(Assets::getPlace,place);
        }

        Page<Assets> purchasePage = assetsMapper.selectPage(new Page<>(number,pagesize),wrapper);
        if(purchasePage.getTotal()!=0) return Result.success(purchasePage);
        return Result.error("-1","未查询到任何资产信息");
    }

    @PostMapping("/getInDate")
    public String getInDate(@RequestBody String id){
        String indate = outInPutMapper.selectOne(Wrappers.<OutInPut>lambdaQuery().eq(OutInPut::getId,id)).getIndate();;
        return indate;
    }

    @PostMapping("/updateAssetsStatus")
    public void updateAssetsStatus(@RequestBody String id ,@RequestBody Integer status){
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,id));
        assets.setStatus(status);
        assetsMapper.updateById(assets);
    }

    @PostMapping("/getStatusData")
    public List<StatusData> getStatusData(){
        List<StatusData> res = new ArrayList<>();
        Long ok = assetsMapper.selectCount(Wrappers.<Assets>lambdaQuery().eq(Assets::getStatus, 0));
        Long normal = assetsMapper.selectCount(Wrappers.<Assets>lambdaQuery().eq(Assets::getStatus, 1));
        Long bad = assetsMapper.selectCount(Wrappers.<Assets>lambdaQuery().eq(Assets::getStatus, 2));
        res.add(new StatusData("正常",ok));
        res.add(new StatusData("一般",normal));
        res.add(new StatusData("差",bad));
        return res;
    }

    @PostMapping("/getStoreData")
    public List<ClassData> getStoreData(){
        List<ClassData> res = new ArrayList<>();
        Map<String,Integer> count = new HashMap<>();
        List<Assets> assets = assetsMapper.selectList(Wrappers.<Assets>lambdaQuery().eq(Assets::getPlace,0));
        assets.forEach(value->{
            if(count.containsKey(value.getKind())){
                int n = count.get(value.getKind());
                count.remove(value.getKind());
                count.put(value.getKind(),n+1);
            }else{
                count.put(value.getKind(),1);
            }
        });
        count.forEach((k,v)->{
            res.add(new ClassData(k,v));
        });
        return res;
    }

    @PostMapping("/getPlaceData")
    public List<ClassData> getPlaceData(){
        List<ClassData> res = new ArrayList<>();
        List<Place> places = placeMapper.selectList(Wrappers.lambdaQuery());
        places.forEach(value->{
            res.add(new ClassData(value.getName(),value.getCount()));
        });
        return res;
    }

    @PostMapping("/borrowAssets")
    public Result<?> borrowAssets(@RequestBody Map<String,Object> datas){
        String id = (String) datas.get("id");
        String userid = (String) datas.get("userid");
        Integer place = (Integer) datas.get("place");
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,id));
        Place originplace = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,assets.getPlace()));
        Place distplace = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,place));
        if(distplace.getCount().equals(distplace.getMaxcount())){
            return Result.error("-1","目的地的库存已经达到最大了喔，没法再借啦~");
        }
        Calendar calendar = new GregorianCalendar();
        String date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        assets.setBorrowing(1);
        String bid =""+ calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + (calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH)) + (int)(Math.random()*899 + 100);
        borrowMapper.insert(new Borrow(bid,originplace.getId(),place,date,id,0,userid,0));
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().ne(User::getPermission,2));
        String msg_id = ""+ calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + (calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        Message msg = new Message("","",date,"系统提示： 用户("+userid+")提出了借用资源("+id+")的申请，请决定是否同意该申请吧",0,"0",0);
        users.forEach(value->{
            msg.setId(msg_id+(int)(Math.random()*899 + 100));
            msg.setUserid(value.getId());
            messageMapper.insert(msg);
        });
        assetsMapper.updateById(assets);
        return Result.success();
    }

    @PostMapping("/handleBorrow")
    public Result<?> handleBorrow(@RequestBody Map<String,Object> datas){
        String id = (String) datas.get("id");
        String userid = (String) datas.get("userid");
        Borrow borrow = borrowMapper.selectOne(Wrappers.<Borrow>lambdaQuery().eq(Borrow::getId,id));
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,borrow.getAssetsid()));
        Place originplace = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,borrow.getOriginplace()));
        Place distplace = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,borrow.getDstplace()));
        if(distplace.getCount().equals(distplace.getMaxcount())){
            return Result.error("-1","目的地的库存已经达到最大了喔，没法再借啦~");
        }
        originplace.setCount(originplace.getCount()-1);
        distplace.setCount(distplace.getCount()+1);
        OutInPut outInPut = outInPutMapper.selectOne(Wrappers.<OutInPut>lambdaQuery().eq(OutInPut::getId,assets.getId()));
        Calendar calendar = new GregorianCalendar();
        String date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        assets.setPlace(distplace.getId());
        if(originplace.getId()==0&&distplace.getId()!=0){
            assets.setIsinstore(0);
            outInPut.setOutdate(date);
        }else if(originplace.getId()!=0&&distplace.getId()==0){
            outInPut.setIndate(date);
            assets.setIsinstore(1);
        }
        assets.setBorrowing(0);
        assets.setBorrowed(1);
        borrow.setIshandle(1);
        assetsMapper.updateById(assets);
        placeMapper.updateById(distplace);
        placeMapper.updateById(originplace);
        outInPutMapper.updateById(outInPut);
        borrowMapper.updateById(borrow);
        String msg_id = ""+ calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + (calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH))+(int)(Math.random()*899 + 100);
        Message msg = new Message(msg_id,"001001",date,"系统提示： 管理员("+userid+")接受了借用申请("+id+")",0,"0",0);
        messageMapper.insert(msg);
        msg_id = ""+ calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + (calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH))+(int)(Math.random()*899 + 100);
        msg.setId(msg_id);
        msg.setUserid(borrow.getUserid());
        msg.setFromuserid(userid);
        msg.setContent("我已经同意了您的申请，您现在可以使用所借资源啦~");
        messageMapper.insert(msg);
        return Result.success();
    }

    @PostMapping("/rejectBorrow")
    public void rejectBorrow(@RequestBody Map<String,Object> datas){
        String id = (String) datas.get("id");
        String userid = (String) datas.get("userid");
        Borrow borrow = borrowMapper.selectOne(Wrappers.<Borrow>lambdaQuery().eq(Borrow::getId,id));
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,borrow.getAssetsid()));
        assets.setBorrowing(0);
        assetsMapper.updateById(assets);
        borrowMapper.deleteById(borrow);
        Calendar calendar = new GregorianCalendar();
        String date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        String msg_id = ""+ calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + (calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH))+(int)(Math.random()*899 + 100);
        Message msg = new Message(msg_id,"001001",date,"系统提示： 管理员("+userid+")拒绝了借用申请("+id+")",0,"0",0);
        messageMapper.insert(msg);
        msg_id = ""+ calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH)+1) + (calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH))+(int)(Math.random()*899 + 100);
        msg.setId(msg_id);
        msg.setUserid(borrow.getUserid());
        msg.setFromuserid(userid);
        msg.setContent("不好意思，我拒绝了您的借用申请，请再试试吧~");
        messageMapper.insert(msg);
    }

    @GetMapping("/getReturns")
    public Result<?> getReturns(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize , @RequestParam(defaultValue = "") String userid, @RequestParam(defaultValue = "") String assetsid){
        LambdaQueryWrapper<Borrow> wrapper = Wrappers.<Borrow>lambdaQuery();
        wrapper.eq(Borrow::getUserid,userid);
        if(StrUtil.isNotBlank(assetsid)){
            wrapper.like(Borrow::getAssetsid,assetsid);
        }
        wrapper.eq(Borrow::getIshandle,1);
        Page<Borrow> purchasePage = borrowMapper.selectPage(new Page<>(number,pagesize),wrapper);
        if(purchasePage.getTotal()!=0) return Result.success(purchasePage);
        return Result.error("-1","未查询到任何借用信息");
    }

    @GetMapping("/getAllAssetsNotBorrow")
    public Result<?> getAllAssetsNotBorrow(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize , @RequestParam(defaultValue = "") String ID, @RequestParam(defaultValue = "") Integer status, @RequestParam(defaultValue = "") String kindName, @RequestParam(defaultValue = "") Integer place ){
        LambdaQueryWrapper<Assets> wrapper = Wrappers.<Assets>lambdaQuery();
        wrapper.eq(Assets::getBorrowed,0);
        if(StrUtil.isNotBlank(ID)){
            wrapper.like(Assets::getId,ID);
        }
        if(status != null){
            wrapper.eq(Assets::getStatus,status);
        }
        if(StrUtil.isNotBlank(kindName)){
            wrapper.like(Assets::getKind,kindName);
        }
        if(place != null){
            wrapper.eq(Assets::getPlace,place);
        }

        Page<Assets> purchasePage = assetsMapper.selectPage(new Page<>(number,pagesize),wrapper);
        if(purchasePage.getTotal()!=0) return Result.success(purchasePage);
        return Result.error("-1","未查询到任何资产信息");
    }

    public static int dayDiff(String date1, String date2,String format) throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        long diff = 0l;
        long d1 = formater.parse(date1).getTime();
        long d2 = formater.parse(date2).getTime();
        diff = (d1 - d2) / (1000 * 60 * 60 * 24);
        return (int)diff;
    }

    @PostMapping("/returnAssets")
    public Result<?> returnAssets(@RequestBody String id) throws ParseException {
        Borrow borrow = borrowMapper.selectOne(Wrappers.<Borrow>lambdaQuery().eq(Borrow::getId,id));
        borrow.setIsreturn(1);
        Place op = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,borrow.getDstplace()));
        Place dp = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,borrow.getOriginplace()));
        Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,borrow.getAssetsid()));
        OutInPut outInPut = outInPutMapper.selectOne(Wrappers.<OutInPut>lambdaQuery().eq(OutInPut::getId,assets.getId()));
        Calendar calendar = new GregorianCalendar();
        String date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        if(dp.getCount().equals(dp.getMaxcount())){
            return Result.error("-1","物品原来所在地库存已满，目前无法归还，请稍后再试试~");
        }
        if(op.getId()==0&&dp.getId()!=0){
            assets.setPlace(dp.getId());
            assets.setIsinstore(0);
            outInPut.setOutdate(date);
        }else if(op.getId()!=0&&dp.getId()==0){
            outInPut.setIndate(date);
            assets.setPlace(dp.getId());
            assets.setUsetime(assets.getUsetime()+dayDiff(date,outInPut.getOutdate(),"yyyy-MM-dd"));    // 更新资产使用时间
            assets.setIsinstore(1);
        }
        assets.setBorrowed(0);
        borrowMapper.updateById(borrow);
        op.setCount(op.getCount()-1);
        dp.setCount(dp.getCount()+1);
        assetsMapper.updateById(assets);
        placeMapper.updateById(dp);
        placeMapper.updateById(op);
        outInPutMapper.updateById(outInPut);
        return Result.success();
    }

    @GetMapping("/getNotHandleRequst")
    public Result<?> getNotHandleRequst(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize ) {
        Page<Borrow> res =borrowMapper.selectPage(new Page<>(number,pagesize),Wrappers.<Borrow>lambdaQuery().eq(Borrow::getIshandle, 0));
        if(res.getTotal()>0)
            return Result.success(res);
        else
            return Result.error("-1","当前没有申请未处理~");
    }
}
