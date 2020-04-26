$(function(){
	
	
})

function loginOut(){
	if(confirm("是否退出登录?")){
		localStorage.removeItem("userId");
		
		location.href = ctx + '/login';
	}
}


let mediaStreamTrack=null; // 视频对象(全局)
let video ;
function openMedia() {
//	$("#file").click();
	
	if(!isIos()){
		layer.open({
			type : 1,
			title : "请拍摄证件照",
			area : [ '370px', '500px' ], // 宽高
			content : $('#windowDiv')
		});
		
	    let constraints = {
	        video: { 
	        	width: 350, 
	        	height: 350,
	        	facingMode : 'user' //"user"前置, "environment"后置
	        },
	        audio: false
	    };
	    // 获得video摄像头
	    video = document.getElementById('video');     
	    let promise = navigator.mediaDevices.getUserMedia(constraints);
	    promise.then((mediaStream) => {
	       mediaStreamTrack=mediaStream.getVideoTracks()
	        video.srcObject = mediaStream;
	        video.play();
	    });
	} else {
		$("#file").click();
	}
	
}

// 拍照
var loadIndex;
function takePhoto() {
	loadIndex = layer.load();
    // 获得Canvas对象
    let video = document.getElementById('video');
    let canvas = document.getElementById('canvas');
    let context = canvas.getContext('2d');
    context.drawImage(video, 0, 0, 350, 350);

    // toDataURL --- 可传入'image/png'---默认, 'image/jpeg'
    let img = document.getElementById('canvas').toDataURL();
    // 这里的img就是得到的图片
    console.log('img-----', img);
    // 上传
	$.ajax({
		type : 'POST',
		url : ctx + '/uploadBase64',
		data : {
			base64 : img,
		},
		dataType : 'json',
		success : function(data) {
			layer.close(loadIndex);
			closeMedia();
			if (data.success) {
				location.reload();
			} else {
				alert(data.msg);
			}
		},
		error : function() {
			alert("出错了,请联系技术人员!");
		}
	});

}

// 关闭摄像头
function closeMedia() {
      let stream = document.getElementById('video').srcObject;
      let tracks = stream.getTracks();

      tracks.forEach(function(track) {
        track.stop();
      });

     document.getElementById('video').srcObject = null;
}

function subAvatar(){
	$("#fileForm").submit();
}

function userInfo(){
	gohref(ctx + "/user");
}



function isIos(){
	var u = navigator.userAgent, app = navigator.appVersion;
	var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	
	return isiOS;
}

function goKpi(){
	if($("#name").html() == '陈钇蒙'){
		gohref(ctx + '/kpi/myKpi');
	}
	
}