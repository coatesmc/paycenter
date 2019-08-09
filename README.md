
提供给大家一个微信支付和支付宝支付集成工具类用于大家学习使用。

#系统与系统之间访问加密  RSA
RSACoder 中配置
#公钥
default_public_key: xxxx
#私钥
default_private_key: xxx
在配置文件中配置

#支付宝加密
alipay:
    merchant_private_key: xxx 
    alipay_public_key: xxx
    notify_url: 回调地址可以写死
    return_url:同步地址
    gatewayUrl: https://openapi.alipaydev.com/gateway.do
    
weixin:
    appID: 
    mchID: 
    key: 