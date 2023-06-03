package com.example.wechartapplogintool.controller;

import cn.hutool.json.JSONObject;
import com.example.wechartapplogintool.common.Result;
import com.example.wechartapplogintool.model.WeChartAuth;
import com.example.wechartapplogintool.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("getSessionId")
    public JSONObject getSessionId(@RequestParam String code) {
        Result result = userService.getSessionId(code);
        log.info("getSessionId返回对象" + result);
        return new JSONObject(result);
    }

    @PostMapping("authLogin")
    public JSONObject authLogin(@RequestBody WeChartAuth weChartAuth) {
        Result result = userService.authLogin(weChartAuth);
        log.info("authLogin返回对象" + result);
        return new JSONObject(result);
    }

    @GetMapping("userinfo")
    public JSONObject userInfo(@RequestHeader("Authorization") String token, Boolean refresh) {
        Result result = userService.userInfo(token, refresh);
        log.info("userInfo返回对象:" + result);
        return new JSONObject(result);
    }

    /**
     * 保存更新用户设置的头像以及用户名
     */
    @PostMapping("saveUserInfo")
    public JSONObject saveUserInfo(@RequestHeader("Authorization") String token, @RequestBody String data) throws Exception {
        Result result = userService.saveUserInfo(token, data);
        log.info("userInfo返回对象:" + result);
        return new JSONObject(result);
    }
}
