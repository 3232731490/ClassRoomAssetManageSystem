package com.nilu.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nilu.Common.Result;
import com.nilu.entity.Assets;
import com.nilu.entity.User;
import com.nilu.mapper.AssetsMapper;
import com.nilu.mapper.UserMapper;
import com.nilu.util.MultipartFileUtil;
import net.sf.jsqlparser.statement.select.Wait;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 倪路
 * Time: 2021/11/6-9:58
 * StuNo: 1910400731
 * Class: 19104221
 * Description:
 */
@RestController
public class UploadController {

    @Resource
    UserMapper userMapper;

    @Resource
    AssetsMapper assetsMapper;

    // nginx发送不了POST请求
    @PostMapping("/uploadAvatar")
    public Result<?> uploadAvatar(MultipartFile file,String userid) throws Exception {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,userid));
        String originalFilename = file.getOriginalFilename();
        Map<String,Object> paramMap = new HashMap<>();
        Map<String,Object> paramMap1 = new HashMap<>();
        Map<String,Object> paramMap2 = new HashMap<>();
        paramMap.put("fileId",file.getBytes().length+"_"+originalFilename);
        paramMap.put("filename",originalFilename);
        paramMap1.put("email","3232731490@qq.com");
        paramMap1.put("password","20021112");
        paramMap2.put("id",user.getImgid());
        File f = MultipartFileUtil.multipartFileToFile(file);
        paramMap.put("image", f);
        HttpResponse token =HttpRequest.post("http://www.nilupri.xyz/api/token")    // 获取Token
                .form(paramMap1)//表单内容
                .timeout(20000)//超时，毫秒
                .execute();
        JSONObject data1 = JSONUtil.parseObj(JSONUtil.parseObj(token.body()).getStr("data"));  //获取Token
        HttpRequest.post("http://www.nilupri.xyz/api/delete")   // 将用户原来的头像删除
                .header("token",data1.getStr("token"))
                .form(paramMap2)//表单内容
                .timeout(20000)//超时，毫秒
                .execute();
        HttpResponse res =HttpRequest.post("http://www.nilupri.xyz/api/upload") // 上传头像
                .header("token",data1.getStr("token"))
                .form(paramMap)//表单内容
                .timeout(20000)//超时，毫秒
                .execute();
        MultipartFileUtil.delteTempFile(f); // 删除本地文件
        JSONObject data = JSONUtil.parseObj(JSONUtil.parseObj(res.body()).getStr("data"));
        user.setAvatarurl(data.getStr("url"));
        user.setImgid(Integer.parseInt(data.getStr("id")));
        userMapper.updateById(user);
        return Result.success();
    }

    //@PostMapping("/uploadTest")
    //public Result<?> uploadTest(MultipartFile file,String userid) throws Exception {
    //    File file1 = MultipartFileUtil.multipartFileToFile(file);
    //    System.out.println(file1.getName());
    //    return Result.success();
    //}

    //@PostMapping("/uploadAvatar")
    //public Result<?> uploadAvatar(MultipartFile file,String userid) throws Exception {
    //    File file1 = MultipartFileUtil.multipartFileToFile(file,"avatar");
    //    System.out.println(file1.getName());
    //    User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getId,userid));
    //    File file2 = new File(System.getProperty("user.dir")+"\\vue-module\\public\\"+user.getAvatarurl());
    //    System.out.println("--"+file2.getAbsolutePath());
    //    if(file2.exists())  {file2.delete();
    //        System.out.println("deleted");};
    //    String filepath = file1.getAbsolutePath();
    //    System.out.println(filepath);
    //    filepath = filepath.substring(System.getProperty("user.dir").length()+19);
    //    System.out.println(filepath);
    //    user.setAvatarurl(filepath);
    //    System.out.println(user);
    //    userMapper.updateById(user);
    //    return Result.success();
    //}

    //@PostMapping("/uploadAssets")
    //public Result<?> uploadAssets(MultipartFile file,String id) throws Exception {
    //    File file1 = MultipartFileUtil.multipartFileToFile(file,"assets");
    //    System.out.println(file1.getName());
    //    Assets assets = assetsMapper.selectOne(Wrappers.<Assets>lambdaQuery().eq(Assets::getId,id));
    //    File file2 = new File(System.getProperty("user.dir")+"\\vue-module\\public\\"+assets.getAssetsimg().substring(9));
    //    if(file2.exists())  file2.delete();
    //    String filepath = file1.getAbsolutePath();
    //    filepath = filepath.substring(System.getProperty("user.dir").length()+19);
    //    assets.setAssetsimg(filepath);
    //    assetsMapper.updateById(assets);
    //    return Result.success();
    //}
}
