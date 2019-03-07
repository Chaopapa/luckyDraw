(function($){
	var once = 0;
	var index = 0;
	$.fn.File = function(i,cb){
		var self = $(this);
		i = i||Math.floor(Math.random()*100000000);
		var id = self.attr('id')||'file_'+new Date().getTime()+''+index+''+i;
		self.attr('id',id);
		self = $('#'+id);
		//添加到队列里面
		if(!window.imgFileList){
			window.imgFileList = [];
		}
		if(window.imgFileList.indexOf(id)==-1){
			window.imgFileList.push(id);
		}
		if(once == 1){
			return;
		}
		once = 1;
		index++;
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		
		self.hide();
		cb = cb||function(){}

		//图片上传的路径
		var url = self.attr('file-url')||realPath+'/admin/file/upload.jhtml';
		//删除图片的方法路径
		var delurl = self.attr('file-del-url')||realPath+'/admin/file/remove.jhtml';
		//总共可以上传的文件个数
		var fileNumLimit = self.attr('file-fileNumLimit')||5;
		//文件重命名，默认id为文件名
		var filename = self.attr('file-fileName')||id;
		//文件上传请求的参数表，每次发送都会发送此对象中的参数
		var formData = self.attr('file-formData')||'{}';
		//允许的文件后缀，不带点，多个用逗号分割。 默认所有文件 例如 gif,jpg,jpeg,bmp,png,zip,rar
		var fext = self.attr('file-ext')||'*';
		//验证单个文件大小是否超出限制, 超出则不允许加入队 单位kb 默认50M
		var fileSingleSizeLimit = self.attr('file-fileSingleSizeLimit')||50000;
		//验证文件总大小是否超出限制, 超出则不允许加入队列 单位kb 默认300M
		var fileSizeLimit = self.attr('file-fileSizeLimit')||300000;
		//上传文件失败回调事件
		var uerror = self.attr('file-error');
		//当文件上传成功时触发
		var usuccess = self.attr('file-success');
		//当所有文件上传结束时触发
		var finished = self.attr('file-finished');
		//不管成功或者失败，文件上传完成时触发
		var complete = self.attr('file-complete');
		//当某个文件的分块在发送前触发，主要用来询问是否要添加附带参数，大文件在开起分片上传的前提下此事件可能会触发多次。
		var uploadBeforeSend = self.attr('file-uploadBeforeSend');
		//是否使用默认http请求头（例如:上传至阿里云有自带回调的http://则无需再添加默认"http"）
		var useHttp = self.attr('file-useHttp')||'false';
		//是否不让点击
		var canClick = self.attr('file-click')||'true';
		
		var http = '';
		if(useHttp=='true'){
			var uploadIp = [];
			for(var i = 0 ;i< 3;i++){
				uploadIp.push(url.split('/')[i]);
			}
			http = uploadIp.join('/');
		}
		
		//获取图片路径
		function getUrlList(items){
			var list = [];
			var ul = $('#filelist-file_'+id);
			if(items){
				$.each(items,function(){
					if($(this).attr('data-json')!=''&&typeof $(this).attr('data-json') != 'undefined'){
						var json = eval('('+$(this).attr("data-json")+')');
						list.push(json.url);
					}
				})
			}else{
				ul.find('li[data-json]').each(function(){
					var json = eval('('+$(this).attr("data-json")+')');
					list.push(json.url);
				})
			}
			self.val(list);
			//如果是在新版的表单上使用则change
			self.trigger('change');
//			if(self.parents('[component="form2"]').length>0){
//				self.trigger('change');
//			}
			return list;
		}
		//所有计时器
		var cst = {};
		var sit = {};
		var fsit = {};
		cst[id] = null;
		sit[id] = null;
		fsit[id] = null;
		function checkaddBtn(time){
			if(!time){time=1000;}
			var ul = $('#filelist-file_'+id);
			clearTimeout(cst[id]);
			nFileNumLimit = ul.find('[data-json]').length;
			ul.find('li.imglist-addli').css({zIndex:-1}).addClass('hide');

			cst[id] = setTimeout(function(){
				if(nFileNumLimit>(fileNumLimit-1)){
					ul.find('li.imglist-addli').css({position:'fixed',zIndex:-1,bottom:-9999}).addClass('hide');
				}else{
					ul.find('li.imglist-addli').css({position:'relative',zIndex:0,bottom:0}).removeClass('hide');
				}
				ul.find('.lo').hide();
				ul.find('li.imglist-addli').css({zIndex:0}).addClass('cur');
				isupload = false;
				
				clearInterval(sit[id]);
				sit[id] = setInterval(function(){
					ul.gridly('layout');
					ul.gridly('draggingBegan');
					ul.gridly('draggingMoved');
					ul.gridly('draggingEnded');
					//判断添加按钮是否在最后，如果不是在最后则继续执行
//					var max = 0;
//					ul.find('li').each(function(){
//						var _this = $(this);
//						var tmax = _this.position().top*1+_this.position().left*1;
//						if(tmax>max){
//							max = tmax;
//						}
//					})
//					var dmax = ul.find('li.imglist-addli').position().top*1+ul.find('li.imglist-addli').position().left*1;
//					console.log(dmax +'=='+ max);
//					if(dmax == max){
//						console.log('给我停下来');
//					}
					console.log(1);
				},10)
			},time)
		}
		//当文件被加入队列以后触发
		var nFileNumLimit = 0;
		var singleFileNumLimit = 0;
		window['fileQueued'+id] = function(file,response){
			var ul = $('#filelist-file_'+id);
			ul.find('li.imglist-addli').css({zIndex:-1});
			var uploader = null;
			if(!ul.find('.cur').hasClass('imglist-addli')){
				nFileNumLimit -- ;
			}
			var ids = ul.find('[uploader]').attr('id');
			uploader = System.getComponent('#'+ids);
			if(nFileNumLimit>(fileNumLimit-1)){
				System.toast('上传文件的个数不能大于'+fileNumLimit+'个');
				//删除队列内刚刚准备上传的文件
				uploader.removeFile(file.id);
				//删除队列中的文件
				ul.parent().find('.uploader-progress .uploader-progress-queue-item').remove();
				ul.find('li.imglist-addli').css({position:'absolute',zIndex:-1,bottom:-9999}).addClass('hide');
			}else{
				ul.find('li.imglist-addli').css({position:'relative',zIndex:0,bottom:0}).removeClass('hide');
			}
			singleFileNumLimit++;
			nFileNumLimit++;
		}
		
		//上传文件失败回调事件
		window['uerror'+id] = function(file,code){
			checkaddBtn();
			(window[uerror]||function(){})(file,code);
		}
		//文件上传完成事件
		window['usuccess'+id] = function(file,response){
			if(response){
				insertFile(response.data,null,file);
				(window[usuccess]||function(){})(file,response);
			}
		}
		//当所有文件上传结束时触发
		window['finished'+id] = function(){
			nFileNumLimit = ul.find('[data-json]').length;
			(window[finished]||function(){})();
		}
		//不管成功或者失败，文件上传完成时触发
		window['complete'+id] = function(file){
			(window[complete]||function(){})(file);
			clearTimeout(fsit[id]);
			fsit[id] = setTimeout(function(){
				console.log('给我停下来');
				clearInterval(sit[id]);
			},10000);
		}
		//上传文件之前回调事件
		window['uploadBeforeSend'+id] = function(obj, data, headers){
			isupload = true;
			var ul = $('#filelist-file_'+id);
			ul.find('li.imglist-addli').css({zIndex:-1});
			ul.find('.imglist-addli').css({position:'absolute',zIndex:-1,bottom:-9999});
			//隐藏关闭上传按钮
			$('#progress-file_'+id+' .cancel a').hide();
			(window[uploadBeforeSend]||function(){})(obj, data, headers);
		}
		//删除上传的图片
		window['removefile'+id] = function(_this){
			System.stopBubble();
			var json = eval('('+$(_this).parents('[data-json]').attr("data-json")+')');
			System.confirm({
				title:'删除确认',
				text:'确定删除图片'+json.fin+'?',
				callback:function(bool){
					if(bool){
						$('#'+$(_this).parents('[data-json]').attr('id')).remove();
						getUrlList();
						var ul = $('#filelist-file_'+id);
						if(ul.find('li[data-json]').length>(fileNumLimit-1)){
							ul.find('li.imglist-addli').css({position:'absolute',zIndex:-1,bottom:-9999}).addClass('hide');
						}else{
							ul.find('li.imglist-addli').css({position:'relative',zIndex:0,bottom:0}).removeClass('hide');
						}
						nFileNumLimit = ul.find('[data-json]').length;
						cid = null;

						ul.gridly('layout');
					}
				}
			})
		}
		var cid = null;
		
		//将文件插入预览队列
		var rst = null;
		function insertFile(_url,i2,file){
			var ul = $('#filelist-file_'+id);
			if(typeof i2 == 'undefined'||i2 === null){
				i2 = new Date().getTime();
			}
			var html2 = '';
			var eUrl = _url;
			var ext = eUrl.substring(eUrl.lastIndexOf('.')+1,eUrl.length);
			ext = ext.toLowerCase();
			var imgExt = "gif,jpg,jpeg,bmp,png";
			var videoExt = "3gp,mp4,avi";
			var imgsDefaultUrl = basePaths+'/component/file/img/error.png';
			var filesDefaultUrl = basePaths+'/component/file/img/file.png';
			var videosDefaultUrl = basePaths+'/component/file/img/video.png';
			var isImgs = imgExt.indexOf(ext)!=-1&&imgExt.split(',')[imgExt.split(',').indexOf(ext)].length==ext.length?true:false;
			var isVideo = videoExt.indexOf(ext)!=-1&&videoExt.split(',')[videoExt.split(',').indexOf(ext)].length==ext.length?true:false;

			var rel = '';
			var fileName = eUrl.substring(eUrl.lastIndexOf('/')+1,eUrl.length);
			
			//收集标签上的需要的属性
			var data = {
				'type':'',
				'title':'',
				'id':'',
				'data-json':'',
				'style':'',
				'data-id':'',
				'fileName':'',
				'data-value':''
			}
			var odata = {
				'type':'',
				'title':'',
				'id':'',
				'data-json':'',
				'style':'',
				'data-id':'',
				'fileName':'',
				'data-value':''
			}
			
			if(isImgs){
				//说明是新增图片
				if(file){
					html2+='	<li data-id="'+id+'" fileName="'+fileName+'" type="img" title="'+eUrl+'" id="fid'+i2+'-'+id+'" data-json={fid:"fid'+i2+'",fin:"'+fileName+'",url:"'+eUrl+'",type:"return"} style="background:url('+basePaths+'/component/file/img/imgloading.gif) #f1f1f1 center center no-repeat;background-size:100% 100%;cursor:pointer;" data-value="{title:\'图片预览 - '+fileName+'\',url:\''+http+eUrl+'\',width:500,height:500,maxmin:true}"><div class="imglist-panel"><span class="btn del" title="删除" ></span><span id="fid'+i2+'-'+id+'-rel" '+rel+' class="btn rel" title="重新上传" ></span></div><div class="imglist-container" ></div><div class="imglist-name">'+fileName+'</div></li>';
					(function(id,cid,i2,file){
						var uploader = null;
						var cids = ul.find('[uploader]').attr('id');
						System.getComponent('#'+cids).makeThumb(file, function (error, src) {
			                if (error) {
			                    return;
			                }
							$('#fid'+i2+'-'+id).css('backgroundImage','url('+src+')');
						});
					})(id,cid,i2,file)
				}else{
					html2+='	<li data-id="'+id+'" fileName="'+fileName+'" type="img" title="'+eUrl+'" id="fid'+i2+'-'+id+'" data-json={fid:"fid'+i2+'",fin:"'+fileName+'",url:"'+eUrl+'",type:"return"} style="background:url('+http+eUrl+') #f1f1f1 center center no-repeat;background-size:100% 100%;cursor:pointer;" data-value="{title:\'图片预览 - '+fileName+'\',url:\''+http+eUrl+'\',width:500,height:500,maxmin:true}"><div class="imglist-panel"><span class="btn del" title="删除" ></span><span id="fid'+i2+'-'+id+'-rel" '+rel+' class="btn rel" title="重新上传"></span></div><div class="imglist-container" ></div><div class="imglist-name">'+fileName+'</div></li>';
				}
			}else if(isVideo){
				html2+='	<li data-id="'+id+'" fileName="'+fileName+'" type="video" title="'+eUrl+'" id="fid'+i2+'-'+id+'" data-json={fid:"fid'+i2+'",fin:"'+fileName+'",url:"'+eUrl+'",type:"return"} style="background: url('+videosDefaultUrl+') #f1f1f1 center center no-repeat;cursor:pointer;" data-value="{title:\'播放视频 - '+fileName+'\',url:\''+basePaths+'/component/file/html/video.html?url='+http+eUrl+'\&ext='+ext+'&v='+Math.floor(new Date().getTime())+'\',width:500,height:500,maxmin:true}"><div class="imglist-panel"><span class="btn del" title="删除" ></span><span id="fid'+i2+'-'+id+'-rel" '+rel+' class="btn rel" title="重新上传" ></span></div><div class="imglist-container" ></div><div class="imglist-name">'+fileName+'</div></li>';
			}else{
				html2+='	<li data-id="'+id+'" fileName="'+fileName+'" type="other" title="'+eUrl+'" id="fid'+i2+'-'+id+'" data-json={fid:"fid'+i2+'",fin:"'+fileName+'",url:"'+eUrl+'",type:"return"} style="background:url('+basePaths+'/component/file/img/imgloading.gif) #f1f1f1 center center no-repeat;"><img src="'+basePaths+'/component/file/img/'+ext+'.png" style="display: none;" onload="this.parentNode.style.backgroundImage = \'url('+basePaths+'/component/file/img/'+ext+'.png)\';" onerror="this.parentNode.style.backgroundImage = \'url('+basePaths+'/component/file/img/file.png)\';" /><div class="imglist-panel"><span class="btn del" title="删除" ></span><span id="fid'+i2+'-'+id+'-rel" '+rel+' class="btn rel" title="重新上传"></span></div><div class="imglist-container" ></div><div class="imglist-name">'+fileName+'</div></li>';
			}
			var html2 = $(html2);
			

			for(var i in data){
				data[i] = html2.attr(i);
			}
			
			if(!ul.find('li.cur').hasClass('imglist-addli')){
				ul.find('li.cur').attr(odata);
				ul.find('li.cur').attr(data);
				ul.find('li.cur .imglist-name').html(data.fileName);
				ul.find('li.cur').removeClass('cur');
				ul.find('li.imglist-addli').addClass('cur');
			}else{
				ul.find('li.cur').before(html2);
			}
			
			//添加重新上传绑定事件
			if(!window.isBindEvent){
				window.isBindEvent = true;
				
				var m = {}
				$(document).on('mousemove','.btn.del,.btn.rel,li[type] .imglist-container',function(event){
					var _this = $(this);
					m = {
						x:_this.parents('li[type]').offset().left,
						y:_this.parents('li[type]').offset().top
					}
				})
				
				//查看
				$(document).on('mouseup','li[type] .imglist-container',function(event){
					var _this = $(this);
					var p = _this.parents('li[type]');
					if(m.x != p.offset().left||m.y != p.offset().top){
						return;
					}
					m = {
						x:p.offset().left,
						y:p.offset().top
					}
					if(p.attr('type')=='img'){
						var data = p.attr('data-value');
						data = eval('('+data+')');
						var bg = $(this).parent().css('background-image').replace('url(','').replace(')','');;
						var showImg = function(_this){
							System.hideLoading();
							if(_this.width==0||_this.height==0){
								System.toast('图片获取失败');
								return;
							}
							var w = $(System.top.window);
							var ww = w.width()-60;
							var wh = w.height()-60;
							var p = _this.width/_this.height;
							data.type = 1;
							data.title = '';
							data.move = '.layui-layer-content';
							//data.maxmin = false;
							data.shadeClose = true;
							if(_this.width>this.height){
								if(_this.width>ww){
									_this.width = ww;
									_this.height = _this.width/p;
								}
								if(_this.height>wh){
									_this.height = wh;
									_this.width = _this.height*p;
								}
							}else{
								if(_this.height>wh){
									_this.height = wh;
									_this.width = _this.height*p;
								}
								if(_this.width>ww){
									_this.width = ww;
									_this.height = _this.width/p;
								}
							}
							data.area = [_this.width+'px',_this.height+'px'];
							data.content = '<div style="text-align:center;"><img src="'+_this.src+'" style="max-width:100%;" /></div>';
//							alert(JSON.stringify(data));
							System.top.layer.open(data);
						}

						System.showLoading();
						var img = new Image();
						img.onload = function(){
							showImg(this);
						}
						img.onerror = function(){
							var img2 = new Image();
							img2.onload = function(){
								showImg(this);
							}
							img2.src = bg.substring(1,bg.length-1);
						}
						img.src = data.url;
					}
					if(p.attr('type')=='video'){
						var data = p.attr('data-value');
						data = eval('('+data+')');
						System.top.System.openWindow(data);
					}
				})
				
				//删除
				$(document).on('mouseup','.btn.del',function(event){
					var _this = $(this);
					var p = _this.parents('li[type]');
					if(m.x != p.offset().left||m.y != p.offset().top){
						return;
					}
					m = {
						x:p.offset().left,
						y:p.offset().top
					}
					var id = p.attr('data-id');
					window['removefile'+id](this);
				})
				
				//重新上传
				$(document).on('mouseup','.btn.rel',function(event){
					var _this = $(this);
					var p = _this.parents('li[type]');
					if(m.x != p.offset().left||m.y != p.offset().top){
						return;
					}
					m = {
						x:p.left,
						y:p.offset().top
					}
					singleFileNumLimit = 0;
					var _this = $(this);
					var panel = _this.parents('.imglist');
					_this.parents('[type]').addClass('cur');
					panel.find('.imglist-addli').removeClass('cur');
					var abtn = panel.find('[uploader]');
					var id = abtn.attr('id');
					cid = id;
					var multiple = false;
					var mimeTypes = abtn.attr('uploader-ext').Trim();
					var accepts = [];
		    		var mimeType2 = mimeTypes.split(',');
		    		for(var i = 0 ; i < mimeType2.length ; i ++){
		    			accepts.push('image/'+mimeType2[i]);
		    		}
					var webUploaderInput = document.createElement('input');
					webUploaderInput.type = 'file';
					webUploaderInput.id = 'fileUploader';
					webUploaderInput.multiple = false;
					webUploaderInput.accept = mimeTypes=='*'?mimeTypes:accepts.join(',');
					webUploaderInput.onchange = function(){
						System.getComponent('#'+id).addFiles(this.files);
		    			System.getComponent('#'+id).upload();
						webUploaderInput.value = '';
						webUploaderInput.parentNode.removeChild(webUploaderInput);
					}
					webUploaderInput.style.cssText = 'display:none;';
					document.body.appendChild(webUploaderInput);
					webUploaderInput.click();
				})
			}
			checkaddBtn();
			getUrlList();
		}
		
		//处理回显路径
		if(typeof self.val() == 'undefined'){
			return;
		}
		var echoUrl = self.val().split(',');
		if(canClick=="false"){
			var img_index = -1;
		}else{
			var img_index = 0;
		}
		var html = '';
		html+='<div class="fileBox">';
		html+='<ul id="filelist-file_'+id+'" class="imglist gridly" style="z-index:'+img_index+'">';
		html+='	<li class="imglist-addli cur" >';
		html+='	<a href="javascript:void(0);" class="imglist-addli-addBtn"><img src="'+basePaths+'/component/file/img/add.png" /></a>';
		html+='<div id="file_'+id+'" uploader style="position:absolute;left:0;z-index:'+img_index+';top:0;right:0;bottom:0;opacity:0;padding:0;"';
		html+='	uploader-autoupload="true" ';
		html+='	uploader-filename="'+filename+'" ';
		html+='	uploader-formData="'+formData+'" ';
		html+='	uploader-fileSingleSizeLimit="'+fileSingleSizeLimit+'" ';
		html+='	uploader-fileSizeLimit="'+fileSizeLimit+'" ';
		html+='	uploader-ext="'+fext+'" ';
		html+='	uploader-url="'+url+'" ';
		html+='	uploader-fileQueued="fileQueued'+id+'" ';
		html+='	uploader-success="usuccess'+id+'" ';
		html+='	uploader-error="uerror'+id+'" ';
		html+='	uploader-finished="finished'+id+'" ';
		html+='	uploader-complete="complete'+id+'" ';
		html+='	uploader-uploadBeforeSend="uploadBeforeSend'+id+'" ';
		html+='	uploader-text="请选择图片"';
		html+='	uploader-progress="progress-file_'+id+'"';
		html+='></div>';
		html+='	</li>';
		html+='</ul>';
		html+='<div id="progress-file_'+id+'"></div>';
		html+='</div>';
		self.after(html);
		
		var ul = $('#filelist-file_'+id);
		if(self.val().Trim()!=""){
			ul.find('.imglist-addli').css({position:'absolute',zIndex:-1,bottom:-9999});
			for(var i = 0 ;i<echoUrl.length;i++){
				insertFile(echoUrl[i],i);
			}
		}else{
			ul.find('.lo').hide();
		}
		
		clearTimeout(fsit[id]);
		fsit[id] = setTimeout(function(){
			console.log('给我停下来');
			clearInterval(sit[id]);
		},2000);
		
		var ui = $('#file_'+id);
		System.initComponent(ui,'uploader',function(){
			$('#filelist-file_'+id).parent().find('.ly').hide();
			cb();
			
			//添加拖拽效果
			$('#filelist-file_'+id).gridly({
				base: 100, // px 
    			gutter: 10, // px
				columns: $('#filelist-file_'+id).width()/110,
			    draggable: {
			        selector: '> [type]'
			    },
			    callbacks:{
			    	reordering:function($elements){
			    		//console.log($elements);
			    	},reordered:function($elements){
			    		getUrlList($elements);
			    	}
			    }
			})
			//禁止拖拽
			if(fileNumLimit == 1){
				$('#filelist-file_'+id).gridly('draggable','off'); // disables dragging
			}

			self.attr('isInit','true');
			System.setReady(self);//告诉base我已加载完成 
			window.imgFileList.shift();
			//执行下一个file
			once = 0;
			if(window.imgFileList.length>0){
				$('#'+window.imgFileList[0]).File(0);
			}
		});
	    return self;
	}
})(jQuery)
$(function(){
	$('[component="file"]').each(function(i){
		$(this).File(i);
	})
})

