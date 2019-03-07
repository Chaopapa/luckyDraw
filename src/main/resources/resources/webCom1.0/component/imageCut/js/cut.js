System.ready(function(){
	var parentImageId = System.getQueryString('id')||localStorage.imageId;
	
	/**
	 * 获取父页面的配置
	 * my_aspectRatio:裁剪框比例设置
	 */
	var my_aspectRatio = $("#"+parentImageId,window.parent.document).attr("aspectRatio")||"1";
	if(System.getQueryString('aspectRatio')){
		my_aspectRatio = System.getQueryString('aspectRatio');
	}

	/**
	 * 是否显示比例设置按钮
	 */
	var set_aspectRatio = $("#"+parentImageId,window.parent.document).attr("set_aspectRatio")||false;
	if(System.getQueryString('set_aspectRatio') == 'true'){
		set_aspectRatio = true;
	}
	if(set_aspectRatio){
		$("#cutGroupBtn").show();
	}
	
	
	/**
	 * 设置图片上传大小
	 */
	var set_ImageSize = $("#"+parentImageId,window.parent.document).attr("set_ImageSize")||1;
	if(System.getQueryString('set_ImageSize')){
		set_ImageSize = System.getQueryString('set_ImageSize');
	}
	
	/**
	 * 配置
	 */
	var $image = $('#image');
	/**
	 * 判断缓存是否为空
	 */
	//if(localStorage.imageUrl == undefined||localStorage.imageUrl == ''){
		//localStorage.imageUrl = "../img/picture.jpg";
	//}
	//alert(localStorage.imageUrl);
	$image.attr("src","../img/picture.jpg");
	
	var options = {
		//设置裁剪容器的比例
		aspectRatio:my_aspectRatio,
		preview:'.img-preview',
		autoCropArea:1,
		viewMode:2,
		dragMode:'move',
		autoCrop: false,
		crop:function(e){
			
		}
	};
	//根据option配置的图片裁剪
	//$image.cropper(options);
	
	$(".docs-button").on("click","[data-method]",function(){
		var data = $(this).data();
		$image.cropper(data.method,data.option);
	});

	 $('.cutSize').on('click', 'input', function () {
	 	var name = $(this).attr('name');
	 	options[name] = $(this).val();
	 	var lastPic = $image.attr('src');
	 	$image.cropper('destroy').cropper(options);
	 	$image.cropper("reset", true).cropper("replace", lastPic);
	 })
	
	//图片更改
	var $inputImage = $("#inputImage");
	var URL = window.URL || window.webkitURL;
	var blodURL;
	if(URL){
		$inputImage.change(function(){
			var files = this.files;
			var file;
			//if(!$image.data('cropper')){
			//	return;
			//}else{
				$image.cropper(options);
			//}
			
			//有选择文件
			if(files && files.length){
				file = files[0];
			}
			// /^image\/\w+$/ /^开始符号 	image /\==/ \w+==代表字母、数字、下划线 $/结束
			//image/xxx
			//test()用于检测一个字符串是否匹配某个模式
			if(/^image\/\w+$/.test(file.type)){
				$image[0].removeAttribute('onclick');
				$image.cropper(options);
				//createObjectURL创建一个新的对象的URL
				$image.cropper('clear');
				blodURL = URL.createObjectURL(file);
				//built.cropper 当cropper对象构建完成时触发该事件
				$image.one('built.cropper',function(){
					//释放
					//URL.revokeObjectURL();
					//reset 重置裁剪区域的图片到初始化状态
					//replace 替换图片的URL重建cropper
					$image.cropper('crop');
					
					$('.hideBtn').removeClass('hideBtn');
				}).cropper('reset').cropper('replace',blodURL);
				//$inputImage.val('');
				
			}else{
				window.alert('please choose an image file.');
			}
		});
	}else{
		//冻结cropper
		$inputImage.prop('disabled',true).parent().addClass('disabled')
	}
	
	
	/**
	 * 裁剪
	 */
	$("#cutImage").on("click", function() {
		
	    var src = $image.eq(0).attr("src");
	    var canvasdata = $image.cropper("getCanvasData");
	    var cropBoxData = $image.cropper('getCropBoxData');
	    convertToData(src, canvasdata, cropBoxData, function(basechar) {
	        // 回调后的函数处理 
	    	$image.cropper('clear');
	        $image.attr("src",basechar);
	        //console.log(basechar);
	        $image.one('built.cropper',function(){
				//释放
				//URL.revokeObjectURL();
				//reset 重置裁剪区域的图片到初始化状态
				//replace 替换图片的URL重建cropper
				$image.cropper('crop');
				
				$('.hideBtn').removeClass('hideBtn');
			}).cropper("reset", true).cropper("replace", basechar);
	        //localStorage.imageUrl = basechar;
	     
	    });
	})
	
	/**
	 * 功能:获取裁剪位置及转base64
	 */
	function convertToData(url, canvasdata, cropdata, callback) {
	    var cropw = cropdata.width; // 剪切的宽
	    var croph = cropdata.height; // 剪切的宽
	    var imgw = canvasdata.width; // 图片缩放或则放大后的高
	    var imgh = canvasdata.height; // 图片缩放或则放大后的高
	    var poleft = canvasdata.left - cropdata.left; // canvas定位图片的左边位置
	    var potop = canvasdata.top - cropdata.top; // canvas定位图片的上边位置
	    var canvas = document.createElement("canvas");
	    var ctx = canvas.getContext('2d');
	    canvas.width = cropw;
	    canvas.height = croph;
	    var img = new Image();
	    img.src = url;
	    var imgObj = {"width":cropdata.width,"height":cropdata.height};
	    if(parent.cutImageList){
	    	parent.cutImageList(imgObj);
	    }
	    //console.log('this.width='+cropdata.width+"||this.height="+cropdata.height)
	    img.onload = function() {
	        this.width = imgw;
	        this.height = imgh;	       
	        // 这里主要是懂得canvas与图片的裁剪之间的关系位置
	        ctx.drawImage(this, poleft, potop, this.width, this.height);
	        var base64 = canvas.toDataURL('image/jpg', 1);  // 这里的“1”是指的是处理图片的清晰度（0-1）之间，当然越小图片越模糊，处理后的图片大小也就越小
	        callback && callback(base64)      // 回调base64字符串
	    }
	}
	
	
	/**
	 * 上传
	 */
	$("#uploadImage").on("click",function(){
		var result = $image.cropper("getCroppedCanvas");
		var base64 = '';
		try{
			base64 = result.toDataURL().toString();
			//localStorage.imageUrl = result.toDataURL().toString();
			//如果图片大于512kb,则提示图片过大(可以根据用户需求自己设置)
			if(base64/1024/1024>set_ImageSize){
				alert("截取图片不能超过"+set_ImageSize+"M,请重新获取");
			}else{
				System.showLoading();
					if(parent.ImageURL){
						var data = $image.cropper("getData");
						var form = document.getElementById('clipOssUpload');
						var iframe = document.createElement('iframe');
						iframe.name = form.getAttribute('target');
						iframe.times = 0;
						form.width.value = Math.floor(data.width);
						form.height.value = Math.floor(data.height);
						form.posX.value = Math.floor(data.x);
						form.posY.value = Math.floor(data.y);
						iframe.width = 0;
						iframe.height = 0;
						iframe.style.display = 'none';
						iframe.onload = function(){
							if(iframe.times != 0){
								var data = eval('('+iframe.contentWindow.document.body.innerText+')');
								//alert(iframe.contentWindow.document.body.innerText);
								document.body.removeChild(iframe);
								parent.ImageURL(base64,data.urls,$image.cropper("getData"),parentImageId);
								System.hideLoading();
								System.alert({
									title:'提示',
									text:'操作成功',
									type:'info',
									callback:function(){
										System.closeThisWindow();
									}
								})
							}
							iframe.times = 1;
						}
						document.body.appendChild(iframe);
						//parent.ImageURL(test,$image.cropper("getData"));
						form.submit();
					}
				    
				    //ajax上传
				    /*$.ajax({
			            url:'./index.php',  
			            dataType:'text',  
			            type: "post",  
			            data: 'ImageUrl='+test,  
			            success: function (data) {
			             alert(data);
			              console.log('Upload success');  
			            },  
			            error: function () {  
			              console.log('Upload error');  
			            }  
		          });*/
				
				
			}
		}catch(e){
			//alert(e.toString());
		}
	})

})
