function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#ip").val(""); 
	$("#port").val(""); 
	$("#protocol").val("http"); 
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

function contentLocal(){
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/remote/readContent',
		success : function(data) {
			if (data) {
				$("#content").val(data);
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

function content(id) {
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/remote/content',
		dataType : 'json',
		data : {
			id : id
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

var load;
function addOver() {
	if($("#ip").val().trim() == '' || $("#port").val().trim() == '' || $("#name").val().trim() == '' || $("#pass").val().trim() == ''){
		layer.msg("未填写完成");
		return;
	}
	
	load = layer.load();
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/remote/addOver',
		data : $('#addForm').serialize(),
		dataType : 'json',
		success : function(data) {
			layer.close(load);
			if (data.success) {
				location.reload();
			} else {
				
				layer.msg(data.msg);
			}
		},
		error : function() {
			layer.close(load);
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
				$("#protocol").val(remote.protocol); 
				
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

function asyc(id){
	if(confirm("是否同步此服务器所有数据到其他服务器？注意同步只能在相同操作系统之间同步.")){
		layer.load();
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/remote/asyc',
			data : {
				id : id
			},
			dataType : 'json',
			success : function(data) {
				layer.closeAll();
				if (data.success) {
					layer.msg("同步成功")
				}else{
					layer.msg(data.msg)
				}
			},
			error : function() {
				layer.closeAll();
				alert("出错了,请联系技术人员!");
			}
		});
	}
}