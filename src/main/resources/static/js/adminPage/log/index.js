
function content(path) {
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/log/content',
		dataType : 'json',
		data : {
			path : path
		},
		success : function(data) {
			if (data.success) {
				
				layer.msg("总数据量:" + data.obj);
			}else{
				layer.msg(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}

function del(path){
	if(confirm("确认删除?")){
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/log/del',
			data : {
				path : path
			},
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					location.reload();
				}else{
					layer.msg(data.msg)
				}
			},
			error : function() {
				alert("出错了,请联系技术人员!");
			}
		});
	}
}


