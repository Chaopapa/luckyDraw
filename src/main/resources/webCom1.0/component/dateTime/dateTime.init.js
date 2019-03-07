(function($){
	$.fn.DateTime = function(){
		var self = $(this);
		var id = self.attr('id')||'dateTime_'+new Date().getTime();	
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		self.addClass('form-control datatime');
		//判断是否有onchange事件，如果没有则添加一个空事件
		if(!self.attr('onchange')){
			self.attr('onchange','');
		}
		self.on('blur',function(){
			self.trigger('input');
		})
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="dateTime"]').each(function(){
		$(this).DateTime();
	})
})