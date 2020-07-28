$(function() {
	form.on('switch(enable)', function(data){
		  
		  $.ajax({
				type : 'POST',
				url : ctx + '/adminPage/server/setEnable',
				data : {
					enable : data.elem.checked?1:0,
					id : data.elem.value
				},
				dataType : 'json',
				success : function(data) {			
					
				},
				error : function() {
					alert("出错了,请联系技术人员!");
				}
		});
	});   
	
	
	form.on('select(type)', function(data) {
		checkType(data.value, $(data.elem).attr("lang"));
	});
	form.on('select(ssl)', function(data) {
		checkSsl(data.value);
	});
	form.on('select(proxyType)', function(data) {
		checkProxyType(data.value);
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
	if (type == 0) {
		$("#" + id + " span[name='valueSpan']").show();
		$("#" + id + " span[name='rootPathSpan']").hide();
		$("#" + id + " span[name='upstreamSelectSpan']").hide();
		$("#" + id + " span[name='blankSpan']").hide();
		$("#" + id + " span[name='headerSpan']").show();
	} 
	if (type == 1) {
		$("#" + id + " span[name='valueSpan']").hide();
		$("#" + id + " span[name='rootPathSpan']").show();
		$("#" + id + " span[name='upstreamSelectSpan']").hide();
		$("#" + id + " span[name='blankSpan']").hide();
		$("#" + id + " span[name='headerSpan']").hide();
	}
	if (type == 2) {
		$("#" + id + " span[name='valueSpan']").hide();
		$("#" + id + " span[name='rootPathSpan']").hide();
		$("#" + id + " span[name='upstreamSelectSpan']").show();
		$("#" + id + " span[name='blankSpan']").hide();
		$("#" + id + " span[name='headerSpan']").show();
	} 
	if (type == 3) {
		$("#" + id + " span[name='valueSpan']").hide();
		$("#" + id + " span[name='rootPathSpan']").hide();
		$("#" + id + " span[name='upstreamSelectSpan']").hide();
		$("#" + id + " span[name='blankSpan']").show();
		$("#" + id + " span[name='headerSpan']").hide();
	} 
}

function checkSsl(value){
	if (value == 0) {
		$(".pemDiv").hide();
	} 
	if (value == 1) {
		$(".pemDiv").show();
	} 
}

function checkProxyType(value){
	if (value == 0) {
		$(".proxyHttp").show();
		$(".proxyTcp").hide();
		
	} 
	if (value == 1) {
		$(".proxyHttp").hide();
		$(".proxyTcp").show();
	} 
	
}

function search() {
	$("#searchForm").submit();
}

function add() {
	$("#id").val("");
	$("#listen").val("");
	$("#ip").val("");
	$("#serverName").val("");
	$("#ssl option:first").prop("selected", true);
	$("#rewrite option:first").prop("selected", true);
	$("#http2 option:first").prop("selected", true);
	$("#proxyType option:first").prop("selected", true);
	$("#proxyUpstreamId option:first").prop("selected", true);
	
	$("#pem").val("");
	$("#pemPath").html("");
	$("#key").val("");
	$("#keyPath").html("");
	$("#itemList").html("");
	$("#paramJson").val("");
	
	checkSsl(0);
	checkProxyType(0);
	
	form.render();
	showWindow("添加反向代理");
}

function showWindow(title) {
	layer.open({
		type : 1,
		title : title,
		area : [ '1210px', '700px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	if($("#listen").val().trim() == ''){
		layer.msg("端口未填写");
		return;
	}
	
	if($("#ssl").val() == 1 && $("#serverName").val() == ''){
		layer.msg("开启ssl必须填写域名");
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
	$("input[name='rootPath']").each(function(){
		if(!$(this).is(":hidden") && $(this).val().trim() == ''){
			over = false;
		}
	})
	$("select[name='upstreamId']").each(function(){
		if(!$(this).parent().is(":hidden") && ($(this).val() == '' || $(this).val() == null)){
			over = false;
		}
	})
	$("select[name='proxyUpstreamId']").each(function(){
		if($("#proxyType").val() == 1 &&  ($(this).val() == '' || $(this).val() == null)){
			over = false;
		}
	})
	if(!over){
		layer.msg("填写不完整");
		return;
	}
	
	
	var server = {};
	server.id =  $("#id").val();
	server.proxyType = $("#proxyType").val();
	server.proxyUpstreamId = $("#proxyUpstreamId").val();
	server.listen = $("#listen").val();
	if($("#ip").val() != ''){
		server.listen = $("#ip").val() + ":" + $("#listen").val();
	}
	
	server.serverName = $("#serverName").val();
	server.ssl = $("#ssl").val();
	server.pem = $("#pem").val();
	server.key = $("#key").val();
	server.rewrite = $("#rewrite").val();
	server.http2 = $("#http2").val();
	
	var serverParamJson = $("#serverParamJson").val();
	
	var locations = [];
	
	$(".itemList").children().each(function(){
		var location = {};
		location.path = $(this).find("input[name='path']").val();
		location.type = $(this).find("select[name='type']").val();
		location.value = $(this).find("input[name='value']").val();
		location.upstreamId = $(this).find("select[name='upstreamId']").val();
		location.upstreamPath = $(this).find("input[name='upstreamPath']").val();
		location.rootPath = $(this).find("input[name='rootPath']").val();
		location.rootPage = $(this).find("input[name='rootPage']").val();
		location.rootType = $(this).find("select[name='rootType']").val();
		location.locationParamJson =  $(this).find("textarea[name='locationParamJson']").val();
		location.header =  $(this).find("input[name='header']").prop("checked")?1:0;
		
		locations.push(location);
	})
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/server/addOver',
		data : {
			serverJson : JSON.stringify(server),
			serverParamJson : serverParamJson,
			locationJson : JSON.stringify(locations),
		},
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

function edit(id,clone) {
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
				if(!clone){
					$("#id").val(server.id);
				}else{
					$("#id").val("");
				}
				
				if(server.listen.indexOf(":") > -1){
					$("#ip").val(server.listen.split(":")[0]);
					$("#listen").val(server.listen.split(":")[1]);
				} else {
					$("#ip").val("");
					$("#listen").val(server.listen);
				}
				
				$("#serverName").val(server.serverName);
				$("#ssl").val(server.ssl);
				$("#pem").val(server.pem);
				$("#key").val(server.key);
				$("#pemPath").html(server.pem);
				$("#keyPath").html(server.key);
				$("#proxyType").val(server.proxyType);
				$("#proxyUpstreamId").val(server.proxyUpstreamId);
				$("#serverParamJson").val(data.obj.paramJson);
				
				if(server.rewrite != null){
					$("#rewrite").val(server.rewrite);
				} else{
					$("#rewrite option:first").prop("selected", true);
				}
				
				if(server.http2 != null){
					$("#http2").val(server.http2);
				} else{
					$("#http2 option:first").prop("selected", true);
				}
				
				checkSsl(server.ssl);
				checkProxyType(server.proxyType);
				var list = data.obj.locationList;
				
				var upstreamSelect = $("#upstreamSelect").html();
				$("#itemList").html("");
				for(let i=0;i<list.length;i++){
					var location = list[i];
					var uuid = guid();
					
					location.locationParamJson = location.locationParamJson;
					var html = buildHtml(uuid, location, upstreamSelect);
						
					$("#itemList").append(html);
					
					$("#" + uuid + " input[name='value']").val(location.value);
					$("#" + uuid + " input[name='rootType']").val(location.rootType);
					$("#" + uuid + " input[name='rootPath']").val(location.rootPath);
					$("#" + uuid + " input[name='rootPage']").val(location.rootPage);
					$("#" + uuid + " select[name='rootType']").val(location.rootType);
					$("#" + uuid + " select[name='upstreamId']").val(location.upstreamId);
					$("#" + uuid + " input[name='upstreamPath']").val(location.upstreamPath);
					
					if(location.header == 1){
						$("#" + uuid + " input[name='header']").prop("checked", true);
					}else{
						$("#" + uuid + " input[name='header']").prop("checked", false);
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
	
	var html = buildHtml(uuid, null, upstreamSelect);
	
	$("#itemList").append(html);
	checkType(0, uuid);
	form.render();
	
}



function buildHtml(uuid, location, upstreamSelect){
	if(location == null){
		location = {
			path : "/",
			type : "0",
			locationParamJson : ""
		};
	}
	
	
	var str = `<tr id='${uuid}'>
				<td>
					<input type="text" name="path" class="layui-input short" value="${location.path}">
				</td>
				<td>
					<div class="layui-input-inline" style="width: 130px;">
						<select name="type" lang='${uuid}' lay-filter="type">
							<option ${location.type=='0'?'selected':''} value="0">代理动态http</option>
							<option ${location.type=='1'?'selected':''} value="1">代理静态html</option>
							<option ${location.type=='2'?'selected':''} value="2">负载均衡</option>
							<option ${location.type=='3'?'selected':''} value="3">空白location</option>
						</select>
					</div>
				</td>
				
				<td>
					<span name="valueSpan">
						<div class="layui-inline">
							<input type="text"  style="width: 315px;" name="value" id="value_${uuid}" class="layui-input long" value=""  placeholder="例：http://127.0.0.1:8080">
						</div>
					</span>
					
					<span name="rootPathSpan">
						<div class="layui-inline" style="width: 100px;">
							<select name="rootType" >
								<option value="root">root模式</option>
								<option value="alias">alias模式</option>
							</select>
						</div>
						
						<div class="layui-inline" style="width: 150px;">
							<input type="text" name="rootPath" id="rootPath_${uuid}" class="layui-input" placeholder="例：/root/www">
						</div>
							
						<i class="layui-icon layui-icon-export" lang="value" onclick="selectWww('${uuid}')"></i> 
							
						<div class="layui-inline" style="width: 150px;">
							<input type="text" name="rootPage" id="rootPage_${uuid}" class="layui-input" placeholder="默认页如 index.html">
						</div>	
					</span>
					
					<span name="upstreamSelectSpan">
						${upstreamSelect}
					</span>
					
					<span name="blankSpan">
					
					</span>
					
					<span  name="headerSpan">
						<div class="layui-inline">
							<input type="checkbox" name="header" title="header添加Host参数" lay-skin="primary" checked> 
						</div>
					</span>
				</td> 
				<td>
					<textarea style="display: none;" id="locationParamJson_${uuid}" name="locationParamJson" >${location.locationParamJson}</textarea>
					<button type="button" class="layui-btn layui-btn-sm" onclick="locationParam('${uuid}')">设置额外参数</button>
					<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button>
				</td>
			</tr>`
		
	return str;
}


function delTr(id){
	$("#" + id).remove();
}

var certIndex;
function selectCert(){
	certIndex = layer.open({
		type : 1,
		title : "选择内置证书",
		area : [ '500px', '300px' ], // 宽高
		content : $('#certDiv')
	});
	
}

function selectCertOver(){
	var id = $("#certId").val();
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/cert/detail',
		data : {
			id : id
		},
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				var cert = data.obj;
				$("#pem").val(cert.pem);
				$("#pemPath").html(cert.pem);
				$("#key").val(cert.key);
				$("#keyPath").html(cert.key);
				
				layer.close(certIndex);
			} else {
				layer.msg(data.msg)
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});
}



function selectPem(){
	rootSelect.selectOne(function(rs){
		$("#pem").val(rs);
		$("#pemPath").html(rs);
	})
}


function selectKey(){
	rootSelect.selectOne(function(rs){
		$("#key").val(rs);
		$("#keyPath").html(rs);
	})
}


function serverParam(){
	var json = $("#serverParamJson").val();
	$("#targertId").val("serverParamJson");
	var params = json!=''?JSON.parse(json):[];
	fillTable(params);
	
}

function locationParam(uuid){
	var json = $("#locationParamJson_" + uuid).val();
	$("#targertId").val("locationParamJson_" + uuid);
	var params = json!=''?JSON.parse(json):[];
	fillTable(params);
}

var paramIndex;
function fillTable(params){
	var html = "";
	for(var i=0;i<params.length;i++){
		var param = params[i];
		
		var uuid = guid();
		if(param.templateValue == null){
			html += `
			<tr name="param" id=${uuid}>
				<td>
					<textarea  name="name" class="layui-textarea">${param.name}</textarea>
				</td>
				<td  style="width: 60%;">
					<textarea  name="value" class="layui-textarea">${param.value}</textarea>
				</td>
				<td>
					<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button>
				</td>
			</tr>
			`;
		} else {
			html +=  buildTemplateParam(param);
		}
	}
	
	$("#paramList").html(html);
	
	paramIndex = layer.open({
		type : 1,
		title : "设置额外参数",
		area : [ '800px', '600px' ], // 宽高
		content : $('#paramJsonDiv')
	});
}

function addParam(){
	var uuid = guid();
	
	var html = `
	<tr name="param" id="${uuid}">
		<td>
			<textarea  name="name" class="layui-textarea"></textarea>
		</td>
		<td  style="width: 60%;">
			<textarea  name="value" class="layui-textarea"></textarea>
		</td>
		<td>
			<button type="button" class="layui-btn layui-btn-sm layui-btn-danger" onclick="delTr('${uuid}')">删除</button>
		</td>
	</tr>
	`;
	
	$("#paramList").append(html);
	
}


function addParamOver(){
	
	var targertId = $("#targertId").val();
	var params = [];
	$("tr[name='param']").each(function(){
		var param = {};
		if ($(this).find("input[name='templateValue']").val() == null) {
			param.name = $(this).find("textarea[name='name']").val();
			param.value = $(this).find("textarea[name='value']").val();
		} else {
			param.templateValue = $(this).find("input[name='templateValue']").val();
			param.templateName = $(this).find("input[name='templateName']").val();
		}
		params.push(param);
	})
	$("#" + targertId).val(JSON.stringify(params));
	
	layer.close(paramIndex);
}


function sort(id){
	$("#sort").val(id.replace("Sort",""))
	if($("#"+id).attr("class").indexOf("blue") > -1){
		if($("#direction").val()=='asc'){
			$("#direction").val("desc")
		}else{
			$("#direction").val("asc")
		}
	}else{
		$("#direction").val("asc")
	}
	
	search();
}


var wwwIndex;
var uuid;
function selectWww(id){
	uuid = id;
	rootSelect.selectOne(function callBack(val){
		$("#rootPath_" + uuid).val(val);
	});
}


function clone(id){
	if(confirm("确认进行克隆?")){
		edit(id, true);
	}
}


function importServer() {
	var formData = new FormData();
	formData.append("nginxPath", $("#nginxPath").val());
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/server/importServer',
		data : formData,
		dataType : 'json',
		processData: false,
		contentType: false,
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

var importIndex;
function openImport() {
	importIndex = layer.open({
		type : 1,
		title : "导入conf",
		area : [ '500px', '300px' ], // 宽高
		content : $('#importDiv')
	});
}
// 选择系统文件
function selectRootCustom(inputId){
	rootSelect.selectOne(function callBack(val){
		$("#" + inputId).val(val);
	});
}

function testPort(){
	if(confirm("是否测试全部监听的端口?")){
		layer.load();
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/server/testPort',
			dataType : 'json',
			processData: false,
			contentType: false,
			success : function(data) {
				layer.closeAll();
				if (data.success) {
					layer.msg("没有端口被占用");
				} else {
					layer.alert(data.msg);
				}
			},
			error : function() {
				layer.closeAll();
				alert("出错了,请联系技术人员!");
			}
		});
	}
	
}