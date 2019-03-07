/**
 * 功能：单选和多选
 */
;
(function($){	
	$.fn.CheckRadio = function(i){
		var not_frist = false;		
		var self = $(this);
		var id = self.attr('id')||'radio_'+new Date().getTime()+''+i;
		var radio_color = self.attr("checkRadio-color")||'success';
		var r_callback =  self.attr("checkRadio-callback");
		var callback = window[r_callback];
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
			var box = $('<div class="radio radio-'+radio_color+' radio-inline"></div>');
			$(this).clone(true).appendTo(box);
			$(this).next('label').clone(true).appendTo(box);
			html.push(box);
		})
		
		self.html(html);
		
		//取input值		
		$("#"+id+"  input[type='radio']").click(function(){
			callback({"componentId":id,"id":$(this).attr('id'),"value":$(this).val()});
		})
		
		function getChcekedDom(){
			return $("#"+id+"  input[type='radio']:checked");
		}
		
		callback({"componentId":id,"id":getChcekedDom().attr('id'),"value":getChcekedDom().val()});
		
		System.setReady(self);//告诉base我已加载完成 
	}	
})(jQuery)
$(function(){	
	$('[component="checkRadio"]').each(function(i){
		$(this).CheckRadio(i);
	})
})
