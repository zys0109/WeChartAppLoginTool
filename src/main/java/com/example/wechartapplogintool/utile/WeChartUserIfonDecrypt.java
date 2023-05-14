package com.example.wechartapplogintool.utile;

import cn.hutool.json.JSONObject;
import com.example.wechartapplogintool.common.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

//WeChartUserIfonDecrypt
@Slf4j
@Component
public class WeChartUserIfonDecrypt {


    public WeChartUserIfonDecrypt(){}

    public static String wxDecrypt(String encryptedData, String sessionId, String vi,StringRedisTemplate redisTemplate) throws Exception {
        // 开始解密
        log.info("需要被解密的数据的redis键:"+RedisKey.WX_SESSION_ID+sessionId);
        log.info("redisTemplate:"+redisTemplate);
        String json = redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID + sessionId);
        log.info("需要被解密的对象:"+json);
        JSONObject jsonObject = new JSONObject(json);
        String sessionKey = (String) jsonObject.get("session_key");
        byte[] encData = cn.hutool.core.codec.Base64.decode(encryptedData);
        byte[] iv = cn.hutool.core.codec.Base64.decode(vi);
        byte[] key = cn.hutool.core.codec.Base64.decode(sessionKey);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(encData), "UTF-8");
    }
}
