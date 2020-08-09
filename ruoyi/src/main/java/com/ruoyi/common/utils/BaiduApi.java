package com.ruoyi.common.utils;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.framework.config.BaiduApiConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaiduApi {


    public static String vatInvoice(File file,String ticketType) {
        try {
            // 本地文件路径
            byte[] imgData = FileUtil.readFileByBytes(file);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，
            // 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = "";
            switch (ticketType){
                case "1":result=HttpUtil.post(BaiduApiConfig.getInvoiceUrl(), accessToken, param);break;
                case "2":result=HttpUtil.post(BaiduApiConfig.getQuotaInvoiceUrl(), accessToken, param);break;
                case "3":;break;
                case "4":result=HttpUtil.post(BaiduApiConfig.getTrainTicketUrl(), accessToken, param);break;
                case "5":result=HttpUtil.post(BaiduApiConfig.getAirTicketUrl(), accessToken, param);break;
                case "7":result=HttpUtil.post(BaiduApiConfig.getTaxiTicketUrl(), accessToken, param);break;
                default:break;
            }
            return result;
        } catch (Exception e) {
            log.error("百度云解析发票照片失败!",e);
        }finally {
            if(file.exists()){
                file.delete();
            }
        }
        return null;
    }

    /**
     * 获取API访问token 该token有一定的有效期，token要自行管理，当失效时需要重新获取.
     */
    private static String getAuth() {
        // 获取token地址
        String getAccessTokenUrl = BaiduApiConfig.getAuthHost()
                // 1. grant_type为固定参敄1¤7
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + BaiduApiConfig.getApiKey()
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + BaiduApiConfig.getSecretKey();
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历响应头
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            /**
             * 返回结果示例
             */

            JSONObject jsonObject = JSONObject.parseObject(result.toString());
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            System.err.printf("获取token失败");
            log.error("获取token失败!",e);
        }
        return null;
    }

}
