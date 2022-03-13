<div class = "header">
    <div class="top-nav">
        <div class="container fn-clear">
              <span class="tip">
              	欢迎来到电影票
              	<#if movie_account??>
              	<span class="user-name">${movie_account.nickname!movie_account.mobile}</span>
              	<a class="loginout" href="/home/index/logout">退出</a>
              	<#else>
              	<a rel="#verlayLogin" href="javascript:void(0)" class="boxLogin blue">登录</a>
              	<a href="/home/index/register">注册</a>
              	</#if>
              </span>
            <ul>
                <li><a href="/home/account/user-center" title="个人中心" data-flag="<#if movie_account??>0<#else>1</#if>">个人中心</a></li>
                <li><a href="/home/account/user-order-list" title="我的订单" data-flag="<#if movie_account??>0<#else>1</#if>">我的订单</a></li>

            </ul>
        </div>
    </div>

    <div class="main-nav" style="background-color: darkred">
        <div class="container fn-clear">
            <div class="location" data-citycode="360100">
               <#if movie_area??>${movie_area.name}<#else><span style="color: white">选择地址</span></#if>
                <ul class="other-city">
                    <#list provinceList as province>
                    	<li style="width:315px;"><a><font size="3px"><b>${province.name}</b></font></a></li>
                    	<#list cityList as city>
                    	<#if province.id == city.provinceId>
                    	<li><a onclick="changecity('${city.id}')" href="javascript:void(0)">${city.name}</a></li>
                    	</#if>
                    	</#list>
                    </#list>
                </ul>
            </div>
            <ul class="nav-list fn-clear">
                <li data-index="index"><a href="/home/index/index" style="color: white">首页</a></li>
                <li data-index="movie"><a href="/home/movie/list" style="color: white">电影</a></li>
                <li data-index="cinema"><a href="/home/cinema/list"style="color: white">影院</a></li>
                <li data-index="news"><a href="/home/news/list" style="color: white">通知公告</a></li>

            </ul>

        </div>
    </div>
</div>
