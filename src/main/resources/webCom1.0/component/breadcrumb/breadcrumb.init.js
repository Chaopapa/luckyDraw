(function($){
	$.fn.Breadcrumb = function(i){
		var self = $(this);
		var id = self.attr('id')||'breadcrumb_'+new Date().getTime()+'_'+i;
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		self = $('#'+id);
		
		//在父窗口添加
		if(this!=top){
			if(!top.breadcrumb){
				top.breadcrumb = {};
			}
		}
		var path = [];
		var win = null;
		//创建子父级根目录导航结构
		function createPath(o){
			var _title = o.title;
			var lw = window.location.href;
//			lw = lw.substring(0,lw.lastIndexOf('.'));
			var lw2 = document.referrer;
			//lw2 = lw2.substring(0,lw2.lastIndexOf('.'));
			if(!top.breadcrumb[lw]){
				top.breadcrumb[lw] = {
					level:top.breadcrumb[lw]?top.breadcrumb[lw].level:0,
					title:_title,
					parent:lw2,
					url:lw
				};
			}
			if(top.breadcrumb[lw2]){
				var l = top.breadcrumb[lw2].level;
				l++;
				top.breadcrumb[lw].level = l;
			}
		}
		
		//获取当前导航的数据
		function getPath(p){
			if(top.breadcrumb[p]){
				path.push(top.breadcrumb[p]);
				getPath(top.breadcrumb[p].parent);
			}
		}
		
		//清除当前页面的子级
		function clearChild(p){
			if(top.breadcrumb[p]){
				var p2 = top.breadcrumb[p].url;
				for(var i in top.breadcrumb){
					if(top.breadcrumb[i].parent == p2){
						clearChild(top.breadcrumb[i].url);
						delete top.breadcrumb[i];
					}
				}
			}
		}
		clearChild(window.location.href);
		
		//console.log(top.breadcrumb);
		createPath(document);
		
		getPath(window.location.href);
		
		var html = [];
		html.push('<ul class="breadcrumb">');
		if(this!=top){
			html.push('<li><a href="#"><img src="'+basePaths+'/css/skin/zhengxie/img/nav/icon-0-active.png"/></a> <span class="divider">></span></li>');
		}
		
		for(var i = path.length-1;i>-1;i--){
			html.push('<li><a title="'+path[i].title+'" '+(i!=0?'href=\''+path[i].url+'\'':'href=\'javascript:;\' style="cursor:default;"')+'>'+path[i].title+' <span class="divider">></span></li>');
		}
        html.push('</ul>');
		self.append(html.join(''));
		
//		var whh = window.location.href;
//		whh = whh.substring(0,whh.lastIndexOf('.'));
  		setTimeout(function(){
			System.setReady(self);//告诉base我已加载完成 
		},100)
	    return self;
	}
})(jQuery)
$(function(){
	$('[component="breadcrumb"]').each(function(i){
		$(this).Breadcrumb(i);
	})
})