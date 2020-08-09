package com.ruoyi.framework.security;

import lombok.Data;

@Data
public class WeixinLogin {

    private String country;

    private String unionid;

    private String province;

    private String city;

    private String openid;

    private String sex;

    private String nickname;

    private String headimgurl;
}
