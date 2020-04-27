$(function() {
	form.on('select(ssl)', function(data) {
		check(data.value);
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

function check(value){
	if (value == 0) {
		$("#pemDiv").hide();
		$("#targetDiv").show();
		$("#root").hide();
	} 
	if (value == 1) {
		$("#pemDiv").show();
		$("#targetDiv").show();
		$("#root").hide();
	}
	if (value == 2) {
		$("#pemDiv").hide();
		$("#targetDiv").hide();
		$("#root").show();
	}
}

function search() {
	$("#searchForm").submit();
}

function add() {
	$("#id").val("");
	$("#listen").val("");
	$("#serverName").val("");
	$("#ssl option:first").prop("selected", true);
	$("#pem").val("");
	$("#pemPath").html("");
	$("#key").val("");
	$("#keyPath").html("");
	$("#proxyPass").val("");
	$("#proxyPassPort").val("");
	$("#root").val("");

	showWindow("添加server");
}

function showWindow(title) {
	layer.open({
		type : 1,
		title : title,
		area : [ '700px', '600px' ], // 宽高
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
				$("#ssl").val(server.ssl);
				$("#pem").val(server.pem);
				$("#key").val(server.key);
				$("#pemPath").html(server.pem);
				$("#keyPath").html(server.key);
				$("#proxyPass").val(server.proxyPass);
				$("#proxyPassPort").val(server.proxyPassPort);
				$("#root").val(server.root);
				
				check(server.ssl);
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
