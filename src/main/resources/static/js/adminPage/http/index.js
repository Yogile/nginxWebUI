

function edit(name,value) {
	$("#name").val(name); 
	$("#nameDiv").html(name); 
	$("#value").val(value); 
	
	showWindow("编辑项");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '400px', '400px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/http/addOver',
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

