package com.myplas.q.common.api;

/**
 * 编写： 黄双
 * 电话：15378412400
 * 邮箱：15378412400@163.com
 * 时间：2017/5/16 12:10
 */
public class API {
    /*base url*/
    //测试
//    public final static String BASEURL = "https://ssl.myplas.com/qapi_3/";
    //    正式
    public final static String BASEURL = "https://api.myplas.com/qapi_3/";
    //测试_快速登陆
//    public final static String BASEURL_API = "https://ssl.myplas.com/";
    //正式_快速登陆
    public final static String BASEURL_API = "https://api.myplas.com/";

    /*common*/
    //检查UA
    public final static String CHECK_UA = "common/checkUA";
    //APP更新
    public final static String CHECK_VERSION = "common/checkVersion";
    //微信分享缩略图
    public final static String LOG_IMG_URL = "http://statics.myplas.com/myapp/img/sharelogo.png";
    //测试——微信appid
//     public final static String WXAPI="wxf17d8dabcebe5b1b";
    // 正式——微信appid
    public final static String WXAPI = "wxc0eb2ef58d5df955";
    //分享通讯录
    public final static String PLASTIC_URL = "http://q.myplas.com/plasticzone/plastic#/index";
    //分享文章
    public final static String PLASTIC_SUCRIBLE = "http://q.myplas.com/plasticzone/plastic#/headlinedetail/";
    //分享授信
    public final static String PLASTIC_CREDIT = "http://q.myplas.com/plasticzone/plastic#/credit2/";
    //分享供求
    public final static String PLASTIC_SUPPLY_DEMAND = "http://q.myplas.com/plasticzone/plastic#/supplybuydetail/";
    //分享成功日志
    public final static String SAVE_SHARE_LOG = "wechat/saveShareLog";


    /*Credit*/
    //企查查
    public final static String GET_QI_CHACHA = "credit/getQiChaCha";
    //获取证书
    public final static String CREDIT_CERTIFICATE = "credit/creditCertificate";
    //塑料配资
    public final static String CREDIT_LIMIT_PAGE = "credit/creditLimitPage";
    //企业信用额度
    public final static String CREDIT_PAGE = "credit/creditPage";


    /*User*/
    //初始化界面
    public final static String INIT = "user/init";
    //发送验证码
    public final static String SEND_MSG = "user/sendMsg";
    //找回密码
    public final static String FINF_MY_PWD = "user/finfMyPwd";
    //注册
    public final static String REGISTER = "user/register";
    //普通登陆
    public final static String LOGIN = "user/login";
    //快速登陆
    public final static String SIMPLE_LOGIN = "user/simpleLogin";
    //退出登录
    public final static String LOGOUT = "user/logout";
    //检查登录状态
    public final static String VALIDUSERTOKEN = "user/validUserToken";


    /*Api*/
    //发送验证码
    public final static String SEND_MOBILE_MSG = "user/login/sendMobileMsg";
    //获取验证码--仅供APP使用
    public final static String VCODE = "api/vcode/app";
    //验证码
    public final static String CHK_VCODE = "api/vcode/chkVcode";


    /*friend*/
    //取消或关注
    public final static String FOCUS_OR_CANCEL = "friend/focusOrCancel";
    //发送回复消息
    public final static String SEND_ZONE_CONTACTMSG = "friend/sendZoneContactMsg";
    //获取好友资料
    public final static String GET_ZONE_FRIEND = "friend/getZoneFriend";
    //获取ta的求购或供给
    public final static String GET_TA_PUR = "friend/getTaPur";
    //获取通讯录首页数据
    public final static String GET_PLASTIC_PERSON = "friend/getPlasticPerson";


    /*myInfo*/
    //[我的]首页数据
    public final static String MY_ZONE = "myInfo/myZone";
    //保存名片到服务器
    public final static String SAVE_CARD_IMG = "myInfo/saveCardImg";
    //保存头像到服务器
    public final static String SAVE_PIC_TO_SERVER = "myInfo/savePicToServer";
    //保存资料
    public final static String SAVE_SELFINFO = "myInfo/saveSelfInfo";
    //获取我的资料
    public final static String GET_SELF_INFO = "myInfo/getSelfInfo";
    //偏好设置
    public final static String FAVORATE_SET = "myInfo/favorateSet";
    //关注/粉丝的头像
    public final static String HEAD_PICTURE = "myInfo/headPicture";
    //塑料圈联系人的-我的消息
    public final static String GET_ZONE_CONTACT_MSG = "myInfo/getZoneContactMsg";
    //获取我的引荐
    public final static String GET_MY_INTRODUCTION = "myInfo/getMyIntroduction";
    //获取我的关注 粉丝
    public final static String GET_MY_FUNS = "myInfo/getMyFuns";
    //获取系统消息
    public final static String GET_ROBOT_MSG = "myInfo/getRobotMsg";
    //获取谁看过我详情数据
    public final static String GET_VIEW_HISTORY_DETAILS = "myInfo/getViewHistoryDetails";

