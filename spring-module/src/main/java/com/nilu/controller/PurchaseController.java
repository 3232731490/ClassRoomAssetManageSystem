package com.nilu.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nilu.Common.Result;
import com.nilu.entity.Assets;
import com.nilu.entity.OutInPut;
import com.nilu.entity.Place;
import com.nilu.entity.Purchase;
import com.nilu.mapper.AssetsMapper;
import com.nilu.mapper.OutInPutMapper;
import com.nilu.mapper.PlaceMapper;
import com.nilu.mapper.PurchaseMapper;
import com.nilu.pojo.ClassData;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author: 倪路
 * Time: 2021/11/2-9:32
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@RestController
public class PurchaseController {

    @Resource
    PurchaseMapper purchaseMapper;
    @Resource
    AssetsMapper assetsMapper;
    @Resource
    OutInPutMapper outInPutMapper;
    @Resource
    PlaceMapper placeMapper;

    @GetMapping("/getPurchases")
    public Result<?> getPurchases(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize , @RequestParam(defaultValue = "") String userid , @RequestParam(defaultValue = "") String kindName){
        LambdaQueryWrapper<Purchase> wrapper = Wrappers.<Purchase>lambdaQuery();
        if(StrUtil.isNotBlank(userid)){
            wrapper.like(Purchase::getUserid,userid);
        }
        if(StrUtil.isNotBlank(kindName)){
            wrapper.like(Purchase::getKind,kindName);
        }
        Page<Purchase> purchasePage = purchaseMapper.selectPage(new Page<>(number, pagesize), wrapper);
        if(purchasePage.getTotal()==0){
            return Result.error("-1","未查询到任何采购单~");
        }else{
            return Result.success(purchasePage);
        }
    }


    @PostMapping("/addPurchase")
    public void addPurchase(@RequestBody Purchase purchase){
        purchaseMapper.insert(purchase);
    }

    @GetMapping("/getNotHandlePurchase")
    public Result<?> getNotHandlePurchase(@RequestParam(defaultValue = "1") Integer number, @RequestParam(defaultValue = "10") Integer pagesize){
        LambdaQueryWrapper<Purchase> wrapper = Wrappers.<Purchase>lambdaQuery();
        wrapper.eq(Purchase::getIshandle,0);
        Page<Purchase> purchasePage = purchaseMapper.selectPage(new Page<>(number,pagesize),wrapper);
        return Result.success(purchasePage);
    }

    @PostMapping("/inStore")
    public void inStore(@RequestBody String id){
        Purchase purchase = purchaseMapper.selectOne(Wrappers.<Purchase>lambdaQuery().eq(Purchase::getId,id));
        purchase.setIshandle(1);
        purchaseMapper.updateById(purchase);    // 更新订购单状态
        // 将每一件物品编上号送到库中
        Assets assets = new Assets("",purchase.getKind(),purchase.getBrand(),0,0,0,purchase.getId(),0,1,0,0);
        Calendar calendar = new GregorianCalendar();
        String indate = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+(calendar.get(Calendar.DAY_OF_MONTH)<=9?"0"+calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH));
        OutInPut outInPut = new OutInPut("",indate,"");
        String key = "0";
        // 更新仓库库存信息
        Place place = placeMapper.selectOne(Wrappers.<Place>lambdaQuery().eq(Place::getId,0));
        place.setCount(place.getCount()+purchase.getNum());
        placeMapper.updateById(place);
        for(int i=1;i<=purchase.getNum();i++){
            if(i<=9){
                assets.setId(purchase.getId()+key+"0"+i);
            }else{
                assets.setId(purchase.getId()+key+i);
            }
            assetsMapper.insert(assets);
            outInPut.setId(assets.getId());
            outInPutMapper.insert(outInPut);
        }
    }

    @PostMapping("/getAllPurchases")
    public List<ClassData> getAllPurchases(){
        List<Purchase> purchases = purchaseMapper.selectList(Wrappers.lambdaQuery());
        List<ClassData> res = new ArrayList<>();
        purchases.forEach(value -> {
            res.add(new ClassData(value.getKind(),value.getNum()));
        });
        return res;
    }
}
