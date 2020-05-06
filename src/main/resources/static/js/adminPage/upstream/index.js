function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
	
}

function add() {
	$("#id").val(""); 
	$("#itemList").html("");
	form.render();
	showWindow("添加uptream");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '800px', '600px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	if ($("#name").val() == "") {
		layer.msg("名称为空");
		return;
	}
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/upstream/addOver',
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
		url : ctx + '/adminPage/upstream/detail',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				var ext = data.obj;
				var list = ext.upstreamServerList;
				
				$("#id").val(ext.upstream.id);
				$("#name").val(ext.upstream.name);
				
				var html = ``;
				for(let i=0;i<list.length;i++){
					var upstream = list[i];
					
					var uuid = guid();
					html += `<tr id='${uuid}'>
									<td><input type="text" name="server" class="layui-input" value="${upstream.server}"></td>
									<td><input type="text" name="port" class="layui-input" value="${upstream.port}"></td>
									<td>
										<input type="text" name="weight" class="layui-input" value="${upstream.weight}">
									</td>
									<td><button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button></td>
							</tr>`
				}
				$("#itemList").html(html);
				
				form.render();
				showWindow("编辑upstream");
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
			url : ctx + '/adminPage/upstream/del',
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

function addItem(){
	var uuid = guid();
	var html = `<tr id='${uuid}'>
						<td><input type="text" name="server" class="layui-input" value=""></td>
						<td><input type="text" name="port" class="layui-input" value=""></td>
						<td>
							<input type="text" name="weight" class="layui-input" value="">
						</td>
						<td><button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button></td>
				</tr>`
	$("#itemList").append(html);
	
	form.render();
}


function delTr(id){
	$("#" + id).remove();
}