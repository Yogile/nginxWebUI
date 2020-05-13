$(function() {
	form.on('select(type)', function(data) {
		checkType(data.value, $(data.elem).attr("lang"));
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

function checkType(type,id){
	if (type == 0 || type == 1) {
		$("#" + id + " input[name='value']").show();
		$("#" + id + " span[name='upstreamSelect']").hide();
	} 
	if (type == 2) {
		$("#" + id + " input[name='value']").hide();
		$("#" + id + " span[name='upstreamSelect']").show();
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
	$("#ssl option:first").prop("selected", true);
	$("#rewrite option:first").prop("selected", true);
	
	$("#pem").val("");
	$("#pemPath").html("");
	$("#key").val("");
	$("#keyPath").html("");
	$("#itemList").html("");
	
	checkSsl(0);
	
	form.render();
	showWindow("添加反向代理");
}

function showWindow(title) {
	layer.open({
		type : 1,
		title : title,
		area : [ '1200px', '700px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	if($("#listen").val().trim() == ''){
		layer.msg("端口未填写");
		return;
	}
	
	var over = true;
	$("input[name='path']").each(function(){
		if($(this).val().trim() == ''){
			over = false;
		}
	})
	$("input[name='value']").each(function(){
		if(!$(this).is(":hidden") && $(this).val().trim() == ''){
			over = false;
		}
	})
	if(!over){
		layer.msg("填写不完整");
		return;
	}
	
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
				var server = data.obj.server;
				$("#id").val(server.id);
				$("#listen").val(server.listen);
				$("#serverName").val(server.serverName);
				$("#ssl").val(server.ssl);
				$("#pem").val(server.pem);
				$("#key").val(server.key);
				$("#pemPath").html(server.pem);
				$("#keyPath").html(server.key);
				
				if(server.rewrite != null){
					$("#rewrite").val(server.rewrite);
				} else{
					$("#rewrite option:first").prop("selected", true);
				}
				
				checkSsl(server.ssl);
				
				var list = data.obj.locationList;
				
				var upstreamSelect = $("#upstreamSelect").html();
				$("#itemList").html("");
				for(let i=0;i<list.length;i++){
					var location = list[i];
					var uuid = guid();
					var html = `<tr id='${uuid}'>
								<td>
									<input type="text" name="path" class="layui-input" value="${location.path}">
								</td>
								<td>
									<select name="type" lang='${uuid}' lay-filter="type">
										<option ${location.type=='0'?'selected':''} value="0">代理动态http</option>
										<option ${location.type=='1'?'selected':''} value="1">代理静态html</option>
										<option ${location.type=='2'?'selected':''} value="2">负债均衡</option>
									</select>
								</td>
								
								<td>
									<input type="text" name="value" class="layui-input" value=""  placeholder="例：http://127.0.0.1:8080 或 /root/www">
									<span name="upstreamSelect">
									${upstreamSelect}
									</span>
								</td> 
								<td><button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button></td>
						</tr>`
						
					$("#itemList").append(html);
					
					if(location.type == 0 || location.type == 1){
						$("#" + uuid + " input[name='value']").val(location.value);
					} else {
						$("#" + uuid + " select[name='upstreamId']").val(location.upstreamId);
					}
					
					checkType(location.type, uuid)
				}
				
				form.render();
				showWindow("编辑反向代理");
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



function addItem(){
	var uuid = guid();
	
	var upstreamSelect = $("#upstreamSelect").html();
	
	var html = `<tr id='${uuid}'>
						<td>
							<input type="text" name="path" class="layui-input" value="/">
						</td>
						<td>
							<select name="type" lang='${uuid}' lay-filter="type">
								<option value="0">代理动态http</option>
								<option value="1">代理静态html</option>
								<option value="2">负债均衡</option>
							</select>
						</td>
						
						<td>
							<input type="text" name="value" class="layui-input" value="" placeholder="例：http://127.0.0.1:8080 或 /root/www">
							<span name="upstreamSelect">
								${upstreamSelect}
							</span>
						</td> 
						<td><button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button></td>
				</tr>`
	$("#itemList").append(html);
	checkType(0, uuid);
	form.render();
	
}


function delTr(id){
	$("#" + id).remove();
}