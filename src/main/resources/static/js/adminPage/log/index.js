var pvuv, statusDiv, browser, httpReferer;

$(function() {
	pvuv = echarts.init(document.getElementById('pvuv'));
	statusDiv = echarts.init(document.getElementById('statusDiv'));
	browser = echarts.init(document.getElementById('browser'));
	httpReferer = echarts.init(document.getElementById('httpReferer'));

})

function content(path) {
	layer.load();
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/log/content',
		dataType : 'json',
		data : {
			path : path
		},
		success : function(data) {
			layer.closeAll();
			if (data.success) {
				showContent(data.obj)
			} else {
				layer.msg(data.msg);
			}
		},
		error : function() {
			layer.closeAll();
			alert("出错了,请联系技术人员!");
		}
	});
}

function showContent(dataGroup) {
	// 请求状态占比
	var option = {
		title : {
			text : '请求状态占比',
			left : 'center'
		},
		series : [ {
			type : 'pie',
			radius : '55%',
			data : dataGroup.status,
			label : {
				formatter : '{b}状态 : {c} ({d}%)'
			}
		} ]
	};

	statusDiv.setOption(option);
	// 系统占比
	option = {
		title : {
			text : '系统占比',
			left : 'center'
		},
		series : [ {
			type : 'pie',
			radius : '55%',
			data : dataGroup.browser,
			label : {
				formatter : '{b} : {c} ({d}%)'
			},
		} ]
	};

	browser.setOption(option);
	
	// pv uv统计
	option = {
		title : {
			text : '访问统计',
			left : 'center'
		},tooltip: {
	        trigger: 'item'
	    },
		xAxis : {
			type : 'value'
		},
		yAxis : {
			type : 'category',
			data : ['uv','pv']
		},
		series : [ {
			data : [dataGroup.uv,dataGroup.pv],
			type : 'bar',
			showBackground : true,
			backgroundStyle : {
				color : 'rgba(220, 220, 220, 0.8)'
			}
		} ]
	};

	pvuv.setOption(option);
	

	// 域名统计
	var names = [];
	var values = [];
	for (var i = 0; i < dataGroup.httpReferer.length; i++) {
		names.push(dataGroup.httpReferer[i].name);
		values.push(dataGroup.httpReferer[i].value);
	}

	option = {
		title : {
			text : '域名统计',
			left : 'center'
		},
		tooltip: {
	        trigger: 'item'
	    },
		xAxis : {
			type : 'value'
		},
		yAxis : {
			type : 'category',
			data : names
		},
		grid: { // 控制图的大小，调整下面这些值就可以，
			x: 100// x的值可以空值y轴与label标签的距离，效果如下图：
		},
		series : [ {
			data : values,
			type : 'bar',
			showBackground : true,
			backgroundStyle : {
				color : 'rgba(220, 220, 220, 0.8)'
			}
		} ]
	};

	httpReferer.setOption(option);

	// 弹出框
	layer.open({
		type : 1,
		title : "统计",
		area : [ '1200px', '800px' ], // 宽高
		content : $('#windowDiv')
	});
}

function del(path) {
	if (confirm("确认删除?")) {
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/log/del',
			data : {
				path : path
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
