$(function() {

	layer.open({
		type : 1,
		shade : false,
		title : "mongoHelper脚手架",
		closeBtn : false,
		area : [ '400px', '330px' ], //宽高
		content : $('#windowDiv')
	});

})

function login() {
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/login/login',
		data : $("#loginForm").serialize(),
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				location.href = ctx + "adminPage/admin/goMenu";
			} else {
				alert(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
	
}

function getKey() {
	if (event.keyCode == 13) {
		login();
	}
}