$(function() {
	var map = new AMap.Map('container', {
		zoom : 10,
		center : [ 104.066422, 30.65577 ],
	});
	var wms = new AMap.TileLayer.WMTS({
		url : 'http://t0.tianditu.gov.cn/img_w/wmts',
		blend : false,
		tileSize : 256,
		params : {
			LAYER : 'img',
			VERSION : '1.0.0',
			Format : 'tiles',
			TileMatrixSet : 'w',
			STYLE : 'default',
			tk : '26837ca6679a927b7877c0f0aa01407c'
		}
	})
	wms.setMap(map)

	
//	var wms = new AMap.TileLayer.WMTS({
//		url : 'https://www.google.cn/maps/vt',
//		blend : false,
//		tileSize : 256,
//		params : {
//			lyrs : 's',
//			gl : 'cn',
//			Layer : 'img',
//			Version : '1.0.0',
//			Format : 'tiles'
//		}
//	})
//	wms.setMap(map)

	
})