(function($){
	$.fn.Form = function(){
		var self = $(this);
		var id = self.attr('id')||'form_'+new Date().getTime();
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		var action = self.attr('action')||'';
		var onSubmit = self.attr('form-onSubmit');//一般配合action使用，当表单没有action属性或者action属性为空时该属性生效，直接触发表单提交事件（主要目的是用来子定义ajax提交方法），当action属性有值或者不为空，则该属性失效
		var onload = self.attr('form-onload');
		var beforeSerialize = self.attr('form-beforeSerialize');
		var beforeSubmit = self.attr('form-beforeSubmit');
		var success = self.attr('form-success');
		var ferror = self.attr('form-error');
		var complete = self.attr('form-complete');
		var clearForm = (self.attr('form-afterSuccess-clearForm')=="true")?true:false;
		var resetForm = (self.attr('form-afterSuccess-resetForm')=="true")?true:false;
		onSubmit = window[onSubmit]||function(data){};
		beforeSubmit = window[beforeSubmit]||function(data){};
		beforeSerialize = window[beforeSerialize]||function(data){};
		success = window[success]||function(data){};
		ferror = window[ferror]||function(data){};
		complete = window[complete]||function(data){};
		onload = window[onload]||function(data){};
//		console.log(self.serialize());
		var data = {};
		var options = {
//		   	target: '#output',          	//把服务器返回的内容放入id为output的元素中      
			//在序列化表单参数之前
			beforeSerialize:function(jqForm,options){
				return beforeSerialize({
		   			jqForm:jqForm,
		   			options:options,
		   			form:window['#'+id]
		   		});
			},
		   	beforeSubmit: function(arr, jqForm, options){
		   		return beforeSubmit({
		   			arr:arr,
		   			jqForm:jqForm,
		   			options:options,
		   			form:window['#'+id]
		   		});
		   	},  	//提交前的回调函数
		   	success: function(data){
		   		if(action.length>0){
		   			success({
			   			data:data
			   		});
		   		}else{
		   			onSubmit(window['#'+id]);
		   		}
		   	},
		   	error:function(xhr){
		   		console.log(xhr);
		   		ferror(xhr);
		   		if(xhr.status=='403'){
		   			$.ajax({
		   				url:realPath+'/common/checkLogin.jhtml',
		   				success:function(bool){
		   					//  false   未登陆     true  已登陆
		   					if(bool||bool=='true'){
		   						System.top.System.openWindow({
					   				url:realPath+"/common/error"+xhr.status+".jhtml?type=min",
					   				width:'930',
					   				height:'500'
					   			});
		   					}else{
		   						System.top.System.confirm({
		   			   			  	title:'登录超时',
		   			   			  	text:'是否重新登录？',
		   			   			  	type:'info',
		   			   			  	btns:['重新登录'],
		   			   			  	callback:function(bool){
		   			   					if(bool){
		   			   						System.top.System.openWindow({
		   						   				url:realPath+"/admin/login.jhtml?type=min",
		   						   				width:'930',
		   						   				height:'500'
		   						   			});
		   			   					}
		   			   				}
		   			   			})
		   					}
		   				}
		   			})
		   		}
		   		if(xhr.status=='404'){
		   			System.top.System.openWindow({
		   				url:realPath+"/common/error"+xhr.status+".jhtml?type=min",
		   				width:'930',
		   				height:'500'
		   			});
		   		}
		   	},
		   	complete:function(xhr){
		   		complete(xhr);
		   	},
		   	//提交后的回调函数
		   	data:data,
		   	uploadProgress:function(event,position,total,percentComplete ){
		   		console.log(event+'=='+position+'=='+total+'=='+percentComplete);
		   	},
		   	//url: url,                 	//默认是form的action， 如果申明，则会覆盖  
		   	//type: type,               	//默认是form的method（get or post），如果申明，则会覆盖  
		   	//dataType: null,           	//html(默认), xml, script, json...接受服务端返回的类型  
			clearForm: clearForm,      		//成功提交后，清除所有表单元素的值  
		   	resetForm: resetForm,          	//成功提交后，重置所有表单元素的值  
		   	timeout: 3000               	//限制请求的时间，当请求大于3秒后，跳出请求  
		}  
		var st = setInterval(function(){
			if(self.ajaxForm){
				clearInterval(st);
				self.ajaxForm(options);
			}
		},10)
		
		self.find('input,select,textarea').change(function(){
			if(typeof validate == 'undefined'){
				console.log('未找到validate方法');
				validate = function(jqForm){}
			}
			try{
				validate(self);
			}catch(e){console.log(e);}
		})
		
		window['#'+id] = self;
		//添加参数
		window['#'+id]['setParameter'] = function(name,value){
			data[name] = value;
		}
		//添加参数
		window['#'+id]['getParameters'] = function(name){
			var json = {};
			var serialize = self.serialize().split('&');
			for(var i = 0 ; i < serialize.length;i++){
				var kv = serialize[i].split('=');
				json[kv[0]] = kv[1];
			}
			data = $.extend(data,json);
			if(typeof name !='undefined'){
				return data[name];
			}else{
				var serializStr = [];
				for(var i in data){
					serializStr.push(i+'='+data[i]);
				}
				return serializStr.join("&");
			}
		}
		//添加参数
		window['#'+id]['getJSONParameters'] = function(name){
			var json = {};
			var serialize = self.serialize().split('&');
			for(var i = 0 ; i < serialize.length;i++){
				var kv = serialize[i].split('=');
				json[kv[0]] = kv[1];
			}
			data = $.extend(data,json);
			if(typeof name !='undefined'){
				return data[name];
			}else{
				return data;
			}
		}
		setTimeout(function(){
			onload(window['#'+id],self);
			System.setReady(self);//告诉base我已加载完成
		},300)
	    return self;
	}
})(jQuery)

$(function(){
	$('[component="form"]').each(function(){
		$(this).Form();
	})
})
