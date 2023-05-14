package com.example.wechartapplogintool.entity.dto;

import com.example.wechartapplogintool.model.WeChartUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
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
    private String openId;

    private String wxUnionId;

    //dto拓展属性
    private String token;
    List<String> permissions;
    List<String> roles;
     public void from(WeChartUserInfo weChartUserInfo){
         this.openId=weChartUserInfo.getOpenId();
         this.nickname=weChartUserInfo.getNickName();
         this.password="";
         this.username="";
         this.gender= weChartUserInfo.getGender();
         this.portrait=weChartUserInfo.getAvatarUrl();
     }
}
