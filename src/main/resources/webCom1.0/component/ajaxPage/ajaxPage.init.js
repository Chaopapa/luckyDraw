(function($){
	$.fn.AjaxPage = function(){
		var self = $(this);
		var id = self.attr('id')||'ajaxPage_'+new Date().getTime();
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		self = $('#'+id);
  		var complete = window[self.attr('ajaxPage-complete')]||function(){};
  		
    	var cc = self.find('[component]');
  		var firstLoad = 0;
  		var ids = {};
  		function loadingPage(){
  			var url = self.attr('ajaxPage-url')||'';
  			if(url==''){
  				return;
  			}
  			self.css({
				opacity:0
			})
  			self.removeAttr('componentstatus');
		   	System.rNum2 = 0;
		    
  			$.ajax({
				url: url, 
				dataType:'html',
				context: document.body, 
				success: function(data){
					window.componentsIds = {};
		        	self.html(data);
		        	cc = self.find('[component]');
					//检查是否有组件未生成，如果有则使其执行
		        	cc.each(function(){
		        		var $$ = $(this);
		        		var id = $$.attr('id');
		        		var componentName = $$.attr('component');
						
		        		//首字母大写对应插件init.js
						var nComoponentName = componentName.substring(0, 1).toUpperCase() + componentName.substring(1, componentName.length);
						System.initComponent($$,componentName);
					});
		      	},
		      	error:function(err){
		      		self.html('<div style="text-align:center;color:#cccccc;font-size:14px;">加载失败<br/>请检查ajaxPage-url是否可以访问<br/>ajaxPage不支持跨域访问</div>');
		      		console.log(err.statusText);
		      	},
		      	complete:function(){
		      		self.stop().animate({
		      			opacity:1
		      		},800,function(){
		      			complete();
		      			System.setReady(self);//告诉base我已加载完成 
		      			
		      			if(firstLoad>0){
		      				//重新定义rNum
			      			function iReady(){
								clearInterval(st);
								clearTimeout(dst);
								for(var i = 0 ; i < System.readyFuns.length; i ++){
									//判断如果ready内的方法已执行过了就不再执行
									if(System.readyFuns[i][1]==0){
										System.readyFuns[i][0]();
										System.readyFuns[i][1] = 1;
									}
								}
								System.rNum2 = 0;
								
								//重新收集id
								$('[component]').each(function(){
									System.setComponentsId($(this).attr('id'));
								})
							}
			      			var st = setInterval(function() {
	//								//判断所有的组件都有getComonent则表示都加载完成
								var rl = 0;
								cl = self.find('[component]').length;
								console.log(cl +"”"+ System.rNum2);
								if(cl == System.rNum2){
									iReady();
								}
							}, 10)
			      			
			      			//如果超过5秒还未执行iReady，则默认执行iReady
							var dst = setTimeout(function(){
								iReady();
							},5000)
		      			}
		      			firstLoad++;
		      		})
		      	}
			});
  		}
		loadingPage();
		window[id] = self;
		window[id]['reload'] = function(){
			loadingPage();
		}
		window[id]['load'] = function(url){
			self.attr('ajaxPage-url',url);
			loadingPage();
		}
	}
})(jQuery)
$(function(){
	$('[component="ajaxPage"]').each(function(){
		$(this).AjaxPage();
	})
})