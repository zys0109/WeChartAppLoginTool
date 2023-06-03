package com.example.wechartapplogintool.common.util;

import com.alibaba.fastjson.JSONObject;
import com.example.wechartapplogintool.common.Result;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CommonUtil {
    public static Object objectChangeString(String data, Boolean flag, String obj, String name) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        String result = jsonObject.getString(obj) == null ? "" : jsonObject.getString(obj);
        if (flag && StringUtils.isEmpty(result)) {
           return  new Result(-1,name+"不能为空");
        }
        return result;
    }
    public static Map objectChangeMap(String data, Boolean flag, String obj, String name) {
        if (flag && data == null) {

        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        Map result = (Map) jsonObject.get(obj);
        return result;
    }
}
