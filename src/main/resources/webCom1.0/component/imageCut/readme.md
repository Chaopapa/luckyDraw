//图片剪裁
System.ready(function(){
	$(document).on('click','.imgCutBox',function(){
		var _this = $(this);
		var id = _this.attr('id');
		System.openWindow({
			id:'图片剪裁',
			title:'图片剪裁',
			url:'${base}/resources/webCom1.0/component/imageCut/html/image.html?v=4&id='+id+'&aspectRatio=1&set_ImageSize=40',
			width:'600px',
			height:'600px',
			maxmin:false
		});
	})
	
	//图片删除
	$(document).on('click','.imgCutBox .glyphicon',function(e){
		System.stopBubble(e);
		e.stopPropagation();//阻止冒泡
		var _this = $(this);
		_this.parent().find('img').attr('src','${base}/resources/webCom1.0/component/imageCut/img/noImg.jpg');
		_this.parents('.imgCutBox').parent().find('[name="image"]').val('');
	})
})

//回显剪裁后的图片
function ImageURL(base64,url,data,imageid){
	$('#'+imageid+' img').attr('src',base64);
	$('#'+imageid).parent().find('[name="image"]').val(url);
	System.getComponent('#form').clearValidateError($('#form')[0].image);
}