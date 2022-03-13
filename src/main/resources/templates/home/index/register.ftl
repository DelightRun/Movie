<!DOCTYPE html>
<html lang = "zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=990, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta charset="utf-8" />
    <meta name="renderer" content="webkit">
    <title>电影票网_电影在线选座购票平台</title>
    <meta name="keywords" content="电影票,电影票网,电影院,影票,电影,电影票，电影"/>
    <meta name="description" content="电影票网是最大的电影票在线选座平台，同时电影票网还提供电影排期，影院信息查询、本土电影行业资讯等服务。看电影，来电影票选座"/>
    <#include "../common/head-css.ftl"/>
</head>
<body>
<#include "../common/header.ftl"/>
<div id="body" class="main"style="background-color: wheat" >
<div class="wrapper-register fn-clear">
    <form action="/home/index/toregister" >
    <div class="box-register">
        <h2>免费注册电影票系统</h2>

        <p class="fn-clear box-tel">
            <input name="tel" class="tel fn-left" type="text" value="手机号" maxlength="11" />

        </p>

        <p class="ipt">
            <input name="password" class="pwd bgpwd2" type="password" />

        </p>
        <p class="fn-clear"><button type="submit" class="btn do-register">立即注册</button></p>
        <p class="box-des fn-clear">
            <label class="cor999"><input type="checkbox" checked="checked" class="fn-left agreement" /><span class="fn-left">阅读并接受江西电影票<a href="/help/serviceterms.html" target="_blank">《用户协议》</a></span></label>
            <span class="error">请接受江西电影票《用户协议》</span>
        </p>
    </div>
    <div class="loginmenthod-register">
        <p>已有账户，点此登录</p>
        <p class="fn-clear box-btn"><span class="btn do-login">立即登录</span></p>

    </div>
    </form>
</div>
</div>
<#include "../common/footer-js.ftl"/>
<#include "../common/footer.ftl"/>
<#include "../common/login-dialog.ftl"/>

</body>
</html>
