/**
 * 功能：树形视图插件
 */
;
(function($){
	
	$.fn.Orgchart = function(i){
		/*定义接收参数开始*/
		var self = $(this);
		var id = self.attr('id')||'orgchart_'+new Date().getTime()+'_'+i;
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		
		var opts = self.attr('orgchart-opts')||'{}';
		opts = eval('('+opts+')');
		
		if(opts.url != ''){
			var base = {
				'data':opts.url
				,'type':opts.type||'get'
		      	,'ajaxURL': {children:opts.url}
		      	,'nodeContent': opts.nodeContent||'title'
		      	,'nodeId': opts.nodeId||'id'
		      	,'depth': '2'
		      	,'direction': 't2b'//方向    b2t|l2r|r2l,默认是t2b
		      	,'callback':window[opts.callback]||function(){}
		      	,'createNode': function($node, data) {
			        $node.on('click','.title,.content', function() {
			        	window[opts.callback](data);
			        });
		      	}
		    }
			
			//如果允许拖拽
			if(opts.draggable == true||opts.draggable == 'true'){
				var dg = {
			      	'draggable': true,
			      	'dropCriteria': function($draggedNode, $dragZone, $dropZone) {
			        	if($draggedNode.find('.content').text().indexOf('manager') > -1 && $dropZone.find('.content').text().indexOf('engineer') > -1) {
			          	return false;
			        	}
			        	return true;
			      	}
			    }
				base = $.extend(true,dg,base);
			}
			
			window['#'+id] = self.orgchart(base);
			
			window['#'+id].getData = function(){
				return self.orgchart('getHierarchy');
			}
		}else{
			System.alert('请配置orgchart-url');
		}
		
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)

$(function(){
	$('[component="orgchart"]').each(function(i){
		$(this).Orgchart(i);
	})
})