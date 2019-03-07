(function($){
	$.fn.Form2 = function(){
		var self = $(this);
		var id = self.attr('id')||'form2_'+new Date().getTime();
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
		var validateSuccess = self.attr('form-validateSuccess');//所有验证成功完成事件
		var success = self.attr('form-success');
		var ferror = self.attr('form-error');
		var complete = self.attr('form-complete');
		var clearForm = (self.attr('form-afterSuccess-clearForm')=="true")?true:false;
		var resetForm = (self.attr('form-afterSuccess-resetForm')=="true")?true:false;
		onSubmit = window[onSubmit]||function(data){};
		beforeSubmit = window[beforeSubmit]||function(data){};
		beforeSerialize = window[beforeSerialize]||function(data){};
		validateSuccess = window[validateSuccess]||function(data){};
		success = window[success]||function(data){};
		ferror = window[ferror]||function(data){};
		complete = window[complete]||function(data){};
		onload = window[onload]||function(data){};
//		console.log(self.serialize());

		var fThis = this;
		fThis.removeError = function(jd){
			$(jd).parent().removeClass('has-error');
			if($(jd).parent().find('.error')){
				$(jd).parent().find('.error').remove();
			}
		}
		fThis.validateTxt = function(jd,txt){
			fThis.removeError(jd);
			$(jd).parent().addClass('has-error');
      		$(jd).parent().append('<div class="error help-block "><i class="fa fa-info-circle"></i>&nbsp;'+txt+'</div>');
		}
		/**
		 * @param {Object} vdom 当前录入对象
		 */
		fThis.validate = function(vdom){
			var _this = this;
			var tl = [];
			
			//利用jqForm对象  
			var jqForm = self;
	   		var form = jqForm[0]; //把表单转化为dom对象  
	   		var dom = null;
	   		
	   		for(var i = 0 ; i < form.length; i++){
	   			dom = form[i];
				if(dom.getAttribute('validate')!=null){
					//验证空
					if(vdom==dom||!vdom){
//						if(dom.getAttribute('validate-empty')=="false"||dom.getAttribute('validate-empty')){
//						}else{
							var defaultTxt = (dom.type.toLowerCase()=="checkbox"||dom.type.toLowerCase()=="radio")?'请选择':'请输入';
							var errorTxt = dom.getAttribute('placeholder')||$(dom).parents('[placeholder]').attr('placeholder')||defaultTxt;
							//选择
							if(dom.type.toLowerCase()=="checkbox"||dom.type.toLowerCase()=="radio"){
								var cl = jqForm.find('[name="'+dom.name+'"]').length;
								fThis.removeError($(dom).parent());
								if(dom.getAttribute('validate-isEmpty')=="false"){
									
								}else{
									if (!dom.checked&&cl==1){
							      		fThis.validateTxt($(dom).parent(),errorTxt);
							      		tl.push(false);
							      	}else if(!dom.checked&&cl>1){
							      		var lcl = jqForm.find('[name="'+dom.name+'"]:checked').length;
										if(lcl<1){
											fThis.validateTxt($(dom).parent(),errorTxt);
							    			tl.push(false);
										}
							      	}
								}
							}else{
								//输入
								if(dom.getAttribute('component')+"".Trim()=='dropDownSearch'){
									fThis.removeError($(dom).parent());
								}else{
									fThis.removeError(dom);
								}
								if(dom.getAttribute('validate-isEmpty')=="false"){
									
								}else{
									if(!dom.value||dom.value.Trim()==""){
										if(dom.getAttribute('component')+"".Trim()=='dropDownSearch'){
											fThis.validateTxt($(dom).parent(),errorTxt);
										}else{
											fThis.validateTxt(dom,errorTxt);
										}
						      			tl.push(false);
									}
								}
							}
//						}

						if(dom.value.Trim().length>0){
							//自定义验证(值不能为空)
							var fvalidate = window[dom.getAttribute('validate')]||null;
							if(fvalidate){
								if(dom){
									var result = fvalidate(jqForm,dom)||"";
									if(result.length>0){
										//选择
										if(dom.type.toLowerCase()=="checkbox"||dom.type.toLowerCase()=="radio"){
								      		fThis.validateTxt($(dom).parent(),result);
								      		tl.push(false);
										}else{
											//输入
											if(dom.getAttribute('component')+"".Trim()=='dropDownSearch'){
												fThis.validateTxt($(dom).parent(),result);
											}else{
												fThis.validateTxt(dom,result);
											}
							      			tl.push(false);
							      		}
									}
								}
							}
						}
						
						if(vdom==dom){
							break;
						}
					}
				}
	   		}
	      	if(tl.length>0){
	      		form.vde = false;
	      		return false;
	      	}else{
	      		form.vde = true;
	      		return true;
	      	}
		}
		
		var formSubmit = false;
		var fst = null;
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
		   		if(formSubmit){
		   			formSubmit = false;
		   			return true;
		   		}
		   		//验证所有表单
		   		fThis.validate();
	   			beforeSubmit({
		   			arr:arr,
		   			jqForm:jqForm,
		   			options:options,
		   			form:window['#'+id]
		   		})
		   		var sis = System.getComponent('#'+id).isSubmit;
		   		var validateBool = (typeof sis == 'undefined')?true:sis;
		   		clearTimeout(fst);
		   		if(!formSubmit){
		   			if(fThis.$subtn){
		   				clearTimeout(fThis.$subtn.st);
		   			}
		   			fst = setTimeout(function(){
				   		//alert(validateBool+'=='+self.find('.has-error').length);
				   		if(validateBool&&self.find('.has-error').length<1){
				   			//禁用提交按钮
				   			fThis.$subtn = self.find('[type="submit"]');
				   			fThis.$subtn.attr('data-loading-text','loading...');
				   			fThis.$subtn.button('loading');
				   			//System.showLoading();
				   			validateSuccess({
					   			arr:arr,
					   			jqForm:jqForm,
					   			options:options,
					   			form:window['#'+id]
					   		});
				   			formSubmit = true;
				   			self.submit();
				   		}else{
				   			formSubmit = false;
				   		}
			   		},200)
		   		}
		   		return formSubmit;
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
		   		fThis.$btn.button('reset');
		   	},
		   	complete:function(xhr){
		   		fThis.$subtn.st = setTimeout(function(){
		   			fThis.$subtn.button('reset');
		   		},300)
		   		complete(xhr);
		   		System.hideLoading();
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
		   	resetForm: resetForm          	//成功提交后，重置所有表单元素的值
		   	///timeout: 60000               	//限制请求的时间，当请求大于3秒后，跳出请求
		}  
		
		var st = setInterval(function(){
			if(self.ajaxForm){
				clearInterval(st);
				self.ajaxForm(options);
			}
		},10)
		
		self.find('[validate]').change(function(){
			try{	
				fThis.validate($(this)[0]);
			}catch(e){}
		})
		
		window['#'+id] = self;
		//显示错误提示
		window['#'+id]['setValidateError'] = function(dom,txt){
			var dom = self.find('[name="'+$(dom).attr('name')+'"]')[0];
//			var fvalidate = window[dom.getAttribute('validate')]||null;
//			if(fvalidate){
				if(dom){
					var result = txt;
					if(result.length>0){
						//选择
						if(dom.type.toLowerCase()=="checkbox"||dom.type.toLowerCase()=="radio"){
				      		fThis.validateTxt($(dom).parent(),result);
						}else{
							//输入
							if(dom.getAttribute('component')+"".Trim()=='dropDownSearch'){
								fThis.validateTxt($(dom).parent(),result);
							}else{
								fThis.validateTxt(dom,result);
							}
			      		}
					}
				}
//			}
		}
		window['#'+id]['clearValidateError'] = function(dom){
			var jqd = self.find('[name="'+$(dom).attr('name')+'"]');
			jqd.parents('.has-error').find('.help-block').remove();
			jqd.parents('.has-error').removeClass('has-error');
		}
		//表单单个控件验证
		window['#'+id]['validate'] = function(dom){
			try{
				fThis.validate($(dom)[0]);
			}catch(e){}
		}
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
			return serializeObj(self);
		}
		
		//清空文本表单，用于表格重置
		window['#'+id]['clearForm'] = function(){			
		       $("#"+id+" input[type !='button']").val("");		    
		}
		
		//清空表单组件
		var cst = null;
		window['#'+id]['clear'] = function(bool){
			System.showLoading();
			if(typeof bool == 'undefined'){
				bool = true;
			}
			clearTimeout(cst);
			//清空所有
			if(bool){
				//组件
				self.find('[component]').each(function(i){
					var that = $(this);
					var id = that.attr('id');
					var com = that.attr('component');
					var sgc = System.getComponent('#'+id);
					if(com == 'input'||com == 'dateTime'){
						that.val('');
					}else if(com == 'checkRadio'||com == 'checkBox'){
						sgc.setData('');
					}else if(com == 'chosen'||com == 'dropDownSearch'){
						sgc.setData('');
					}else if(com == 'editor'){
						sgc.execCommand('cleardoc');
					}else if(com == 'file'){
						that.val('');
						$('#filelist-file_'+id+' li[data-json]').remove();
						$('#filelist-file_'+id+' li.imglist-addli').css({position:'relative',zIndex:0,bottom:0});
					}
				})
				//非组件
				self.find('input[type!="button"]').each(function(){
					if(typeof $(this).attr('component')=='undefined'){
						$(this).val('');
					}
				})
			}else{
				//组件
				self.find('[component]').each(function(i){
					var that = $(this);
					var id = that.attr('id');
					var com = that.attr('component');
					var validate = that.attr('validate')||that.find('[validate]').attr('validate');
					var sgc = System.getComponent('#'+id);
					if(com == 'input'||com == 'dateTime'){
						if(that.attr('validate')!=null){
							that.val('');
						}
					}else if(com == 'checkRadio'||com == 'checkBox'){
						sgc.setData('');
					}else if(com == 'chosen'||com == 'dropDownSearch'){
						if(that.attr('validate')!=null){
							sgc.setData('');
						}
					}else if(com == 'editor'){
						if(that.attr('validate')!=null){
							sgc.execCommand('cleardoc');
						}
					}else if(com == 'file'){
						if(that.attr('validate')!=null){
							that.val('');
						}
						$('#filelist-file_'+id+' li[data-json]').remove();
						$('#filelist-file_'+id+' li.imglist-addli').css({position:'relative',zIndex:0,bottom:0});
					}
				})
				//非组件
				self.find('input[type!="button"][validate]').each(function(){
					$(this).val('');
				})
			}
			cst = setTimeout(function(){
				self.find('.has-error').removeClass('has-error');
				self.find('.error.help-block').hide();
			},300)
			System.hideLoading();
		}
		
		//重置表单
		window['#'+id]['reset'] = function(callback){
			callback = callback||function(){}
			System.showLoading();
			var idlist = System.getComponentsIds();
			//清空表单下所有组件的id
			self.find('[component]').each(function(){
				var that = $(this);
				var id = that.attr('id');
				if(idlist.indexOf(id)!=-1){
					if(idlist[idlist.indexOf(id)].length==id.length){
						//从队列中移除
						window.componentsIds[idlist[idlist.indexOf(id)]] = '';
						idlist.splice(idlist.indexOf(id),1); 
					}
				}
			})
			self.stop().animate({
				opacity:0
			},1000,function(){
				self.after(self.data('html'));
				self.remove();
				self = $('#'+id);
				self.data('html',self[0].outerHTML);
				callback(self);
				//如果有layout
				if(self.find('[component="layout"]').length>0){
					System.initComponent(self.find('[component="layout"]'),'layout',function(){
						System.hideLoading();
					})
				}else{
					var cl = self.find('[ready-component]').length+self.find('[component]').length;
					var c = 0;
					//重新渲染所有ready-component组件
					self.find('[ready-component]').each(function(){
						var _this = $(this);
						var componentName = _this.attr('ready-component');
//						componentName = componentName.substring(0, 1).toUpperCase() + componentName.substring(1, componentName.length);
						System.initComponent(self.find('[component="'+componentName+'"]'),componentName,function(){
							c++;
							if(cl>=c){
								System.hideLoading();
							}
						})
					})
					//重新渲染所有component组件
					self.find('[component]').each(function(){
						var _this = $(this);
						var componentName = _this.attr('component');
//						componentName = componentName.substring(0, 1).toUpperCase() + componentName.substring(1, componentName.length);
						System.initComponent(self.find('[component="'+componentName+'"]'),componentName,function(){
							c++;
							if(cl>=c){
								System.hideLoading();
							}
						})
					})
				}
			})
		}
		
		
		window['#'+id]['resetTable'] = function(callback){
			callback = callback||function(){}
			System.showLoading();
			var idlist = System.getComponentsIds();
			//清空表单下所有组件的id
			self.find('[component]').each(function(){
				var that = $(this);
				var id = that.attr('id');
				if(idlist.indexOf(id)!=-1){
					if(idlist[idlist.indexOf(id)].length==id.length){
						//从队列中移除
						window.componentsIds[idlist[idlist.indexOf(id)]] = '';
						idlist.splice(idlist.indexOf(id),1); 
					}
				}
			})
			self.stop().animate({
				opacity:0
			},1000,function(){
				self.after(self.data('html'));
				self.remove();
				self = $('#'+id);
				self.data('html',self[0].outerHTML);
				callback(self);
				//如果有layout
				var cl = self.find('[ready-component]').length+self.find('[component]').length;
				var c = 0;
			
				//重新渲染所有component组件
				self.find('[component]').each(function(){
					var _this = $(this);
					var componentName = _this.attr('component');
					System.initComponent($(this),componentName,function(){
						c++;
						if(cl>=c){
							System.hideLoading();
						}
					})
				})
			})
			
		}
		
		//表单自定义序列化	
		var serializeObj = function(form){
	      	var $form = (typeof(form)=="string" ? $("#"+form) : form);
	      	var dataArray =  $form.serializeArray();
	     	var result={};
	      	$(dataArray).each(function(){
	          	//如果在结果对象中存在这个值，那么就说明是多选的情形。
	      		//console.log(result)
	      		var _this = this;
	      		var list = [];
	      		//获取当前表单控件元素
            	var element = $form.find("[name='"+ _this.name +"']")[0];
        		//获取当前控件类型
            	var type = ( element.type || element.nodeName ).toLowerCase();
	        	//如果控件类型为多选那么值就是数组形式，否则就是单值形式。
	        	var value = (/^(select-multiple|checkbox)$/i).test(type) ? [_this.value] : _this.value;
	        	value = (value+"").Trim();
	      		if(result[_this.name]){
	      			//return;
	          	}else{
	          		result[_this.name] = [];
//		        	alert((/^(select-multiple|checkbox)$/i).test(type) ? [_this.value] : _this.value);
	          	}
	      		if(value!=""){
	      			result[_this.name].push(value);
	      		}
	      	});
	      	var njs = {};
	      	for(var i in result){
	      		njs[i] = result[i].join(',');
	      	}
	      	return njs;
	  	};
		
		setTimeout(function(){
			onload(window['#'+id],self);
			System.setReady(self);//告诉base我已加载完成 
		},300)
	    return self;
	}
})(jQuery)
$(function(){
	$('[component="form2"]').each(function(){
		$(this).Form2();
	})
})
