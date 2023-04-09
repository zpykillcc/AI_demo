
package com.example.ai_demo.aip;

import com.example.ai_demo.aip.utils.Base64Util;
import com.example.ai_demo.aip.utils.FileUtil;
import com.example.ai_demo.aip.utils.HttpUtil;

import java.net.URLEncoder;


/**
 * 通用文字识别
 */
public class GeneralBasic {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String generalBasic(String filepath) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        try {
            // 本地文件路径
            String filePath = filepath;
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.f4f72823c8a37e403e111e6393408fa8.2592000.1681097676.282335-31145297";

            String result = HttpUtil.post(url, accessToken, param);
            /*
            Gson gson = new Gson();
            String resultJson = gson.toJson(result);
            System.out.println(resultJson["word_result"]);
             */
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        GeneralBasic.generalBasic(args[0]);
        System.out.println(args[0]);
    }
}