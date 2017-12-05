package com.football.freekick.http;

import com.football.freekick.App;

/**
 * Created by ly on 2017/11/18.
 */

public class Url {


    public static final String NEWS = "news";
    public static final String POINT_OF_VIEW = "point_of_view";


    public static final String BaseUrl = "http://api.freekick.hk/api/";
    public static final String BaseImageUrl = "http://www.freekick.hk";
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
    //球隊搜索列表
    public static final String AVAILABLE_MATCHES =  BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams/";
    //文章
    public static final String ARTICLES = BaseUrl + (App.isChinese ? ZH_HK : EN) + "articles";
    //參與約賽
    public static final String JOIN_MATCHES = BaseUrl + (App.isChinese ? ZH_HK : EN) + "join_matches";
    //我的主牆http://api.freekick.hk/api/en/matches/coming
    public static final String MATCHES_COMING = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/coming";
    //登出
    public static final String SIGN_OUT = BaseUrl + (App.isChinese ? ZH_HK : EN) + "auth/sign_out";
    //設置
    public static final String SETTINGS = BaseUrl + (App.isChinese ? ZH_HK : EN) + "settings";
    //邀請球隊
    public static final String MATCHES_INVITE = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/invite";
    //http://api.freekick.hk/api/en/matches/<matchID>
    //Get match detail
    public static final String MATCH_DETAIL = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/";//+matchID

}
