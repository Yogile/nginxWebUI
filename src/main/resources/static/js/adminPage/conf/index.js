function replace(){
	if($("#nginxPath").val() == ''){
		alert("nginx配置文件路径为空");
		return;
	}
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/conf/replace',
		data : {
			nginxPath : $("#nginxPath").val()
		},
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				layer.msg(data.obj);
			} else {
				layer.alert(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}


function check(){
	if($("#nginxPath").val() == ''){
		alert("nginx配置文件路径为空");
		return;
	}

	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/conf/check',
		data : {
			nginxPath : $("#nginxPath").val()
		},
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				layer.msg(data.obj);
			} else {
				layer.alert(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}


function reboot(){
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/conf/reboot',
		data : {
			
		},
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				layer.msg(data.obj);
			} else {
				layer.alert(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
	
}