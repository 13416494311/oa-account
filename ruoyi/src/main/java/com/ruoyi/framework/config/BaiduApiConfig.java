package com.ruoyi.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "baiduapi")
public class BaiduApiConfig
{
    private static String authHost;

    private static String apiKey;

    private static String secretKey;

    private static String invoiceUrl;

    private static String quotaInvoiceUrl;

    private static String trainTicketUrl;

    private static String airTicketUrl;

    private static String taxiTicketUrl;

    public static String getAuthHost() {
        return authHost;
    }

    public  void setAuthHost(String authHost) {
        BaiduApiConfig.authHost = authHost;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public  void setApiKey(String apiKey) {
        BaiduApiConfig.apiKey = apiKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public  void setSecretKey(String secretKey) {
        BaiduApiConfig.secretKey = secretKey;
    }

    public static String getInvoiceUrl() {
        return invoiceUrl;
    }

    public  void setInvoiceUrl(String invoiceUrl) {
        BaiduApiConfig.invoiceUrl = invoiceUrl;
    }

    public static String getQuotaInvoiceUrl() {
        return quotaInvoiceUrl;
    }

    public  void setQuotaInvoiceUrl(String quotaInvoiceUrl) {
        BaiduApiConfig.quotaInvoiceUrl = quotaInvoiceUrl;
    }

    public static String getTrainTicketUrl() {
        return trainTicketUrl;
    }

    public void setTrainTicketUrl(String trainTicketUrl) {
        BaiduApiConfig.trainTicketUrl = trainTicketUrl;
    }

    public static String getAirTicketUrl() {
        return airTicketUrl;
    }

    public void setAirTicketUrl(String airTicketUrl) {
        BaiduApiConfig.airTicketUrl = airTicketUrl;
    }

    public static String getTaxiTicketUrl() {
        return taxiTicketUrl;
    }

    public void setTaxiTicketUrl(String taxiTicketUrl) {
        BaiduApiConfig.taxiTicketUrl = taxiTicketUrl;
    }
}
