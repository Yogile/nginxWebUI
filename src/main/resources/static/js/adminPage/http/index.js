function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#name").val(""); 
	$("#value").val(""); 
	
	showWindow("添加http转发配置");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '400px', '300px' ], // 宽高
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

function edit(id) {
	$("#id").val(id); 
	
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/http/detail',
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
				showWindow("编辑http转发配置");
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
			url : ctx + '/adminPage/http/del',
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

function guide(){
	
	layer.open({
		type : 1,
		title : "简易配置向导",
		area : [ '800px', '600px' ], // 宽高
		content : $('#guideDiv')
	});
	
}

function addGiudeOver(){
	
	var params = [];
	$("input[name='param']").each(function(){
		
		var http = {};
		http.name = $(this).attr("id");
		http.value = $(this).val();
		
		params.push(http);
	})
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/http/addGiudeOver',
		data : {
			json : JSON.stringify(params)
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