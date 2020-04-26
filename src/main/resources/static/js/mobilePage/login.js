$(function(){
	var userId = localStorage.getItem("userId");
	
	if(userId != null){
		location.href = ctx + '/main?userId='+ userId;
	}else{
		$(".main").show();
	}
	
})


function login(){
	
	if($("#phone").val() == ""){
		alert("电话未填写");
		return;
	}
	if($("#pass").val() == ""){
		alert("密码未填写");
		return;
	}
	$.ajax({
		type : 'POST',
		url : ctx + '/loginOver',
		data : {
			phone : $("#phone").val(),
			pass : $("#pass").val(),
		},
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				localStorage.setItem("userId", data.obj.id);
				
				location.href = ctx + "main?userId=" + data.obj.id;
			} else {
				alert(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}