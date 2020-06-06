$(function(){
	var map = new AMap.Map('container', {
	    zoom: 15,
	    center:[104.066422,30.65577],
	}); 
	var wms  = new AMap.TileLayer.WMTS({
	     url: 'http://t0.tianditu.gov.cn/img_c/wmts?tk=26837ca6679a927b7877c0f0aa01407c',        
	     blend:false,
	     tileSize:256,
	     params:{'Layer': '0',Version:'1.0.0',Format: 'image/png'}
	})
	wms.setMap(map)
	
	
})