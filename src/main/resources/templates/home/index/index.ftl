<!DOCTYPE html>
<html lang = "zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta charset="utf-8" />
    <meta name="renderer" content="webkit">
    <title>电影票网_电影在线选座购票平台</title>
    <meta name="keywords" content="电影票,电影票网,电影院,影票,电影,电影票，电影"/>
    <meta name="description" content="电影票网是最大的电影票在线选座平台，同时电影票网还提供电影排期，影院信息查询、本土电影行业资讯等服务。看电影，来电影票选座"/>
    <#include "../common/head-css.ftl"/>
</head>
<body>
	<#include "../common/header.ftl"/>
    <div id="body"  class="main" style="background-color: wheat">
   <#-- <div class="main-ad">
        <ul class="slides">
				<li style="background-image: url(/home/images/00.jpg);">
               </li>
			   <li style="background-image: url(/home/images/0.jpg);">
               </li>
                <li style="background-image: url(/home/images/1.png);">
               </li>
			    <li style="background-image: url(/home/images/2.png);">
               </li>
			    <li style="background-image: url(/home/images/3.png);">
               </li>
        </ul>
    </div>-->
    <div class="wrapper-main">
        <div class="hot-film-index pt30">
            <div class="title">
                <span class="titb">推荐</span>

                <span class="current"><em class="blue">1</em>/2</span>
            </div>
            <ul class="fn-clear slides">
                    <#list topMovieList as topMovie>
                    <li>
                        <a href="/home/movie/detail?id=${topMovie.id}" title="${topMovie.name}" target="_blank"><img src="/photo/view?filename=${topMovie.mainPic}" alt="${topMovie.name}" /></a>
                        <a class="overhide" href="/home/movie/detail?id=${topMovie.id}" title="${topMovie.name}" target="_blank">${topMovie.name}</a>

                    </li>
                    </#list>
            </ul>
        </div>




        <div class="future-film-index pt30 fn-clear">
            <div class="future-film">
                <div class="title pb10">
                    <span class="titb">热门预告</span>
                </div>
                <ul class="fn-clear">
                        <#list topVideoMovieList as videoMovie>
                        <li>
                            <a href="/home/movie/detail?id=${videoMovie.id}" target="_blank"><img src="/photo/view?filename=${videoMovie.mainPic}" alt="${videoMovie.name}" width="140" height="193" /></a>
                            <p class="overhide"><a href="/home/movie/detail?id=${videoMovie.id}">${videoMovie.name}</a></p>
                            <a href="javascript:void(0);" data-url="/download/download_video?filename=${videoMovie.video}" rel="#futurefilmPlay" class="btn-video"></a>
                        </li>
                        </#list>
                </ul>
            </div>
            <div class="top-cinema">
                <div class="title pb10">
                    <span class="titb">票房排行</span>
                </div>
                <ul>
                     <#list topMoneyMovieList as moneyMovie>
                        <li class="fn-clear">
                        	<a href="/home/movie/detail?id=${moneyMovie.id}" title="${moneyMovie.name}" target="_blank">
                        		<#if moneyMovie.name?length gt 25>
                        		${moneyMovie.name?substring(0, 25)}...
                        		<#else>
                        		${moneyMovie.name}
                        		</#if>
                        	</a>
                        	<span>${moneyMovie.rate!"0"}</span>
                        </li>
                     </#list>
                </ul>
            </div>
        </div>
    </div>
    <div id="futurefilmPlay">
        <div class="box-video"></div>
    </div>

        </div>
<#include "../common/footer-js.ftl"/>
        <#include "../common/footer.ftl"/>
        <#include "../common/login-dialog.ftl"/>



<script type="text/javascript">
$(function () {
$(".hot-active-index .active-recomment li:last").addClass("fn-right");
$(".gyfc-film-index .gyfc-film li:not(:last)").addClass("mr31");
$(".future-film-index .future-film li:not(:last)").addClass("mr30");
});
$(window).load(function () {
$('.main-ad').flexslider({
animation: "slide"
});
    $('.hot-film-index').flexslider({
animation: "slide",
slideshow: false,
controlNav: false,
itemWidth: 169,
after: function (slider) {
$(".hot-film-index .current em").text(slider.currentSlide+1);
}
    });
    $(".main-ad").hover(function () {
$(".main-ad .flex-direction-nav").show();
}, function () {
$(".main-ad .flex-direction-nav").hide();
});
});

</script>

<script type="text/javascript">
    $(function () {
$(".classic-comment-index .new li p:first").show();
$(".classic-comment-index .new li").hover(function () {
$(this).siblings().find("p").hide();
$(this).find("p").show();
}, function () {

});
        $('.score').jRating({
rateMax: 10,
isDisabled: true,
length: 5,
type: 'small',
smallStarsPath: '/home/images/ico_tb_stars_small.png'
});
    });
    $(".future-film-index .btn-video").overlay({
closeOnClick: false,
top: 'center',
mask: {
color: '#333',
closeSpeed: 100,
opacity: 0.8
},
        onBeforeLoad: function () {
$('body').on('mousewheel', function (e) {
scrollFunc(e);
});
        },
        onBeforeLoad: function (e) {
var video = $(e.target);
var html = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="600" height="400">';
html += '<param name="movie" value="/home/images/flvplayer.swf" />';
html += '<param name="quality" value="high" />';
html += '<param name="allowFullScreen" value="true" />';
html += '<param name="wmode" value="transparent"/>';
html += '<param name="FlashVars" value="vcastr_file=' + video.attr("data-url") + '&&IsAutoPlay=1" />';
html += '<embed src="/home/images/flvplayer.swf" womode="transparent" allowfullscreen="true" flashvars="vcastr_file=' + video.attr("data-url") + '&&IsAutoPlay=1" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="600" height="400"></embed>';
html += '</object>';
$("#futurefilmPlay .box-video").html(html);
},
        onClose: function () {
$('body').off('mousewheel');
$("#futurefilmPlay .box-video").empty();
}
    });
</script>
</body>
</html>
