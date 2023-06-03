package com.example.wechartapplogintool.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.wechartapplogintool.entity.User;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDomain {
    /**
     * 校验必传不能为空
     */
    public static Map<String, String> checkUserName(String userName, String nickName, String data) throws Exception {
        String[] names = userName.split(";");
        Map<String, String> map = new HashMap<>();
        map.put("flag", "false");
        for (String name : names) {
            if (nickName.equals(name)) {
                //保存图片以及用户名 然后返回
                if(StringUtils.isBlank(data)){
                    throw new Exception("用户头像以及用户名不能为空");
                }
                map = JSONObject.parseObject(data,Map.class);
                String newName = map.get("nickName");
                if (StringUtils.isBlank(newName)) {
                    throw new Exception("用户名不能为空");
                }
                String headImag = map.get("portrait");
                if (StringUtils.isBlank(headImag)) {
                    throw new Exception("用户头像不能为空");
                }
                map.put("flag", "true");
                return map;
            }
        }
        return map;
    }

    /**
     * 校验当前用户是否登录或注册，防止前端校验失败
     */
    public static void checkUserRegisterAndLogin(User user) throws Exception {
        if (user == null) {
            throw new Exception("当前用户未登录，请先登录");
        }
        if (StringUtils.isBlank(user.getOpenId())) {
            throw new Exception("当前用户为注册，请先注册");
        }
    }

    /**
     * 校验图片格式是否正确
     */
    public static String  checkImagType(String[] portraitType,String portrait) {
        String type="";
        for(String imagType:portraitType){
            String regex = MessageFormat.format("data:image/{0};base64,", imagType);
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(portrait);
            if (matcher.lookingAt()) {
              return imagType;
            }
        }
        return type;
    }

    /**
     * 校验文件大小限制
     */
    public static void imageSize(String image,String limitSize) throws Exception {
        String str = image.substring(image.indexOf(",") + 1, image.length() - 1).trim(); //计算文件流大小
        int strLength = str.length();//原来的字符流大小，单位为字节
        int size = strLength - (strLength / 8) * 2;//计算后得到的文件流大小，单位为字节
        if (size > (1024 * 1024) * Integer.parseInt(limitSize)) {
            throw new Exception("上传附件不能超过"+limitSize+"M!");
        }
    }

    /**
     * 删除原有的图片
     */
    public static void deleteImag(String url,String imagUrl){
        imagUrl=imagUrl.replace("\\\\","\\");
        if(url.contains(imagUrl)){
            File file = new File(url);
            boolean result=file.delete();
        }
    }
}
