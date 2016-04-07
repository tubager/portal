var header = document.getElementById("header-area");
if(header){
	header.innerHTML = '<nav class="navbar navbar-static-top white-bg" role="navigation">\
        <div class="navbar-header">\
    <button aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed" type="button">\
        <i class="fa fa-reorder"></i>\
    </button>\
    <img src="/img/landing/logo.png" style="max-height: 55px;" />\
</div>\
<div class="navbar-collapse collapse" id="navbar">\
    <ul class="nav navbar-nav">\
        <li class="active">\
            <a aria-expanded="false" role="button" href="/index.html">首页</a>\
        </li>\
        <li class="dropdown">\
            <a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"> 精彩游记 <span class="caret"></span></a>\
            <ul role="menu" class="dropdown-menu">\
                <li><a href="#">热门</a></li>\
                <li><a href="#">最新</a></li>\
                <li><a href="#">搜索</a></li>\
            </ul>\
        </li>\
        <li class="dropdown">\
            <a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"> 创意线路 <span class="caret"></span></a>\
            <ul role="menu" class="dropdown-menu">\
                <li><a href="#">最新</a></li>\
                <li><a href="#">经典</a></li>\
                <li><a href="#">热门</a></li>\
            </ul>\
        </li>\
        <li class="dropdown">\
            <a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"> 个人中心 <span class="caret"></span></a>\
            <ul role="menu" class="dropdown-menu">\
        		<li><a id="tbg-menu-profile" href="/account/profile.html">我的信息</a></li>\
                <li><a id="tbg-menu-edit" href="#">写游记</a></li>\
                <li><a href="/account/my_article.html">我的游记</a></li>\
                <li><a href="#">我的线路</a></li>\
            </ul>\
        </li>\
        <li class="dropdown">\
            <a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"> 关于我们 <span class="caret"></span></a>\
            <ul role="menu" class="dropdown-menu">\
                <li><a href="#">关于我们</a></li>\
                <li><a href="#">联系我们</a></li>\
            </ul>\
        </li>\
    </ul>\
    <ul class="nav navbar-top-links navbar-right">\
        <li id="loginMenuItem">\
            <a href="/login.html">\
                <i class="fa fa-sign-in"></i>登录/注册\
            </a>\
        </li>\
		<li id="logoutMenuItem" class="dropdown" style="display:none;">\
			<a aria-expanded="false" role="button" href="#" class="dropdown-toggle" data-toggle="dropdown"><span id="loginUserName"></span><span class="caret"></span></a>\
			<ul role="menu" class="dropdown-menu">\
		        <li><a href="/logout">退出登录</a></li>\
		    </ul>\
	    </li>\
    </ul>\
</div>\
</nav>';
}

$.ajax({
	url:'/currentuser',
    type:'GET',
    headers:{
        Accept:"application/json",
        "Content-Type": "application/json;charset=UTF-8"
    }
}).done(function(data){
	if(data && data.name != "anonymousUser"){
		var name = document.getElementById("loginUserName");
		if(name){
			name.textContent = data.nickName || data.name;
		}
		document.getElementById("loginMenuItem").style["display"] = "none";
		document.getElementById("logoutMenuItem").style["display"] = "inline-block";
	}
});

$(document).ready(function(){
	var menu = document.getElementById("tbg-menu-edit");
	if(document.body.clientWidth < 768){
		menu.href = "/account/new_article_m.html";
	}
	else{
		menu.href = "/account/new_article.html";
	}
});