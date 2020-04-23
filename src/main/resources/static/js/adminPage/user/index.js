$(function(){
	
})


function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#name").val("");
	$("#phone").val(""); 
	$("#idcard").val("");
	$("#departmentId option:first").prop("checked",true);
	
	
	form.render();
	showWindow("添加用户");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '450px', '550px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	if ($("#name").val() == "") {
		layer.msg("姓名为空");
		return;
	}

	if ($("#phone").val() == "") {
		layer.msg("电话为空");
		return;
	}
	if ($("#idcard").val() == "") {
		layer.msg("身份证为空");
		return;
	}
	
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/user/addOver',
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

function edit(id) {
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/user/detail',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				var ext = data.obj;
				$("#id").val(ext.user.id); 
				$("#name").val(ext.user.name); 
				$("#phone").val(ext.user.phone); 
				$("#idcard").val(ext.user.idcard); 
				$("#departmentId").val(ext.user.departmentId);
				
				form.render();
				showWindow("编辑用户");
			}else{
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
			url : ctx + '/adminPage/user/del',
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
