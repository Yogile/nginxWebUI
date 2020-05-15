
function add() {
	$("#id").val(""); 
	$("#domain").val(""); 
	
	showWindow("添加证书");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '400px', '300px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {

	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/cert/addOver',
		data : $('#addForm').serialize(),
		dataType : 'json',
		success : function(data) {
		
			if (data.success) {
				location.reload();
			} else {
				layer.msg(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}


function del(id){
	if(confirm("确认删除?")){
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/cert/del',
			data : {
				id : id
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


function renew(id){
	
	if(confirm("确认申请或续签?过程中nginx将被短暂关闭.")){
		layer.load();
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/cert/renew',
			data : {
				id : id
			},
			dataType : 'json',
			success : function(data) {
				layer.closeAll();
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