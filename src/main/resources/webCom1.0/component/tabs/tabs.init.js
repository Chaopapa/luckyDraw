(function($){
	$.fn.Tabs = function(){
		var self = $(this);
		var id = self.attr('id')||'tabs_'+new Date().getTime();
		var dir = self.attr('tabs-dir')||'top';
		var index = self.attr('tabs-index')||'0';
		var beforeShow = self.attr('tabs-beforeShow')||'';
		var afterShow = self.attr('tabs-afterShow')||'';
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
  		
  		beforeShow = window[beforeShow]||function(){}
  		afterShow = window[afterShow]||function(){}
  		
  		if(index>self.find('li').length-1){
  			index = self.find('li').length-1;
  		}
  		
  		var html = '';
  		html+='<div id="'+id+'" component="'+self.attr('component')+'" style="'+(self.attr('style')||'')+'" class="tabs-container">';
  		html+='	<div class="tabs-'+dir+'">';
        html+='    <ul class="nav nav-tabs">';
        self.find('li').each(function(i){
        	if(index==i){
        		html+='        <li class="active"><a data-toggle="tab" href="#tab-'+id+'-'+i+'" aria-expanded="true">'+$(this).attr('tabs-title')+'</a>';
        	}else{
        		html+='        <li><a data-toggle="tab" href="#tab-'+id+'-'+i+'" aria-expanded="false">'+$(this).attr('tabs-title')+'</a>';
        	}
        })
        html+='        </li>';
        html+='    </ul>';
        html+='    <div class="tab-content">';
        self.find('li').each(function(i){
        	if(index==i){
		        html+='        <div id="tab-'+id+'-'+i+'" class="tab-pane fade in active">'+$(this).html()+'</div>';
	        }else{
	        	html+='        <div id="tab-'+id+'-'+i+'" class="tab-pane fade ">'+$(this).html()+'</div>';
	        }
        })
        html+='    </div>';
        html+=' </div><div style="display:block;width:100%;height:0;font-size:0;clear:both;"></div>';
        html+='</div>';
        self.after(html);
        self.remove();
		self = $('#'+id);
        
        self.find('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
        	var tg = e.target.hash;
		  	beforeShow({
		  		index:self.find('a[data-toggle="tab"][href="'+tg+'"]').parent().index(),
		  		title:self.find('a[data-toggle="tab"][href="'+tg+'"]').html(),
		  		tabCon:self.find('.tab-content '+tg)
		  	});
		})
        
        self.find('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        	var tg = e.target.hash;
		  	afterShow({
		  		index:self.find('a[data-toggle="tab"][href="'+tg+'"]').parent().index(),
		  		title:self.find('a[data-toggle="tab"][href="'+tg+'"]').html(),
		  		tabCon:self.find('.tab-content '+tg)
		  	});
		})
        
    	var tg = '#tab-'+id+'-'+self.find('.nav-tabs li.active').index();
        beforeShow({
        	index:self.find('a[data-toggle="tab"][href="'+tg+'"]').parent().index(),
	  		title:self.find('a[data-toggle="tab"][href="'+tg+'"]').html(),
	  		tabCon:self.find('.tab-content '+tg)
        })
        
        //遍历里面所有的组件并生成
		self.find('[ready-component]').each(function(){
			var c = $(this);
			System.initComponent(c,c.attr('ready-component'),function(obj){
				obj.removeAttr('ready-component');
			});
		})
		
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="tabs"]').each(function(){
		$(this).Tabs();
	})
})