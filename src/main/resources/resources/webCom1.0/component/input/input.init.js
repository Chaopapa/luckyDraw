(function($){
	$.fn.Input = function(){
		var self = $(this);
		$(this).addClass('form-control');
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="input"]').each(function(){
		$(this).Input();
	})
	
})