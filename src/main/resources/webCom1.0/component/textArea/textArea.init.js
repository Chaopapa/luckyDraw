(function($){
	$.fn.TextArea = function(){
		var self = $(this);
		var id = self.attr('id')||'file_'+new Date().getTime();
		self.attr('id',id);
		self = $('#'+id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		self.addClass('form-control');
		
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="textArea"]').each(function(){
		$(this).TextArea();
	})
	
})