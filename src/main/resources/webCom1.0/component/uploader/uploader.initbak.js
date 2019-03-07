;
(function($){
	$.fn.Uploader = function(){
		var self = $(this);
		var id = self.attr('id')||'uploader_'+new Date().getTime();
		self = $('#'+id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			return;	
		}
		System.setComponentsId(id);
		var buttonText = function(){
			if(self[0].tagName.toLowerCase()=='input'){
				return self.attr('placeholder')||'请选择';
			}else{
				return self.attr('uploader-text')||'请选择';
			}
		}
		var saveStyle = self.attr('uploader-saveStyle')||'false';//是否保留原样式
		var showItemTemplate = self.attr('uploader-showItemTemplate')||'';//显示进度条
		var fileSizeLimit = self.attr('uploader-fileSizeLimit')||'10240';//文件大小限制 单位KB
		var itemTemp = self.attr('uploader-fileSizeLimit');//文件大小限制 单位KB
		itemTemp = window[itemTemp]||function(){
			//上传模版
			var itemTemplate = '';
			itemTemplate+='<div id="${fileID}" class="uploadify-queue-item '+showItemTemplate+'">';
	        itemTemplate+='    <div class="cancel">';
	        itemTemplate+='        <a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a>';
	        itemTemplate+='    </div>';
	        itemTemplate+='    <span class="fileName">${fileName} (${fileSize})</span><span class="data"></span>';
	        itemTemplate+='		<div class="uploadify-progress">';
			itemTemplate+='			<div class="uploadify-progress-bar"><!--Progress Bar--></div>';
			itemTemplate+='		</div>';
	        itemTemplate+='</div>';
	        return itemTemplate;
		}
		
		
		self.Huploadify({
			fileTypeExts:'*.jpg;*.png;',
			auto     		: true,//是否自动上传 如果为false则需要事件来触发 self.uploadify('upload')
			buttonText 		: buttonText(),
			fileSizeLimit 	: fileSizeLimit+'KB',//文件大小限制
			multi    		: false,//校验重复上传
			progressData 	: 'percentage',//进度条显示的 百分比还是上传速率percentage|speed默认百分比
			uploadLimit 	: 999,//上传个数限制 默认 999
	        swf           	: basePaths+'/component/uploader/js/uploadify.swf',
	        uploader      	: 'http://192.168.10.173:8080/spring_mvc_demo/fileUpload/',
	        height        	: self.outerHeight(),
	        width         	: self.outerWidth(),
	        itemTemplate 	: itemTemp(),
	        onSelectError 	: function(file,errorCode,errorMsg) {
	            //alert('The file ' + file.name + ' returned an error and was not added to the queue.');
	        },
	        onUploadSuccess : function(file, data, response) {
	            //alert('The file ' + file.name + ' was successfully uploaded with a response of ' + response + ':' + data);
	        },
	        onInit   		: function(instance) {
//	        	if(saveStyle=='true'){
//	            	var btn = $('#'+id+'-button');
//	            	btn.removeAttr('class');
//	            	btn.removeAttr('style');
//		            btn.html(self[0].outerHTML);
//	           	}
	        }
	    });
	    return self;
	}
})(jQuery)
$(function(){
	$('[component="uploader"]').each(function(){		
		$(this).Uploader();
	})
})

