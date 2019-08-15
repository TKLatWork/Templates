# Oauth示例

    基于http://blog.didispace.com/spring-security-oauth2-xjf-1/

## oauth基本

    场景解释：
    http://www.ruanyifeng.com/blog/2019/04/oauth_design.html
    http://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html
    额外
    https://juejin.im/post/5cc81d5451882524f72cd32c

## JWT？

    1.JWT和Oauth都是基于Token的认证方式，
    都适用于微服务架构（只是说对于不需要访问每个服务都重新走一边原始的身份认证流程而言）
    2.JWT本身是个简单的方案，相对也弱些
    3.JWT极端情况可以做到无状态（全部身份信息保存在JWT token中），但这个做法有风险。
    Oauth依然会需要一个共享的存储（TokenStore）
    4.JWT无法撤销token。

## 那这样和以前的session服务有什么区别？

    起到的大致效果上应该是类似的。。。
    但Oauth有更标准的（相关功能的）定义和实现，并且主要区分在于对于第三方的使用场景的问题。
    （你不能把用户的session直接公开给第三方吧），但实际上会话这个概念的需求无法避免。

## 更新令牌

    可以请求refresh_token，专门用于更新令牌，也就是用户直接向OAUTH服务器申请更新
    
## 销毁令牌

    由于TokenStore的存在，可以撤销。
    
## 加密问题

    Spring的token只是一个MD5加密，作为访问TokenStore的key工作。所以也没有什么安全问题。