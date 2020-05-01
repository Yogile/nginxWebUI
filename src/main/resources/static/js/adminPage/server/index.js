$(function() {
	form.on('select(type)', function(data) {
		checkType(data.value);
	});
	form.on('select(ssl)', function(data) {
		checkSsl(data.value);
	});
	
	layui.use('upload', function() {
		var upload = layui.upload;
		upload.render({
			elem : '#pemBtn',
			url : '/upload/',
			accept : 'file',
			done : function(res) {
				// 上传完毕回调
				if (res.success) {
					$("#pem").val(res.obj);
					$("#pemPath").html(res.obj);
				}

			},
			error : function() {
				// 请求异常回调
			}
		});

		upload.render({
			elem : '#keyBtn',
			url : '/upload/',
			accept : 'file',
			done : function(res) {
				// 上传完毕回调
				if (res.success) {
					$("#key").val(res.obj);
					$("#keyPath").html(res.obj);
				}
			},
			error : function() {
				// 请求异常回调
			}
		});
	});
})

function checkType(value){
	if (value == 0) {
		$("#targetDiv").show();
		$("#rootDiv").hide();
	} 
	if (value == 1) {
		$("#targetDiv").hide();
		$("#rootDiv").show();
	} 
}


function checkSsl(value){
	if (value == 0) {
		$("#pemDiv").hide();
	} 
	if (value == 1) {
		$("#pemDiv").show();
	} 
}
function search() {
	$("#searchForm").submit();
}

function add() {
	$("#id").val("");
	$("#listen").val("");
	$("#serverName").val("");
	$("#type option:first").prop("selected", true);
	$("#ssl option:first").prop("selected", true);
	$("#pem").val("");
	$("#pemPath").html("");
	$("#key").val("");
	$("#keyPath").html("");
	$("#proxyPass").val("");
	$("#root").val("");
	$("#rewrite option:first").prop("selected", true);
	showWindow("添加server");
}

function showWindow(title) {
	layer.open({
		type : 1,
		title : title,
		area : [ '700px', '650px' ], // 宽高
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
				$("#type").val(server.type);
				$("#ssl").val(server.ssl);
				$("#pem").val(server.pem);
				$("#key").val(server.key);
				$("#pemPath").html(server.pem);
				$("#keyPath").html(server.key);
				$("#proxyPass").val(server.proxyPass);
				$("#root").val(server.root);
				$("#rewrite").val(server.rewrite);
				
				checkType(server.type);
				checkSsl(server.ssl);
				
				form.render();
				showWindow("编辑server");
			} else {
				layer.msg(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}

function del(id) {
	if (confirm("确认删除?")) {
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
				} else {
					layer.msg(data.msg)
				}
			},
			error : function() {
				alert("出错了,请联系技术人员!");
			}
		});
	}
}
