package com.football.freekick.http;

/**
 * Url地址常量类
 * <p/>
 * Created by liuqun on 2016/11/28.
 */
public class Url {
    public static final String APP_ID = "wx0b437443f3dce8c6";//微信APPID

    public static final String TAG = "Activity";

    public static String ON_FAILED_MESSAGE = "加载失败";
    public static String ON_ERROR_MESSAGE  = "获取失败";
    public static String ON_LOAD_FAILED    = "加载失败";

    public static final String QQ_NUMBER    = "3095855441";//客服QQ号码
//    public static final String PHONE_NUMBER = "400-6074-177";//客服电话
    public static final String PHONE_NUMBER = "4006074177";//客服电话

    //    public static final String URL_MAIN         = "http://192.168.1.140/winshop/app.php";//接口地址
    public static final String URL_MAIN         = "http://139.224.239.148:8080/app.php";//接口地址
    //    public static final String WWW_TAOYIBAO_COM = "192.168.1.140";//设置Cookie时的domain值
    public static final String WWW_TAOYIBAO_COM = "139.224.239.148";//设置Cookie时的domain值

    public static final String INDEX_AD_LIST          = "/Index/adList";//首页轮播图
    public static final String PUBLIC_VERIFY          = "/Public/verify";//注册时获取验证码图片
    public static final String PUBLIC_SEND_CODE       = "/Public/sendCode";//2-1.输入手机号码，发送短信验证码
    public static final String PUBLIC_CHECK_LINE_CODE = "/Public/checkLineCode";//2-2.验证短信码
    public static final String PUBLIC_REGISTER        = "/Public/register";//2-3.完成注册
    public static final String PUBLIC_LOGIN           = "/Public/login";//3.登录
    public static final String PUBLIC_OUT             = "/Public/out";//3-1.退出登录

    public static final String INDEX_RUSH_BUY_LIST = "/Index/rushBuyList";//4.首页限时抢购列表
    public static final String GOODS_RUSH_BUY_LIST = "/Goods/rushBuyList";//4-1.首页限时抢购更多
    public static final String GOODS_RUSH_BUY_INFO = "/Goods/rushBuyInfo";//4-2 限时抢购详情

    public static final String INDEX_EXPERT_LIST      = "/Index/expertList";//5.首页艺术顾问列表
    public static final String INDEX_EXPERT_MORE_LIST = "/Index/expertMoreList";//5-1.艺术顾问更多

    public static final String INDEX_ARTIST_LIST      = "/Index/artistList";//艺术家推荐
    public static final String INDEX_ARTIST_MORE_LIST = "/Index/artistMoreList";//6-1.艺术家更多

    public static final String INDEX_GOODS_LIST      = "/Index/goodsList";//6.首页商品列表（包含名画推荐、新品、热销、1000到5000，5000到5万，5万到50万，50万到500万，500万以上）
    public static final String INDEX_WANTBUY_LIST    = "/Index/wantbuyList";//8.首页求购信息列表
    public static final String MEMBER_WANTBUY_LIST   = "/Member/wantbuyList";//8-2 我的求购列表（个人中心）
    public static final String MEMBER_WANTBUY_ADD    = "/Member/wantbuyAdd";//8-3 添加求购
    public static final String MEMBER_WANTBUY_EDIT   = "/Member/wantbuyEdit";//8-4  编辑求购
    public static final String MEMBER_DELETE_WANTBUY = "/Member/deleteWantbuy";//8-5  删除求购

    public static final String GOODS_SEARCH_RECOMMEND  = "/Goods/searchRecommend";//9.搜索推荐词语
    public static final String GOODS_SEARCH_HISTORY    = "/Goods/searchHistory";//10.搜索历史词语（必须登录）
    public static final String GOODS_CLEARN_HISTORY    = "/Goods/clearnHistory";//10-1.清空最近搜索
    public static final String GOODS_SEARCH            = "/Goods/search";//11.搜索
    public static final String GOODS_GOODS_INFO        = "/Goods/goodsInfo";//11-1 商品详情
    public static final String MEMBER_COLLECT          = "/Member/collect";//11-2 收藏、取消收藏商品（必须登录）
    public static final String MEMBER_AVAILABLE_COUPON = "/Member/availableCoupon";//11-3.可用优惠券列表
    public static final String GOODS_GOODS_COMMENT     = "/Goods/goodsComment";//11-4.单个商品所有评论

