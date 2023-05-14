package com.example.wechartapplogintool.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.wechartapplogintool.model.WeChartUserInfo;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {
    //user表的唯一标识
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //用户昵称
    private String nickname;

    //用户账号
    private String username;

    //用户密码
    private String password;

    //用户性别
    private String gender;

    //用户头像
    private String portrait;

    //用户背景图片
    private String background;

    //用户电话号码
    private String phoneNumber;

    //微信用户唯一标识
    private String openId;

    //用户在开放平台的唯一标识符
    private String weChartUnionId;
    private String city;
    private String province;
    private String country;
    public void from(WeChartUserInfo weChartUserInfo){
        this.openId=weChartUserInfo.getOpenId();
        this.nickname=weChartUserInfo.getNickName();
        this.password="";
        this.username="";
        this.gender= weChartUserInfo.getGender();
        this.portrait=weChartUserInfo.getAvatarUrl();
        this.city =weChartUserInfo.getCity();
        this.country= weChartUserInfo.getCountry();
        this.province = weChartUserInfo.getProvince();
    }
}
