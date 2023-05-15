package com.example.wechartapplogintool.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.wechartapplogintool.common.RedisKey;
import com.example.wechartapplogintool.common.Result;
import com.example.wechartapplogintool.entity.User;
import com.example.wechartapplogintool.entity.dto.UserDto;
import com.example.wechartapplogintool.mapper.UserMapper;
import com.example.wechartapplogintool.model.WeChartAuth;
import com.example.wechartapplogintool.model.WeChartUserInfo;
import com.example.wechartapplogintool.service.UserService;
import com.example.wechartapplogintool.utile.JWTUtils;
import com.example.wechartapplogintool.utile.WeChartUserIfonDecrypt;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Value("${wxinfo.getSessionIdUrl}")
    private String getSessionIdUrl;
    @Value("${wxinfo.appId}")
    private String appId;
    @Value("${wxinfo.secret}")
    private String secret;
    @Autowired
    private StringRedisTemplate redisTemplate;

    //@Autowired
    //private WeChartUserIfonDecrypt weChartUserIfonDecrypt;
    @Resource
    private UserMapper userMapper;
    @Override
    public Result getSessionId(String code){
        /***
         * 1、拼接一个url，微信登录凭证校验接口
         * 2、发起get请求获取微信调用结果
         * 3、将调用结果存入redis
         * 4、生成一个sessionId返回给前端作为当前用户的登录标识
         * 5、生成一个sessionId在用户点击一键登录的时候，标识是哪个用户点击的
         * 6、将/GPTAuthLogin/user/getSessionId的返回参数封装好并返回
         */
        String url = getSessionIdUrl.replace("{0}",appId).replace("{1}",secret).replace("{2}",code);
        log.info("获取微信登录标识接口地址:"+url);
        String response = HttpUtil.get(url);
        JSONObject jscode2sessionJson = new JSONObject(response);
        log.info("获取微信登录标识响应:"+response);
        if (jscode2sessionJson.get("errcode") != null){
            return Result.FAIL();
        }else {
            String uuid = UUID.randomUUID().toString();
            log.info("redisKey:"+RedisKey.WX_SESSION_ID+uuid);
            redisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID+uuid,response,30,TimeUnit.MINUTES);
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("sessionId",uuid);
            return Result.SUCCESS(hashMap);
        }
    }

    //登录小程序
    public UserDto login(UserDto userDto){
        String sign = JWTUtils.sign(userDto.getId());
        redisTemplate.opsForValue().set(RedisKey.TOKEN+sign,new JSONObject(userDto).toString(),7,TimeUnit.DAYS);
        userDto.setToken(sign);
        userDto.setUsername(null);
        userDto.setPassword(null);
        userDto.setOpenId(null);
        userDto.setWxUnionId(null);
        log.info("用户("+userDto.getNickname()+")登录成功！！！");
        return userDto;
    }

    //注册小程序
    public UserDto register(User user){
        userMapper.insert(user);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        log.info("新用户"+user.getNickname()+"注册成功！！！");
        return login(userDto);
    }
    public Result authLogin(WeChartAuth weChartAuth){
        /**
         * 1、通过weChartAuth对微信开放数据解密
         * 2、解密完成后得到微信用户信息，其中包括openId等
         * 3、通过opendId校验用户是否存在，存在则登录，不存在则完成注册流程
         * 4、通过jwt技术生成token回传给前端，用户访问资源是携带token进行校验通过则放行，不通过则要求用户登录。
         */
        try {
            String encData = WeChartUserIfonDecrypt.wxDecrypt(weChartAuth.getEncryptedData(), weChartAuth.getSessionId(), weChartAuth.getIv(),redisTemplate);
            WeChartUserInfo weChartUserInfo = JSON.parseObject(encData, WeChartUserInfo.class);
            JSONObject json = new JSONObject(redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID+weChartAuth.getSessionId()));
            weChartUserInfo.setOpenId(json.get("openid").toString());
            log.info("encData:"+encData.toString()+"\n"+"weChartUserInfo:"+weChartUserInfo);
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenId, weChartUserInfo.getOpenId()));
            if (user == null){
                //注册
                User newUser = new User();
                newUser.from(weChartUserInfo);
                log.info("注册时newUser:"+newUser.toString());
                return Result.SUCCESS(register(newUser));
            }else {
                //登录
                UserDto userDto = new UserDto();
                userDto.from(weChartUserInfo);
                UserDto loginUerDto = login(userDto);
                return Result.SUCCESS(loginUerDto);
            }
        } catch (Exception e) {
            log.info("注册/登录小程序时发生异常:"+e.toString());
            return Result.FAIL();
        }
    }
    public Result userInfo(String token,Boolean refresh){
        /**
         * 1、根据当前token验证其是否有效
         * 2、refresh如果是true则需重新生成token并返回用户信息
         * 3、refresh如果是false则不需要重新生成token直接返回用户信息
         */
        log.info("截取前的token:"+token);
        token = token.replace("Bearer", "").trim();
        log.info("截取后的token:"+token);
        boolean verify = JWTUtils.verify(token);
        if (!verify){
            log.info("["+token+"]校验不通过！！！");
            return Result.FAIL();
        }
        String userDtoJson = redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
        if (StringUtils.isBlank(userDtoJson)){
            redisTemplate.delete(RedisKey.TOKEN+token);
            log.info("userDtoJson为空！！！");
            return Result.FAIL();
        }
        UserDto userDto = JSON.parseObject(userDtoJson,UserDto.class);
        if (refresh){
            redisTemplate.delete(RedisKey.TOKEN+token);
            token = JWTUtils.sign(userDto.getId());
            userDto.setToken(token);
            redisTemplate.opsForValue().set(RedisKey.TOKEN+token,new JSONObject(userDto).toString(),7,TimeUnit.DAYS);
            log.info("续签登录态完成！！！");
        }
        userDto.setOpenId(null);
        return Result.SUCCESS(userDto);
    }
}

