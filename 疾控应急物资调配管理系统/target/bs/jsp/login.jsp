<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<title>登录</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/assets2/js/jquery.min.js"></script>

<!--  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/assets2/css/style.css"> -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/assets2/css/loader-style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/assets2/css/bootstrap.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/assets2/css/signin.css">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<!-- Fav and touch icons -->
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/assets2/ico/minus.png">
	</head>
<style>
	html {
		width: 100%;
		height: 100%;
	}
	body {
		height: 100vh;
		background-position: center center;
		background-size: cover;
		background-repeat: no-repeat;
		overflow: hidden !important;
		width: 100%;
		height: 100%;
						/*
						background-image: url(http://codegen.caihongy.cn/20210810/cf858e8cc205445ba562c7ce967190f1.jpg) !important;
						 */
		background-image: url("../resources/assets2/img/bj.jpg") !important;

	}
	.container {
		padding: 0;
		margin: 0;
		display: flex;
		height: 100%;
		width: 100%;
		justify-content: center;
		align-items: center;
	}
	#login-wrapper {
		display: flex;
		flex-direction: column;
		position: inherit;
		height: 300px;
		width: 600px;
		margin: 0px 0 0 0;
		padding: 0;
		border-radius: 100px;
		border-width: 8px;
		border-style: solid;
		border-color: rgba(255, 255, 255, 1);
		background-color: rgba(255, 255, 255, 0.6);
		box-shadow: 0 0 0px 0px rgba(110, 11, 11, 0);
	}
	#login-wrapper h1 {
		line-height: 310px;
		width: 310px;
		color: rgba(0, 0, 0, 1);
		font-size: 16px;
		margin: -120px 0 0 170px;
		padding: auto;
		border-radius: 50%  0 100% 0;
		border-width: 0;
		border-style: solid;
		border-color: rgba(0,0,0,.3);
		background-color: rgba(250, 212, 0, 0);
		/*box-shadow: -260px 30px 0px -58px rgba(255, 202, 0, 1);*/
		text-align: center;
	}
	
	#loginForm .form-group-l-r {
		display: flex;
		flex-wrap: wrap;
	}
	
	#loginForm .form-group {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		height: 60px;
		width: 250px;
		margin: 0 0 0 170px;
		padding: 0;
		border-radius: 0;
		border-width: 0;
		border-style: solid;
		border-color: rgba(0,0,0,1);
		background-color: rgba(255, 120, 0, 0);
		box-shadow: 0 0 6px rgba(255,0,0,0);
	}
	
	#loginForm .form-group .label {
		width: 70px;
		line-height: 44px;
		margin: -240px 0 0 0;
		color: rgba(0, 0, 0, 1);
		font-size: 16px;
		padding: 0;
		border-radius: 0;
		border-width: 0;
		border-style: solid;
		border-color: rgba(0,0,0,0);
		background-color: transparent;
		box-shadow: 0 0 6px rgba(255,0,0,0);
		text-align: center;
	}
	
	#loginForm .form-group .form-control-i {
		width: 180px;
		height: 44px;
		margin: -240px 0 0 0;
		color: rgba(0, 0, 0, 1);
		font-size: 14px;
		padding: 0 10px;
		border-radius: 8px;
		border-width: 1px;
		border-style: solid;
		border-color: #ccc;
		background-color: #fff;
		box-shadow: 0 0 6px rgba(30, 144, 255, 0);
		text-align: center;
	}
	
	#loginForm .form-group .form-control-i:focus {
	    outline: 0 none;
	}
	
	#loginForm .form-group-r {
		display: flex;
		align-items: center;
		width: 320px;
		height: 180px;
		margin: -270px 0 0 160px;
		padding: 140px 0 0 10px;
		border-radius: 0;
		border-width: 0;
		border-style: solid;
		border-color: rgba(0,0,0,1);
		background-color: rgba(0, 206, 209, 0);
		box-shadow: 0 0 0px 0px rgba(0,0,0,0);
	}
	
	#loginForm .form-group-r .checkbox {
		margin: 0 10px 0 0;
		padding: 0;
		display: flex;
		align-items: center;
	}
	
	#loginForm .form-group-r .checkbox label {
		color: #606266;
		font-size: 12px;
		margin: 0;
		margin-top: -1px;
	}
	
	#loginForm .form-group-r .checkbox.active label {
		color: #1e90ff;
		font-size: 12px;
	}
	
	#loginForm .form-group-r .checkbox input {
		margin: 0;
		margin-right: 4px;
	}
	
	#loginForm button.pull-right {
                outline: none;
		width: 140px;
		height: 140px;
		margin: -80px 0 0 440px;
		color: rgba(255, 255, 255, 1);
		font-size: 16px;
		padding: 0;
		border-radius: 100% 0 50% 0;
		border-width: 0;
		border-style: solid;
		border-color: #ccc;
		background-color: skyblue;
		/*background-color: rgba(0, 206, 209, 1);*/
		/*box-shadow: 0px 0px 0px 30px rgba(0, 206, 209, 1);*/
	}
	
	#loginForm a.btn-register {
		display: inline-block;
		width: 90px;
		height: 20px;
		margin: -60px 0 0 10px;
		color: rgba(23, 21, 21, 1);
		font-size: 12px;
		padding: 0;
		border-radius: 0;
		border-width: 0;
		border-style: solid;
		border-color: #ccc;
		background-color: rgba(30, 144, 255, 0);
		box-shadow: 0 0 6px rgba(30, 144, 255, 0);
		display: flex;
		justify-content: center;
		align-items: center;
		text-decoration: none;
	}
	
	#loginForm .codes {
	}
	
	#loginForm .codes input {
		width: calc(100% - 84px);
		height: 44px;
		margin: -240px 0 0 0;
		color: #1e90ff;
		font-size: 14px;
		padding: 0 10px;
		border-radius: 0;
		border-width: 1px;
		border-style: solid;
		border-color: #1e90ff;
		background-color: #fff;
		box-shadow: 0 0 6px rgba(30, 144, 255, 0);
		outline: none;
	}
	
	#loginForm .codes .nums {
		width: 84px;
		height: 44px;
		margin: -240px 0 0 0;
		color: $template2.back.login.code.nums.color;
		font-size: $template2.back.login.code.nums.fontSize;
		padding: 0;
		border-radius: 0;
		border-width: 1px 1px 1px 0;
		border-style: solid;
		border-color: #1e90ff;
		background-color: #f5f5f5;
		box-shadow: 0 0 6px rgba(30, 144, 255, 0);
		outline: none;
	}
