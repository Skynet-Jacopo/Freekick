package com.football.freekick.http;

import com.football.freekick.App;

/**
 * Created by ly on 2017/11/18.
 */

public class Url {

    public static final String BaseUrl = "http://api.freekick.hk/api/";
    public static final String ZH_HK = "zh_HK/";
    public static final String EN = "en/";

    public static final String REGISTER = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth" : "http://api" +
            ".freekick.hk/api/en/auth";
    public static final String LOGINURL = App.isChinese ? "http://api.freekick.hk/api/zh_HK/auth/sign_in" :
            "http://api.freekick.hk/api/en/auth/sign_in";
    //Create team創建球隊
    public static final String CREATE_TEAM = BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams";
    //Get advertisements list獲取廣告列表
    public static final String ADVERTISEMENTS = BaseUrl + (App.isChinese ? ZH_HK : EN) + "advertisements";
    //獲取場地
    public static final String PITCHES = BaseUrl + (App.isChinese ? ZH_HK : EN) + "pitches";
    //創建比賽
    public static final String MATCHES = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches";

}
