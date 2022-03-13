<!DOCTYPE html>
<html lang = "zh">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title>后台管理系统主页</title>
<#include "../common/header.ftl"/>

</head>

<body>
<div class="lyear-layout-web">
  <div class="lyear-layout-container" >
    <!--左侧导航-->
    <aside class="lyear-layout-sidebar" style="background-color:burlywood" >

      <!-- logo -->
      <div id="logo" class="sidebar-header" style="background-color:burlywood;color: white">
        <a href="index.html">后台管理系统</a>
      </div>
      <div class="lyear-layout-sidebar-scroll" style="background-color:burlywood">
        <#include "../common/left-menu.ftl"/>
      </div>

    </aside>
    <!--End 左侧导航-->

    <#include "../common/header-menu.ftl"/>

    <!--页面主要内容-->
    <main class="lyear-layout-content">

      <div class="container-fluid">




        <div class="row">

          <div class="col-sm-6">
            <div class="card">
              <div class="card-header"><h4>会员性别统计</h4></div>
              <div class="card-body">
                <canvas id="chart-line-3"></canvas>
              </div>
            </div>
          </div>

          <div class="col-sm-6">
            <div class="card">
              <div class="card-header"><h4>票房排行榜前五</h4></div>
              <div class="card-body">
                <canvas id="chart-vbar-1"></canvas>
              </div>
            </div>
          </div>

        </div>

      </div>

    </main>
    <!--End 页面主要内容-->
  </div>
</div>
<#include "../common/footer.ftl"/>
<script type="text/javascript" src="/admin/js/perfect-scrollbar.min.js"></script>
<script type="text/javascript" src="/admin/js/Chart.js"></script>
<script type="text/javascript" src="/admin/js/main.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
new Chart($("#chart-line-3"), {
type: 'bar',
data: {
labels: ['男', '女'],
datasets: [{
label: "性别统计",
backgroundColor: "rgba(51,202,185,0.5)",
borderColor: "rgba(0,0,0,0)",
hoverBackgroundColor: "rgba(51,202,185,0.7)",
hoverBorderColor: "rgba(0,0,0,0)",
data: [[${nan}],[${nv}]]
        }]
      },
      options: {
scales: {
yAxes: [{
ticks: {
beginAtZero: true
}
          }]
        },
        axisLabel: { interval: 0, rotate: 40 }
      }
    });
	new Chart($("#chart-vbar-1"), {
type: 'bar',
data: {
labels: [<#list topMovieList as movie>"<#if movie.name?length gt 4>${movie.name?substring(0,4)}...<#else>${movie.name}</#if>",</#list>],
datasets: [{
label: "累计票房",
backgroundColor: "rgba(51,202,185,0.5)",
borderColor: "rgba(0,0,0,0)",
hoverBackgroundColor: "rgba(51,202,185,0.7)",
hoverBorderColor: "rgba(0,0,0,0)",
data: [<#list topMovieList as movie>"${movie.totalMoney}",</#list>]
}]
	    },
	    options: {
scales: {
yAxes: [{
ticks: {
beginAtZero: true
}
	            }]
	        },
	        axisLabel: { interval: 0, rotate: 40 }
	    }
	});
});

</script>
</body>
</html>
