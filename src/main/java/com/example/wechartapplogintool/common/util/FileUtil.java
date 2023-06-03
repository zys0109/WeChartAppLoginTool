package com.example.wechartapplogintool.common.util;


import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {


    /**
     * base64转为图片
     */
    public static String  byteToImage(String imag, String imagType, String outputDirectory) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        FileOutputStream fileOutputStream = null;
        try {
            byte[] bytes = decoder.decodeBuffer(imag);
            // 处理数据
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            //生成文件夹
            File targetDir = new File(outputDirectory);
            if (!targetDir.exists()) {
                boolean flag = targetDir.mkdirs();
            }
            //生成文件
            File img = new File(targetDir.getPath() + File.separator + CommonUtil.getUuid("imag") + "." + imagType);
            if (!img.exists()) {
                boolean flag = img.createNewFile();
            }
            fileOutputStream = new FileOutputStream(img.getPath());
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            return img.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream!=null){
                fileOutputStream.close();
            }
        }
        return null;
    }


}
