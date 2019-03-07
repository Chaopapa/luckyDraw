(function($){
	//输入框change
	$.fn.InputChange = function(i){
		var self = $(this);
		var parentForm = null;
		if(self.parents('[component="form2"]').length>0&&typeof self.attr('validate') != 'undefined'){
			parentForm = self.parents('[component="form2"]').eq(0);
		}else if(typeof self.attr('onchange')=='undefined'){
			return;
		}
		var id = self.attr('id');
		if(typeof id == 'undefined'){
			id = 'inputchange_'+new Date().getTime()+i;
			self.attr('id',id);
		}
		var onchange = self.attr('onchange');
		if(typeof onchange != 'undefined'){
			self.attr('ionchange',onchange);
			self.removeAttr('onchange');
		}
		self.on("input propertychange",function(){  
		    var ionchange = self.attr('ionchange');
			if(parentForm){
				System.getComponent(parentForm).validate(self);
			}
			if(typeof ionchange != 'undefined'){
				eval(ionchange);
			}
		}) 
	}
})(jQuery)
$(function(){
	$('input,textarea').each(function(i){
		$(this).InputChange(i);
	})
})