    public static final String GOODS_SEARCH_CONDITION  = "/Goods/searchCondition";//12.筛选条件
    public static final String GOODS_GOODS_CATEGORY    = "/Goods/goodsCategory";//13.商品分类
    public static final String GOODS_AD_LIST           = "/Goods/adList";//13-1.商品分类上的广告列表
    public static final String MEMBER_COLLECT_LIST     = "/Member/collectList";//14.商品收藏列表
    public static final String MEMBER_FOOTPRINT_LIST   = "/Member/footprintList";//15.浏览足迹列表
    public static final String MEMBER_DELETE_FOOTPRINT = "/Member/deleteFootprint";//15-1.删除浏览足迹
    public static final String SHOPCART_INTOCART       = "/Shopcart/intocart";//16.加入购物车
    public static final String SHOPCART_INDEX          = "/Shopcart/index";//16-1.购物车列表
    public static final String SHOPCART_HANDLE         = "/Shopcart/handle";//16-2.移入商品收藏和删除
    public static final String SHOPCART_CHANGE         = "/Shopcart/change";//16-3增加、减少、改变商品数量
    public static final String SHOPCART_CHOSE          = "/Shopcart/chose";//16-4 勾选、消除所选商品
    public static final String SHOPCART_CHOSE_ALL      = "/Shopcart/choseAll";//16-5 全部选中、全部取消购物车
    public static final String SHOPCART_GET_CARTNUM    = "/Shopcart/getCartnum";//16-6 获得当前购物车选中数量
    public static final String SHOPCART_CHOSED_GOODS   = "/Shopcart/chosedGoods";//16-7.获得购物车选中商品列表
    public static final String SHOPCART_ORDER_ADD      = "/Shopcart/orderAdd";//16-8.结算提交订单

    public static final String MEMBER_COUPON_LIST    = "/Member/couponList";//17.我的优惠券列表
    public static final String MEMBER_ADDRESS_LIST   = "/Member/addressList";//18.我的收货地址列表
    public static final String MEMBER_ADDRESS_ADD    = "/Member/addressAdd";//18-1 增加收货地址
    public static final String MEMBER_ADDRESS_EDIT   = "/Member/addressEdit";//18-2 编辑收货地址
    public static final String MEMBER_ADDRESS_DELETE = "/Member/addressDelete";//18-3 删除收货地址

    public static final String MEMBER_ADDRESS_SET_DEFAULT = "/Member/addressSetDefault";//18-4 设置默认收货地址
    public static final String ORDER_ORDER_ADD            = "/Order/orderAdd";//19 下订单，立即购买
    public static final String ORDER_INDEX                = "/Order/index";//20.我的订单列表
    public static final String ORDER_ORDER_INFO           = "/Order/orderInfo";//20-1.订单详情
    public static final String ORDER_CANCEL_ORDER         = "/Order/cancelOrder";//20-2.取消订单
    public static final String ORDER_DELETE_ORDER         = "/Order/deleteOrder";//20-3.删除订单
    public static final String ORDER_CONFIRM_RECEIVE      = "/Order/confirmReceive";//20-4.确认收货
    public static final String ORDER_CHECK_COMMENT        = "/Order/checkComment";//20-5.查看订单评价
    public static final String ORDER_COMMENT              = "/Order/comment";//20-6.提交评价
    public static final String ORDER_REFUND_ORDER         = "/Order/refundOrder";//20-7.申请退款
    public static final String ORDER_BACKBUY_APPLY        = "/Order/backbuyApply";//20-8.申请回购
    public static final String PAY_WALLET_PAY             = "/Pay/walletPay";//20-9.钱包支付订单
    public static final String PAY_ALIPAY_ORDER           = "/Pay/alipayOrder";//20-10.支付宝支付
    public static final String PAY_WECHAT_PAY             = "/Pay/wechatPay";//20-11.微信支付

