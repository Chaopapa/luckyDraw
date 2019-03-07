(function($){
	$.fn.Uploader = function(i,cb){
		var self = $(this);
		var id = self.attr('id')||'uploader_'+new Date().getTime();
		self.attr('id',id);
		self = $('#'+id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		
		System.setComponentsId(id);
//		self.removeClass('webuploader-container');
		self.addClass('btn');
		
		cb = cb||function(){}
		
		//上传按钮的文字提示
		var buttonText = function(){
			if(self[0]){
				if(self[0].tagName.toLowerCase()=='input'){
					return self.attr('placeholder')||'请选择';
				}else if(self[0].tagName.toLowerCase()=='button'){
					return self.html()||'请选择';
				}else{
					return self.attr('uploader-text')||'请选择';
				}
			}else{
				return '请选择';
			}
		}
		//url上传路径
		var url = self.attr('uploader-url')||realPath+'/admin/oss/upload';
		//是否自动上传文件
		var autoUpload = self.attr('uploader-autoUpload')||'true';
		//文件重命名，默认id为文件名
		var filename = self.attr('uploader-fileName')||id;
		//文件上传请求的参数表，每次发送都会发送此对象中的参数
		var formData = self.attr('uploader-formData')||'{}';
		//允许的文件后缀，不带点，多个用逗号分割。 默认所有文件 例如 gif,jpg,jpeg,bmp,png,zip,rar
		var ext = self.attr('uploader-ext')||'*';
		//上传文件的进度条框架
		var progress = self.attr('uploader-progress');
		//验证单个文件大小是否超出限制, 超出则不允许加入队 单位kb 默认5M
		var fileSingleSizeLimit = self.attr('uploader-fileSingleSizeLimit')||5000;
		//验证文件总大小是否超出限制, 超出则不允许加入队列 单位kb 默认300M
		var fileSizeLimit = self.attr('uploader-fileSizeLimit')||300000;
		//当某个文件的分块在发送前触发，主要用来询问是否要添加附带参数，大文件在开起分片上传的前提下此事件可能会触发多次。
		var uploadBeforeSend = self.attr('uploader-uploadBeforeSend');
		//当一批文件添加进队列以后触发。
		var fileQueued = self.attr('uploader-fileQueued');
		//上传文件失败回调事件
		var uerror = self.attr('uploader-error');
		//当文件上传成功时触发
		var usuccess = self.attr('uploader-success');
		//当所有文件上传结束时触发
		var finished = self.attr('uploader-finished');
		//不管成功或者失败，文件上传完成时触发
		var complete = self.attr('uploader-complete');
		//上传模版
		var progressTemp = function(file){
			if(file.name.length>30){
	        	file.name = file.name.substring(file.name.length-30,file.name.length);
	        }
			progressTempHtml='';
			progressTempHtml+='<div id="item_'+file.id+'" class="uploader-progress-queue-item">';
			progressTempHtml+='    <div class="cancel"><a href="javascript:System.getComponent(\'#'+id+'\').remove(\''+file.id+'\')">X</a></div>';
			progressTempHtml+='    <span class="fileName">'+file.name+'</span><span class="percentage">准备上传</span>';
			progressTempHtml+='	<div class="uploader-progress-progress">';
			progressTempHtml+='		<div class="uploader-progress-progress-bar"></div>';
			progressTempHtml+='	</div>';
			progressTempHtml+='</div>';
			return progressTempHtml;
		}
		
		
	    if ( !WebUploader.Uploader.support() ) {
	        alert( 'Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
	        throw new Error( 'WebUploader does not support the browser you are using.' );
	    }
	    
	    // 实例化
	    var uploader = new WebUploader.Uploader({
	        pick: {
	            id: '#'+id,
	            label: buttonText()
	        },
	        paste: document.body,
//	        runtimeOrder:'flash',//flash|html5
	        fileVal:filename,
	        formData :eval('('+formData+')'),
	        accept: {
	            title: '文件',
	            extensions: ext,
	            mimeTypes: ''
	        },
			compress:{
			    width: 1600,
   	 			height: 1600,
			    // 图片质量，只有type为`image/jpeg`的时候才有效。
			    quality: 100,
			    // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
			    allowMagnify: false,
			    // 是否允许裁剪。
			    crop: false,
			    // 是否保留头部meta信息。
			    preserveHeaders: true,
			    // 如果发现压缩后文件大小比原来还大，则使用原来图片
			    // 此属性可能会影响图片自动纠正功能
			    noCompressIfLarger: false,
			    // 单位字节，如果图片大小小于此值，不会采用压缩。
			    compressSize: 0
			},
	        // swf文件路径
	        swf: basePaths + '/component/uploader/js/Uploader.swf',
//	        disableGlobalDnd: true,
	        chunked: false,
	        server: url,
	        //验证文件总数量, 超出则不允许加入队列
	        fileNumLimit: 300,
	       	// 验证文件总大小是否超出限制, 超出则不允许加入队列
	        fileSizeLimit: fileSizeLimit*1024,
	        //验证单个文件大小是否超出限制, 超出则不允许加入队 单位kb
	        fileSingleSizeLimit: fileSingleSizeLimit*1024
	    });
	    //上传文件之前回调事件
	    uploader.onUploadBeforeSend = function(obj, data, headers) {
	    	(window[uploadBeforeSend]||function(){})(obj, data, headers);
	    };
	    
	    var prog = $('#'+progress);
	    var files = {};
	    //当文件被加入队列以后触发
	    uploader.onFileQueued = function( file ) {
	    	files[file.id] = file;
	    	if(prog.length>0){
	    		//创建上传模版
		        prog.addClass('uploader-progress');
		        prog.append(progressTemp(file));
	    	}
	        //uploader.addFiles(file);
	        uploader.makeThumb( file, function( error, ret ) {
	        	(window[fileQueued]||function(){})(file,error, ret);
		        if ( error ) {
		            
		        } else {
//		        	console.log(ret);
		        }
		    });
	        
	        if(autoUpload=='true'){
	        	uploader.upload();
	        }
	    };
	    
	    //上传过程中触发，携带上传进度。
	    uploader.onUploadProgress = function( file, percentage ) {
	    	if(prog.length>0){
	    		prog.show();
		    	var pItem = $('#item_'+file.id);
		        var progressBar = pItem.find('.uploader-progress-progress-bar');
		        var per = pItem.find('.percentage');
		        progressBar.css('width', Math.round( percentage * 100 ) + '%');
		        per.html(Math.round( percentage * 100 ) + '%');
	       	}
	    };
	    
	    //当validate不通过时，会以派送错误事件的形式通知调用者。
	    uploader.onError = function(code) {
	    	var str = '';
	    	if(code=='Q_EXCEED_NUM_LIMIT'){
	    		str = '添加的文件数量超出验证文件总数量';
	    	}else if(code=='Q_EXCEED_SIZE_LIMIT'){
	    		str = '添加的文件总大小超出 '+fileSizeLimit*1024+'kb<br/>请更改 uploader-fileSizeLimit 值';
	    	}else if(code=='Q_TYPE_DENIED'){
	    		str = '添加的文件类型不满足,请添加 '+ext+' 格式的文件';
	    	}else if(code=='F_EXCEED_SIZE'){
	    		str='添加单个文件大小超出 '+fileSingleSizeLimit*1024+'kb<br/>请更改 uploader-fileSingleSizeLimit 值';
	    	}else {
	    		str='文件校验失败';
	    	}
	    	System.alert(str);
	    };
	    
	    //当文件上传出错时触发。
	    uploader.onUploadError = function(file,code) {
	    	(window[uerror]||function(){})(file,code);
	    	System.alert(file.name+'上传文件失败,原因:'+code);
	    };
	    
	    //当文件上传成功时触发
	    uploader.onUploadSuccess = function(file,response) {
	    	(window[usuccess]||function(){})(file,response);
	    	console.log('文件上传成功时触发');
	    }
	    
	    //当所有文件上传结束时触发
	    uploader.onUploadFinished = function() {
	    	(window[finished]||function(){})();
	    }
	    
	    //不管成功或者失败，文件上传完成时触发。
	    uploader.onUploadComplete = function( file) {
	    	setTimeout(function(){
	    		var pItem = $('#item_'+file.id);
	    		pItem.find('.cancel').hide();
	        	pItem.addClass('uhide');
	    	},1000)
	    	uploader.removeFile(file);
	    	(window[complete]||function(){})(file);
	    };
//	    uploader.addButton({
//		    id: '#'+id,
//		    innerHTML: buttonText
//		});
		window['#'+id] = uploader;
		window['#'+id]['remove'] = function(fid){
			System.getComponent('#'+id).cancelFile(files[fid]);
			System.getComponent('#'+id).removeFile(files[fid]);
			var pItem = $('#item_'+files[fid].id);
			pItem.remove();
		}
		
		
		setTimeout(function(){
			self.find('div').css({
				left:0,
				top:0,
				width:'100%',
				height:'100%',
				lineHeight:self.height()+'px'
			})
		},1)
		
		cb();
		
		System.setReady(self);//告诉base我已加载完成 
	    return self;
	}
})(jQuery)
$(function(){
	var i = 0;
	function createComponent(com){
		com.eq(i).Uploader(i,function(){
			if(i<com.length-1){
				i++;
				createComponent(com,i);
			}
		});
	}
	var component = $('[component="uploader"]');
	if(component.length>0){
		createComponent(component);
	}
})
window.uploaderInit = true;
