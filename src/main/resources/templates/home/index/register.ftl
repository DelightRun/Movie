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
    <form id="register" method="POST" action="/home/index/toregister">
    <div class="box-register">
        <h2>免费注册电影票系统</h2>
        <p class="fn-clear box-tel">
            <input name="mobile" class="tel fn-left" type="text" placeholder="手机号" maxlength="11" />
        </p>
        <p class="fn-clear box-tel">
            <input name="password" class="tel fn-left" type="password" placeholder="密码" maxlength="11" />
        </p>
        <p class="fn-clear"><button type="submit" class="btn do-register">立即注册</button></p>
    </div>
    <div class="loginmenthod-register">
        <p>已有账户，点此登录</p>
        <p class="fn-clear box-btn">
            <a rel="#verlayLogin" href="javascript:void(0)" class="boxLogin btn">立即登录</a>
        </p>
    </div>
    </form>
</div>
</div>
<#include "../common/footer-js.ftl"/>
<#include "../common/footer.ftl"/>
<#include "../common/login-dialog.ftl"/>
<script>
$("#register").submit(function (event) {
    var $this = $(this);
    var mobile = $.trim($("input[name=mobile]").val());
    var password = $.trim($("input[name=password]").val());
    if (!mobile) {
        alert("请输入手机号码！");
        return false;
    }
    if (!password) {
        alert("请输入密码！");
        return false;
    }
    $.ajax({
        url: "/home/index/toregister",
        data: {
            mobile: mobile,
            password: password
        },
        type: 'post',
        dataType: 'json',
        encode: true
    }).done(function (data) {
        if (data.code == 0) {
            window.location.href = "/home/index/index";
        } else {
            alert(data.msg);
        }
    });
    event.preventDefault();
});
</script>

</body>
</html>
