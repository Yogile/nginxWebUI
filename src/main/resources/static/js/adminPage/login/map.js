$(function() {
	// var map = new AMap.Map('container', {
	// zoom : 10,
	// center : [ 104.066422, 30.65577 ],
	// });
	// var wms = new AMap.TileLayer.WMTS({
	// url : 'http://t0.tianditu.gov.cn/img_w/wmts',
	// blend : false,
	// tileSize : 256,
	// params : {
	// LAYER : 'img',
	// VERSION : '1.0.0',
	// Format : 'tiles',
	// TileMatrixSet : 'w',
	// STYLE : 'default',
	// tk : '26837ca6679a927b7877c0f0aa01407c'
	// }
	// })
	// wms.setMap(map)

	var map = new AMap.Map('container', {
		zoom : 40,
		center : [ 120.148697,35.943452 ],
		viewMode : '3D',
		pitch : 40,
		expandZoomRange :true
	});

	var googleLayer = new AMap.TileLayer({
		zIndex : 2,
		getTileUrl : function(x, y, z) {
			return 'http://mt1.google.cn/vt/lyrs=s@142&hl=zh-CN&gl=cn&x=' + x + '&y=' + y + '&z=' + z + '&s=Galil';
		}
	});

	googleLayer.setMap(map);

	// 创建Object3DLayer图层
	var object3Dlayer = new AMap.Object3DLayer();
	map.add(object3Dlayer);

	map.plugin([ "AMap.GltfLoader" ], function() {
		var urlCity = 'https://a.amap.com/jsapi_demos/static/gltf-online/shanghai/scene.gltf';
		var paramCity = {
			position : new AMap.LngLat(120.148697,35.943452), // 必须
			scale : 3580, // 非必须，默认1
			height : 1800, // 非必须，默认0
			scene : 0, // 非必须，默认0
		};


		var gltfObj = new AMap.GltfLoader();

		gltfObj.load(urlCity, function(gltfCity) {
			gltfCity.setOption(paramCity);
			gltfCity.rotateX(90);
			gltfCity.rotateZ(120);
			object3Dlayer.add(gltfCity);
		});


	});

})