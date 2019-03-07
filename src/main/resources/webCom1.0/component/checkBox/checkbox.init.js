/**
 * 功能：单选和多选
 */
;
(function($){
	
	$.fn.CheckBox = function(i){		
		var self = $(this);
		var not_frist = false;		
		var id = self.attr('id')||'checkbox_'+new Date().getTime()+''+i; 		
		var checkbox_color=self.attr("checkbox-color")||'success';
		var c_callback = self.attr("checkBox-callback");
		var callback = window[c_callback];
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		window['#'+id] = self;
		if(typeof callback == 'undefined'){
			callback = function(){};
		}
		
		var html = [];
		//添加input id 跟 label for对应
		self.find('input').each(function(i){
			var cid = $(this).attr('id')||self.attr('id')+'_cid_'+i;
			$(this).attr('id',cid);
			$(this).next('label').attr('for',cid);
			var box = $('<div class="checkbox checkbox-'+checkbox_color+' checkbox-inline"></div>');
			$(this).clone(true).appendTo(box);
			$(this).next('label').clone(true).appendTo(box);
			html.push(box);
		})
		
		self.html(html);
		
		/*根据组件节点获取checkbox*/
		function checkbox(parentId){
			var checkList = $("#"+parentId+" input[type='checkbox']");
			function getData(){
				var mycheckList = [];
				for(var i = 0;i<checkList.length;i++){
					var bool = false;
					if(checkList.eq(i).attr('checked')||checkList.eq(i)[0].checked){
						bool = true;
					}
					
					mycheckList.push({
						componentId:parentId,
						id:checkList.eq(i).attr('id'),
						name:checkList.eq(i).attr('name'),
						bool:bool,
						value:checkList.eq(i).val()
					});	
				}
				return mycheckList;
			}
			
			callback(getData());
			
			/**
			 * 功能：复选框点击事件
			 */		
			checkList.click(function(){		
				callback(getData());
			})
		}
		//初始化调用
		checkbox(id);
		
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="checkBox"]').each(function(i){
		$(this).CheckBox(i);		
	})
})
