$(function() {
	var imageLayer = new AMap.ImageLayer({
		url : ctx + '/img/mp.png',
		bounds : new AMap.Bounds([120.080674,35.907112], [120.082362,35.908948]),
		zooms : [ 3, 20 ],
		zIndex : 3,
	});

	var map = new AMap.Map('container', {
		zoom : 20,
		zooms : [ 3, 20 ],
		center : [ 120.080674,35.908948],
		viewMode : '2D',
		pitch : 0,
		expandZoomRange : true,
		layers : [ new AMap.TileLayer(), imageLayer ]
	});

	var googleLayer = new AMap.TileLayer({
		zIndex : 2,
		getTileUrl : function(x, y, z) {
			return 'http://mt1.google.cn/vt/lyrs=s@142&hl=zh-CN&gl=cn&x=' + x + '&y=' + y + '&z=' + z + '&s=Galil';
		}
	});

	googleLayer.setMap(map);

	
	var path = [
	    new AMap.LngLat(120.082362,35.907112),
	    new AMap.LngLat(120.080674,35.907112),
	    new AMap.LngLat(120.080674,35.908948),
	    new AMap.LngLat(120.082362,35.908948)
	];

	var polygon = new AMap.Polygon({
	    path: path,  
	    fillColor: '#fff', // 多边形填充颜色
	    borderWeight: 2, // 线条宽度，默认为 1
	    strokeColor: 'red', // 线条颜色
	});

//	map.add(polygon);
	
	
//	var mouseTool = new AMap.MouseTool(map); // 在地图中添加MouseTool插件
//	var drawRectangle = mouseTool.rectangle(); // 用鼠标工具画矩形
//	AMap.event.addListener(mouseTool, 'draw', function(e) { // 添加事件
//		console.log(e.obj.getPath());// 获取路径
//		//layer.msg(e.obj.getPath());
//		$("#text").html(JSON.stringify( e.obj.getPath()));
//	});
})