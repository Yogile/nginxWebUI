$(function() {
	
	
	if ($("#adminCount").val() > 0) {
		var remember = window.localStorage.getItem("remember");
		if(remember){
			var name = window.localStorage.getItem("name");
			var pass = window.localStorage.getItem("pass");
			
			$("#name").val(name);
			$("#pass").val(pass);
			$("#remember").prop("checked",true);
			form.render();
		}
		
		layer.open({
			type: 1,
			shade: false,
			title: loginStr.title1,
			closeBtn: false,
			area: ['400px', '350px'], //宽高
			content: $('#windowDiv')
		});
	} else {
		layer.open({
			type: 1,
			shade: false,
			title: loginStr.title2,
			closeBtn: false,
			area: ['400px', '400px'], //宽高
			content: $('#addUserDiv')
		});
	}


})

function login() {
	$("#authCode").val($("#codeInput").val());
	
	if($("#remember").prop("checked")){
		window.localStorage.setItem("name", $("#name").val());
		window.localStorage.setItem("pass", $("#pass").val());
		window.localStorage.setItem("remember", true);
	}else{
		window.localStorage.setItem("name", "");
		window.localStorage.setItem("pass", "");
		window.localStorage.setItem("remember", false);
	}
	
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/login/login',
		data: $("#loginForm").serialize(),
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				location.href = ctx + "adminPage/monitor";
			} else {
				layer.msg(data.msg);
			}
			
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});

}

function getAuth() {
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/login/getAuth',
		data: {
			name : $("#name").val(),
			pass : $("#pass").val(),
			code  :  $("#code").val()
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				if(data.obj.auth){
					// 两步验证
					codeIndex = layer.open({
						type : 1,
						title : loginStr.googleAuth,
						area : [ '500px', '200px' ], // 宽高
						content : $('#codeDiv')
					});
				} else {
					login();
				}
			} else {
				layer.msg(data.msg);
			}
			
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});

}




function addAdmin() {
	if ($("#adminName").val() == '') {
		layer.msg(loginStr.error1);
		return;
	}
	if ($("#adminPass").val() == '' || $("#repeatPass").val() == '') {
		layer.msg(loginStr.error2);
		return;
	}
	if ($("#adminPass").val() != $("#repeatPass").val()) {
		layer.msg(loginStr.error3);
		return;
	}

	var pwdRegex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z]).{8,100}');
	if (!pwdRegex.test($("#adminPass").val())) {
		layer.msg(loginStr.error4);
		return;
	}

	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/login/addAdmin',
		data: $("#adminForm").serialize(),
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				location.reload();
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});
}

function getKey() {
	if (event.keyCode == 13) {
		getAuth();
	}
}


function refreshCode(id) {
	$("#" + id).attr("src", ctx + "adminPage/login/getCode?t=" + (new Date()).getTime());
}
