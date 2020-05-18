function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#ip").val(""); 
	$("#port").val(""); 
	
	$("#name").val(""); 
	$("#pass").val(""); 
	
	showWindow("添加远程服务器");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '400px', '500px' ], // 宽高
		content : $('#windowDiv')
	});
}



function content(path) {
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/remote/content',
		dataType : 'json',
		data : {
			path : path
		},
		success : function(data) {
			if (data.success) {
				$("#content").val(data.obj);
				$("#content").setTextareaCount();
				
				form.render();
				
				layer.open({
					type : 1,
					title : "内容",
					area : [ '1200px', '745px' ], // 宽高
					content : $('#contentDiv')
				});
			}else{
				layer.msg(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}

function addOver() {
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/remote/addOver',
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
	$("#id").val(id); 
	
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/remote/detail',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				var remote = data.obj;
				$("#id").val(remote.id); 
				$("#pass").val(remote.pass); 
				$("#name").val(remote.name);
				$("#ip").val(remote.ip); 
				$("#port").val(remote.port); 
				
				form.render();
				showWindow("编辑远程服务器");
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
			url : ctx + '/adminPage/remote/del',
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



function change(id){
	if(confirm("确认切换到此服务器?")){
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/remote/change',
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
