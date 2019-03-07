(function($){
	$.fn.Chosen = function(){
		var self = $(this);
		var id = self.attr('id')||'chosen_'+new Date().getTime(); 
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		var nrt = self.attr('chosen-no-results-text');
		var placeholder = self.attr('chosen-placeholder')||'请选择';
		placeholder = self.attr('placeholder')||placeholder;
		var multiple = self.attr('multiple');
		var change = self.attr('chosen-change');
		self.attr('id',id);
		self = $('#'+id);
		self.attr('data-placeholder',placeholder);
		//如果是单选下拉
		if(typeof multiple == 'undefined'&&self.find('option').val()!=""){
			self.html('<option value="">'+placeholder+'</option>'+self.html());
		}
		if(typeof change != 'undefined'&&(change+'').Trim()!=''){
			change = window[change];
		}
		self.addClass('chosen-select');
		self.chosen({
			allow_single_deselect: true,
			disable_search_threshold: 10,
			no_results_text: nrt||'哎呀，没有发现！'
		});
		self.on('change', function(evt, params) {
			if(typeof change != 'undefined'&&(change+'').Trim()!=''){
		    	change(System.getComponent('#'+id).getData());
		   	}
	  	});
	  	$('#'+id+'_chosen').attr('style',self.attr('style')).show();
	  	
	  	window['#'+id] = self;
	  	window['#'+id]['getData'] = function() {
	  		var v = {};
			var keys = [],
				values = [],
				allKeys = [],
				allValues = [];
			for(var i = 0; i < self.find('option').length; i++) {
				if(self.find('option').eq(i)[0].selected) {
					keys.push(self.find('option').eq(i).val());
					values.push(self.find('option').eq(i).html());
				}
				allKeys.push(self.find('option').eq(i).val());
				allValues.push(self.find('option').eq(i).html());
			}
			v['cid'] = id;
			v['keys'] = keys;
			v['values'] = values;
			v['allKeys'] = allKeys;
			v['allValues'] = allValues;
			return v;
		};
		
		window['#'+id]['setData'] = function(keys) {
			self.find('option').removeAttr('selected');
			if((keys || "").Trim() != "") {
				var keylist = keys.split(',');
				for(var i in keylist) {
					if(self.find('option[value="' + keylist[i] + '"]')[0]){
						self.find('option[value="' + keylist[i] + '"]')[0].selected = true;
					}
				}
			}
			//重新渲染
			self.trigger("chosen:updated");
	  		self.trigger("change");
		}
	  	
	  	window['#'+id]['destroy'] = function(){
	  		self.chosen("destroy");
	  	}
	  	
	  	window['#'+id]['reload'] = function(){
	  		self.trigger("chosen:updated");
	  	}
	  	
	  	System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="chosen"]').each(function(){
		$(this).Chosen();
	})
})
