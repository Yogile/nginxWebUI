function search() {
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#listen").val(""); 
	$("#serverName").val(""); 
	$("#ssl option:first").prop("selected", true);
	$("#pem").val(""); 
	$("#key").val(""); 
	$("#proxyPass").val(""); 
	$("#proxyPassPort").val(""); 
	
	
	showWindow("添加server");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '400px', '700px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/server/addOver',
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
		url : ctx + '/adminPage/server/detail',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				var server = data.obj;
				$("#id").val(server.id); 
				$("#listen").val(server.listen); 
				$("#serverName").val(server.serverName); 
				$("#ssl").val(server.ssl + "");
				$("#pem").val(server.pem); 
				$("#key").val(server.key); 
				$("#proxyPass").val(server.proxyPass); 
				$("#proxyPassPort").val(server.proxyPassPort);
				
				form.render();
				showWindow("编辑server");
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
			url : ctx + '/adminPage/server/del',
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
