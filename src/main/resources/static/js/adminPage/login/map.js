$(function() {
//	var imageLayer = new AMap.ImageLayer({
//		url : ctx + '/img/mp.png',
//		bounds : new AMap.Bounds([ 120.081730, 35.908096 ], [ 120.083030, 35.909396 ]),
//		zooms : [ 3, 20 ],
//		zIndex : 3,
//	});

	var map = new AMap.Map('container', {
		zoom : 20,
		zooms : [ 3, 20 ],
		center : [ 120.081730, 35.908096 ],
		viewMode : '3D',
		pitch : 0,
		expandZoomRange : true,
//		layers : [ new AMap.TileLayer(), imageLayer ]
	});

	var googleLayer = new AMap.TileLayer({
		zIndex : 2,
		getTileUrl : function(x, y, z) {
			return 'http://mt1.google.cn/vt/lyrs=s@142&hl=zh-CN&gl=cn&x=' + x + '&y=' + y + '&z=' + z + '&s=Galil';
		}
	});

	googleLayer.setMap(map);

	var mouseTool = new AMap.MouseTool(map); // 在地图中添加MouseTool插件
	var drawRectangle = mouseTool.rectangle(); // 用鼠标工具画矩形
	AMap.event.addListener(mouseTool, 'draw', function(e) { // 添加事件
		console.log(e.obj.getPath());// 获取路径
		alert(e.obj.getPath());
	});
})