    /*product*/
    //塑料圈app之兑换置顶信息
    public final static String NEW_EXCHANGE_SUPPLYORDEMAND = "product/newExchangeSupplyOrDemand";
    //塑料圈app之积分商品列表
    public final static String GET_PRODUCT_LIST = "product/getProductList";
    //塑料圈app之积分商品可选日期
    public final static String GET_VALID_DATE = "product/getValidDate";
    //塑料圈app之购买记录
    public final static String GET_PURCHASE_RECORD = "product/getPurchaseRecord";


    /*pay*/
    //塑料圈app之获取订单
    public final static String GET_PREPAY_ORDER = "pay/getPrePayOrder";
    //塑料圈app之获取可选金额
    public final static String GET_PAY_CONFIG = "pay/getPayConfig";
    //塑料圈app之获取固定金额
    public final static String GET_EXACT_AMOUNT = "pay/getExactAmount";
    //塑料圈app之订单状态
    public final static String UPDATE_ORDER_STATUS = "pay/updateOrderStatus";


    /*score*/
    //塑料圈app之积分记录
    public final static String SCORE_RECORD = "score/scoreRecord";


    /*releaseMsg*/
    //获取通讯录首页数据
    public final static String GET_RELEASE_MSG = "releaseMsg/getReleaseMsg";
    //中间供求信息)获取供求发布(详情)
    public final static String GET_RELEASE_MSG_DETAIL = "releaseMsg/getReleaseMsgDetail";
    //中间供求信息)获取供求发布(详情)的消息回复
    public final static String GET_RELEASE_MSG_DETAIL_REPLY = "releaseMsg/getReleaseMsgDetailReply";
    //二次发布
    public final static String SECOND_PUB = "releaseMsg/secondPub";
    //供求信息置顶之供求信息列表
    public final static String SUPPLYDEMAND_LIST = "releaseMsg/supplyDemandList";
    //供求消息中的出价
    public final static String DELIVER_PRICE = "releaseMsg/deliverPrice";
    //删除我的供给或求购
    public final static String DELETE_MY_MSG = "releaseMsg/deleteMyMsg";
    //判断提交的发布报价(采购1、报价2)数据/user/mypurchase/pub
    public final static String PUB = "releaseMsg/pub";
    //回复供求消息
    public final static String SAVE_MSG = "releaseMsg/saveMsg";
    //获取供求消息的出价
    public final static String GET_DELIVER_PRICE = "releaseMsg/getDeliverPrice";
    //获取我的(留言
    public final static String GET_MY_COMMENT = "releaseMsg/getMyComment";
    //获取我的供给或求购
    public final static String GET_MY_MSG = "releaseMsg/getMyMsg";
    //搜索记录查询
    public final static String SEARCH_RECORD = "releaseMsg/SearchLog";
    //物性表查询列表
    public final static String PHYSICAL_SEARCH = "releaseMsg/physicalSearch";
    //搜塑料app接口
    public final static String PLASTIC_SEARCH = "releaseMsg/plasticSearch";
    //供求信息置顶之搜索选项配置栏目
    public final static String GET_TAB_CONFIG = "releaseMsg/getTabConfig";
    //删除搜索历史记录
    public final static String DEL_SEARCH_RECORD = "releaseMsg/delSearchRecord";
    //搜塑料app接口22
    public final static String PLASTIC_SEARCH_DETAIL = "releaseMsg/plasticSearchDetail";
    //物性表查询详情
    public final static String PHYSICAL_DETAIL = "releaseMsg/physicalDetail";

    /*billingInfo*/

    //我的发票列表
    public final static String BILLINGLIST = "billingInfo/billingList";
    //开票基本信息以及开票明细
    public final static String INVOICE = "billingInfo/invoice";
    //申请开票
    public final static String INVOICEDETAILADD = "billingInfo/invoiceDetailAdd";
    //确认签收
    public final static String ORDERSIGN = "billingInfo/orderSign";
    //发票详情列表
    public final static String BILLINGDETAILLIST = "billingInfo/billingDetailList";

    /*toutiao*/
    //塑料头条
    public final static String TOP_LINE = "toutiao/topLine";
    //塑料头条-头条推荐
    public final static String GET_SUBSCRIBE = "toutiao/getSubscribe";
    //塑料头条
    public final static String GET_CATE_LIST = "toutiao/getCateList";
    //塑料头条-获取订阅频道
    public final static String GET_SELECT_CATE = "toutiao/getSelectCate";
    //塑料头条-详情列表
    public final static String GET_DETAIL_INFO = "toutiao/getDetailInfo";
}
