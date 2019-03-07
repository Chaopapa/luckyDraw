;
(function($){
	$.fn.DropDownSearch = function(i){
		var self = $(this);
		var id = self.attr('id')||'dropDownSearch_'+new Date().getTime()+''+i;
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		var newSelf = $('<div class="input-group dropDownSearch" style="'+(self.attr('style')||'')+'" ></div>');
		self.after(newSelf);
		
		//设置样式
		var width = self.attr("dropDwon-width")||(self.width()==0?'100%':self.width());
//		var dncolor = self.attr("dropDwon-color")||'#676A6C';
		var list = self.attr('dropDwon-dataList');
		var url = self.attr('dropDwon-dataUrl');
		var keyField = self.attr('dropDwon-keyField')||'';
		var effectiveFields = self.attr('dropDwon-effectiveFields');
		var bsSuggestData = {
			indexId: 0,                     //每组数据的第几个数据，作为input输入框的 data-id，设为 -1 且 idField 为空则不设置此值
		    indexKey: 0,                    //每组数据的第几个数据，作为input输入框的内容
		    idField: "",                    //每组数据的哪个字段作为 data-id，优先级高于 indexId 设置（推荐）
		    showHeader: false,
		    keyField: keyField				//每组数据的哪个字段作为输入框内容，优先级高于 indexKey 设置（推荐）
		};
		//过滤需要显示的列 ,例如：userName,shortAccount
		if(effectiveFields){
			bsSuggestData.effectiveFields = effectiveFields.split(',');
		}
		if(typeof list == 'undefined'&&typeof url == 'undefined'){
			list=[{'description': '对应变量未定义'}];
		}
		
		var $input,$group,$button_text,$button,$ul = null;
		$input = $(self[0].outerHTML);
		self.remove();
		$input.addClass('form-control');
		$input.css("width",'100%');
		newSelf.append($input);	
		$input.after('<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>');
		self = newSelf;
		$group = $('<div class="input-group-btn"></div>');
		self.append($group);
		$button = $('<button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown"></button>');
		$group.append($button);
		$button_text = $('<span class="caret"></span>');
		$button.append($button_text);
		$ul = $('<ul class="dropdown-menu dropdown-menu-right" role="menu"></ul>');
		$group.append($ul);
		self = $('#'+id);
		
		/**
		 * 获取回调函数
		 */
		var d_callback =  self.attr("dropDownSearch-callback");
		var callback = window[d_callback];
		if(typeof callback == 'undefined'){
			callback = function(){};
		}
		
		function bsSuggest(){
			$input.on('change',function(){
				if($(this).val().Trim()==""){
					self.data('selectValue','');
				}
			})
			$input.bsSuggest(bsSuggestData).on('onDataRequestSuccess', function (e, result) {
	            //console.log('onDataRequestSuccess: ', result);
	        }).on('onSetSelectValue', function (e, keyword, data) {
//	            console.log(JSON.stringify(keyword));
				console.log('onSetSelectValue: ', keyword, data);
				self.data('selectValue',keyword);
				self.data('rowData',data);
				self.trigger('keyup');
	            callback(System.getComponent('#'+id).getData());
	        }).on('onUnsetSelectValue', function (e) {
	        	if(typeof self.data('selectValue') == 'undefined'){
	        		self.data('selectValue',self.val());
	        	}
	            //console.log("onUnsetSelectValue");
	        });
	        
	        System.setReady(self);//告诉base我已加载完成 
		}
		
		/**
		 * 功能：获取下拉列表的值
		 */
		if(typeof list != 'undefined'){
			bsSuggestData.data = {
				'value': eval(list),
               	'defaults': 'www.baidu.com'
			};
			bsSuggest();
		}
		
		/**
		 * 功能：根据url生成下拉框值
		 */
		if(typeof url != 'undefined'){
			bsSuggestData.url = url;
			bsSuggest();
		}
		window['#'+id] = self;
		window['#'+id]['getData'] = function(){
			var v = {
				cid: id
			};
			v['data'] = self.data('selectValue')||{};
			v['data']['rowData'] = self.data('rowData')||{};
			return v;
		}
		
		window['#'+id]['setData'] = function(value){
			self.val(value).trigger('keydown').trigger('keyup');
			var sv = self.data('selectValue');
			if(typeof sv!= 'string'&&value.Trim()!=''){
				self.trigger('click');
				self.next().find('ul').find('tr:eq(0)').trigger('mouseenter').trigger('mousedown');
				self.next().find('ul').hide();
			}
			if(value.Trim()==''){
				self.next().find('ul').hide();
			}
		}
	}
})(jQuery)
$(function(){
	$('[component="dropDownSearch"]').each(function(i){		
		$(this).DropDownSearch(i);
	})
})