    public static final String MEMBER_INFO                 = "/Member/Info";//21.登录会员需要信息
    public static final String MEBMER_UPDATE_AVATAR        = "/Member/updateAvatar";//21-1.修改头像
    public static final String MEMBER_UPDATE_NICKNAME      = "/Member/updateNickname";//21-2.修改昵称
    public static final String MEMBER_UPDATE_SEX           = "/Member/updateSex";//21-3.修改性别
    public static final String MEMBER_UPDATE_PW            = "/Member/updatePw";//21-4.修改密码
    /**
     * 21-5.修改支付密码
     * 分为两种情况
     * 第一种：之前未设置过支付密码，第一次设置；无需验证旧支付密码。
     * 第二种：之前已经设置过支付密码，重新设置；必须验证旧支付密码。
     */
    public static final String MEMBER_CHECK_OLD_PAYCODE    = "/Member/checkOldPaycode";//（1）验证旧支付密码
    public static final String MEMBER_SEND_CODE            = "/Member/sendCode";//（2）发送短信码
    public static final String MEMBER_CHECK_CODE           = "/Member/checkCode";//（3）验证短信码
    public static final String MEMBER_UPDATE_PAYCODE       = "/Member/updatePaycode";//（4）设置支付密码
    public static final String MEMBER_UPDATE_PHONE         = "/Member/updatePhone";//21-6.修改登录手机号码（验证码、发送短信验证码全部走注册的）
    public static final String MEMBER_OTHER_INFO           = "/Member/otherInfo";//21-7.其他信息（优惠券数量、商品收藏数量、我的推荐数量、订单数量、求购数量等）
    public static final String MEMBER_PRESENTER            = "/Member/presenter";//22.我的推荐
    public static final String MEMBER_MESSAGE_LIST         = "/Member/messageList";//23.消息列表
    public static final String MEMBER_READ_MESSAGE         = "/Member/readMessage";//23-1.修改为已读状态（当状态status=0的时候，访问此接口）
    public static final String MEMBER_NOREADNUM            = "/Member/noreadnum";//23-2.消息未读数量
    public static final String MEMBER_RECORD_LIST          = "/Member/moneyList";//24. 钱包明细
    public static final String MEMBER_ADD_ALIACCOUNT       = "/Member/addAliaccount";//24-1.添加支付宝
    public static final String MEMBER_ALIACCOUNT_LIST      = "/Member/aliaccountList";//24-2.支付宝列表
    /**
     * 分为两种情况
     * 第一种：未设置支付密码，可以直接删除
     * 第二种：设置支付密码，删除前，必须验证支付密码
     */
    public static final String MEMBER_ALIACCOUNT_DELETE    = "/Member/aliaccountDelete";//24-3.支付宝删除
    /**
     * 第一，必须设置支付密码后，才能提现
     * 第二，提现时必须验证支付密码
     */
    public static final String MEMBER_WITHDRAW             = "/Member/withdraw";//24-4.提现
    public static final String PUBLIC_VERSION              = "/Public/version";//25.获得版本信息（android）
    public static final String PUBLIC_SEND_CODE_FIND       = "/Public/sendCodeFind";//26-1.输入手机号码，发送短信验证码
    public static final String PUBLIC_CHECK_LINE_CODE_FIND = "/Public/checkLineCodeFind";//26-2.验证短信码
    public static final String PUBLIC_FIND_PW              = "/Public/findPw";//26-3.重设密码
    public static final String PUBLIC_FEEDBACK             = "/Public/feedback";//27.提交意见反馈
    public static final String PUBLIC_THIRD_LOGIN          = "/Public/thirdLogin";//28-1.第三方登录授权成功回调地址
    public static final String PUBLIC_BINDING_OLD          = "/Public/bindingOld";//28-2.绑定已有账号
    public static final String PUBLIC_SEND_CODE_BINDING    = "/Public/sendCodeBinding";//28-3.发送短信验证码（绑定新账号）
    public static final String PUBLIC_BINDING_NEW          = "/Public/bindingNew";//28-4.绑定新账号
    public static final String INDEX_AGREEMENT             = "/Index/agreement";//29（1）用户协议
    public static final String INDEX_QUESTION              = "/Index/question";//29（2）常见问题
    public static final String INDEX_BACKBUY_AGREEMENT     = "/Index/backbuyAgreement";//29（3）回购协议
    public static final String INDEX_ABOUT                 = "/Index/about";//29(4)关于

}
