function search() {
	$("input[name='curr']").val(1);
	$("#searchForm").submit();
	
}

function add() {
	$("#id").val(""); 
	$("#name").val(""); 
	
	showWindow("添加部门");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '400px', '400px' ], // 宽高
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
		url : ctx + '/adminPage/department/addOver',
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

function edit(id,name) {
	$("#id").val(id); 
	$("#name").val(admin.name); 
				
	form.render();
	showWindow("编辑用户");	
}

function del(id){
	if(confirm("确认删除?")){
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/department/del',
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
