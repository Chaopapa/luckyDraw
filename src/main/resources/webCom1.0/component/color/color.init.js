(function(){
	$.fn.Color = function(){
		var self = $(this);
		var id = self.attr("id")||'colorId_'+new Date().getTime();
		self.attr("id",id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		self.colorpicker({
		    fillcolor:true
		});
		
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){	
	$('[component="color"]').each(function(){
		$(this).Color();		
	})
})