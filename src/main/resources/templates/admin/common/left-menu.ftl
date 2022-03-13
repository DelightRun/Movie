<nav class = "sidebar-main">
  <ul class="nav nav-drawer" style="background-color:burlywood;color: white">
    <li class="nav-item"> <a href="/system/index">后台首页</a> </li>
    <#if userTopMenus??>
    <#list userTopMenus as userTopMenu>
    	<li class="nav-item nav-item-has-subnav">
	      <a href="javascript:void(0)">${userTopMenu.name}</a>
	      <ul class="nav nav-subnav">
	        <#if userSecondMenus??>
	        <#list userSecondMenus as userSecondMenu>
	        <#if userTopMenu.id == userSecondMenu.parent.id>
	        <li class="second-menu" style="background-color: #4c4c4c"><a href="${userSecondMenu.url}"></i> ${userSecondMenu.name}</a> </li>
	        </#if>
	        </#list>
	        </#if>
	      </ul>
	    </li>
    </#list>
    </#if>
  </ul>
</nav>
