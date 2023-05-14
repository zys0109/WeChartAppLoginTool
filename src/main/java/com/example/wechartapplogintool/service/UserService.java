package com.example.wechartapplogintool.service;

import com.example.wechartapplogintool.common.Result;
import com.example.wechartapplogintool.model.WeChartAuth;

public interface UserService {
    Result getSessionId(String code);
    Result authLogin(WeChartAuth weChartAuth);

    Result userInfo(String token, Boolean refresh);
}
