function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#name").val(""); 
	$("#value").val(""); 
	
	showWindow("添加基本参数配置");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '600px', '400px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	if ($("#name").val() == "") {
		layer.msg("名称为空");
		return;
	}
	if ($("#value").val() == "") {
		layer.msg("值为空");
		return;
	}
	
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/basic/addOver',
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
			alert(commonStr.errorInfo);
		}
	});
}

function edit(id) {
	$("#id").val(id); 
	
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/basic/detail',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				var http = data.obj;
				$("#id").val(http.id); 
				$("#value").val(http.value); 
				$("#name").val(http.name);
				
				form.render();
				showWindow("编辑基本参数配置");
			}else{
				layer.msg(data.msg);
			}
		},
		error : function() {
			alert(commonStr.errorInfo);
		}
	});
}

function del(id){
	if(confirm("确认删除?")){
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/basic/del',
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
				alert(commonStr.errorInfo);
			}
		});
	}
}


function setOrder(id, count){
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/basic/setOrder',
		data : {
			id : id,
			count : count
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
			alert(commonStr.errorInfo);
		}
	});
}