</style>
<body>
	<!-- Preloader -->
	<!-- <div id="preloader">
		<div id="status">&nbsp;</div>
	</div> -->
		<div class="container" style="position: relative;">

		<div class="" id="login-wrapper">
			<h1>疾控应急物资调配综合管理系统</h1>
			<form id="loginForm" action="" method="post">
				<div class="form-group">
					<label class="label">用户名</label> <input type="text" id="username" name="username" placeholder="用户名"
						class="form-control-i" required>
				</div>
				<div class="form-group">
					<label class="label">密码</label> <input type="password" name="password" placeholder="密码"
						class="form-control-i" required>
				</div>
				
				
				<div class="form-group-l-r">
					<button class="btn btn btn-primary pull-right" type="submit" onclick="login()">登 录</button>
					<a class="btn-register" href="modules/dpry/register.jsp">调配人员注册</a>
					<a class="btn-register" href="modules/danwei/register.jsp">单位注册</a>
				</div>	
			</form>
									
			<!-- <div class="row">
				<div class="col-md-4 col-md-offset-4">
					<div class="account-box">
						
					</div>
				</div>
			</div> -->
		</div>

	</div>

	<!--  END OF PAPER WRAP -->

	<!-- MAIN EFFECT -->
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets2/js/preloader.js"></script>-->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/assets2/js/bootstrap.js"></script>
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets2/js/app.js"></script>-->
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets2/js/load.js"></script>-->
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets2/js/main.js"></script>-->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/js/jquery.form.js"></script>

	<!--<script src="http://maps.googleapis.com/maps/api/js?sensor=false" type="text/javascript"></script> -->
	<!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets2/js/map/gmap3.js"></script> -->
		<script type="text/javascript">
		
	<%@ include file="utils/menu.jsp"%>
		
	<%@ include file="utils/baseUrl.jsp"%>
		var role = "";
		var accountTableName = "";
		var codes = [{
			num: 1,
			color: '#000',
			rotate: '10deg',
			size: '16px'
		  }, {
			num: 2,
			color: '#000',
			rotate: '10deg',
			size: '16px'
		  }, {
			num: 3,
			color: '#000',
			rotate: '10deg',
			size: '16px'
		  }, {
			num: 4,
			color: '#000',
			rotate: '10deg',
			size: '16px'
		  }]
		
		//渲染角色选择
		function setRoleOption() {
			var box = document.createElement('div');
			box.setAttribute('class', 'form-group-r');
            var menuNum = 0;
			for (var i = 0; i < menus.length; i++) {
                if(menus[i].hasBackLogin=='是') {
                    if(menuNum==0) {
                        role = menus[i].roleName;
                        $('#loginForm').attr('action', baseUrl + menus[i].tableName + '/login');
                        accountTableName = menus[i].tableName;
                    }
                    menuNum++;
                }
			}
            if(menuNum>1) {
                role = '';
                $('#loginForm').attr('action', '');
                accountTableName = '';
                for (var i = 0; i < menus.length; i++) {
                    if(menus[i].hasBackLogin=='是') {
                        //console.log(menu[i].roleName)
                        var div = document.createElement('div');
                        div.setAttribute('class', 'checkbox');
                        var label = document.createElement('label');
                        //label.innerHTML = menus[i].roleName;
                        var checkbox = document.createElement('input');
                        checkbox.setAttribute('type', 'radio');
                        checkbox.setAttribute('name', 'chk');
                        checkbox.setAttribute('value', menus[i].roleName);
                        var attr = "checkRole(" + "\'" + menus[i].roleName + "\',\'"
                                + menus[i].tableName + "\')";
                        checkbox.setAttribute('onclick', attr);
                        //label.setAttribute('class','checkbox inline');
                        label.innerHTML = menus[i].roleName;
                        div.appendChild(checkbox);
                        div.appendChild(label);
                        box.appendChild(div);
                    }
                }
            }
            $('form .form-group-l-r').before(box);
		}
		function checkRole(roleName, tableName) {			
			role = roleName;
			$('#loginForm').attr('action', baseUrl + tableName + '/login');
			accountTableName = tableName;
		}
		
		$('#login-wrapper').on('change', 'input[type=radio]', function () {
		    $('#login-wrapper .form-group-r .checkbox').removeClass('active');
			$(this).parent().addClass('active');
		});
		
		function login() {
			
			$("#loginForm")
					.ajaxForm(
							function(res) {
								if (role == "" || role == null) {
									alert("请选择角色后再登录");
								} else {
									if (res.code == 0) {
										var username = $('#username').val();
										window.sessionStorage.setItem('accountTableName',accountTableName)
						 				window.sessionStorage.setItem('username',username);
										window.sessionStorage.setItem('token',
												res.token);
										window.sessionStorage.setItem('role',
												role);
										http(accountTableName+'/session','GET',{},(res2)=>{
											if(res2.code == 0){
												window.sessionStorage.setItem('userid',res2.data.id);
										        window.location.href = "${pageContext.request.contextPath}/index.jsp";
											}
										});
									} else {
										alert(res.msg);
									}
								}

							});
		}
		function ready() {
			setRoleOption();
			//$('form').attr('action',baseUrl + 'users/login');
		}
		document.addEventListener("DOMContentLoaded", ready);
		
		function randomString() {
			var len = 4;
			var chars = [
			  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			  'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			  'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			  'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
			  '3', '4', '5', '6', '7', '8', '9'
			]
			var colors = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
			var sizes = ['14', '15', '16', '17', '18']
			
			var output = []
			for (var i = 0; i < len; i++) {
			  // 随机验证码
			  var key = Math.floor(Math.random() * chars.length)
			  codes[i].num = chars[key]
			  // 随机验证码颜色
			  var code = '#'
			  for (var j = 0; j < 6; j++) {
			    var key = Math.floor(Math.random() * colors.length)
			    code += colors[key]
			  }
			  codes[i].color = code
			  // 随机验证码方向
			  var rotate = Math.floor(Math.random() * 30)
			  var plus = Math.floor(Math.random() * 2)
			  if (plus == 1) rotate = '-' + rotate
			  codes[i].rotate = 'rotate(' + rotate + 'deg)'
			  // 随机验证码字体大小
			  var size = Math.floor(Math.random() * sizes.length)
			  codes[i].size = sizes[size] + 'px'
			}
			
			var str = ''
			for(var i = 0;i<codes.length;i++) {
				str += '<span style="color:' + codes[i].color + ';transform:' + codes[i].rotate + ';fontSize:' + codes[i].size + ';padding: 0 3px;display:inline-block">'+codes[i].num+'</span>'
			}
			$('#nums').html('').append(str);
		}
		
		randomString();
	</script>

</body>

</html>
