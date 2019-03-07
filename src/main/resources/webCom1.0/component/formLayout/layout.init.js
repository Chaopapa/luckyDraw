(function(){
	$.fn.Layout = function(){
		var self = $(this);
		var id = self.attr('id')||'FormLayout'+new Date().getTime();
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		var html = [];
		self.find('td').each(function(){
			var tthis = $(this);
			if(tthis.find('label').eq(0).length>0){
				//有label
				tthis.find('label').eq(0).addClass('control-label');
				tthis.find('label').eq(0).before('&lt;div class="form-group">&lt;div class="x-row" style="overflow:visible;">&lt;div class="x-row-cols ell-label" style="width: 100px;text-align:right;padding-right:12px;">');
				tthis.find('label').eq(0).after('&lt;/div>&lt;div class="x-row-cols last-child" style="margin-left:100px;">');
				tthis.append('&lt;/div>&lt;/div>&lt;/div>');
			}else{
				//无label
				tthis.prepend('&lt;div class="form-group">');
				tthis.append('&lt;/div>');
			}
		})
		html.push('<div id="'+id+'" component="'+self.attr('component')+'" class="form-horizontal " style="'+(self.attr('style')||'')+'">');
		html.push('<div class="row" ><div class="col-md-10 col-md-offset-1" >');
		self.find('tr').each(function(i){
			var tr = $(this);
			var tr_id = tr.attr('id')||'';
			tr_id = tr_id==''?'':'id="'+tr_id+'"';
			var tr_style = tr.attr('style')||'';
			tr_style = tr_style==''?'':'style="'+tr_style+'"';
			html.push('<div class="row td mr0 '+(tr.attr('class')||'')+'" '+tr_id+' '+tr_style+'>');
			var tdl = tr.find('td').length;
			tr.find('td').each(function(i){
				var td = $(this);
				var td_id = td.attr('id')||'';
				td_id = td_id==''?'':'id="'+td_id+'"';
				var td_style = td.attr('style')||'';
				if(typeof td.attr('align') != 'undefined'){
					td_style+='text-align:'+td.attr('align');
				}
				td_style = td_style==''?'':'style="'+td_style+'"';
				html.push('<div '+td_id+' '+td_style+' class="col-sm-'+((12%tdl)==0?(12/tdl):12)+' '+(tr.find('td').attr('class')||'')+'">');
				html.push(td[0].outerHTML);
				html.push('</div>');
			})
			html.push('</div>');
		})
		html.push('</div></div>');
		self.after(html.join('').replace(/&lt;/g,'<').replace(/&gt;/g,'>'));
		self.remove();
		self = $('#'+id);
		//遍历里面所有的组件并生成
		self.find('[ready-component]').each(function(){
			var c = $(this);
			System.initComponent(c,c.attr('ready-component'),function(obj){
				obj.removeAttr('ready-component');
			});
			
		})
		function reset(){
			console.log(win.width());
			if(win.width()<761){
				self.find('.form-group .x-row .x-row-cols').addClass('block-label');
				self.find('.form-group .x-row .x-row-cols.last-child').addClass('block-input');
				self.find('.row.td').addClass('ml0');
			}else{
				self.find('.form-group .x-row .x-row-cols').removeClass('block-label');
				self.find('.form-group .x-row .x-row-cols.last-child').removeClass('block-input');
				self.find('.row.td').removeClass('ml0');
			}
		}
		var win = $(window);
		win.resize(function(){
			reset();
		})
		reset();
		
		//如果自己的父节点有form2则生成form表单组件
		self.parents('[ready-component="form2"]').each(function(){
			System.initComponent('#'+$(this).attr('id'),$(this).attr('ready-component'),function(obj){
				obj.removeAttr('ready-component');
				$('input,textarea').each(function(){
					$(this).InputChange();
				})
			});
		})
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)

$(function(){
	$('[component="layout"]').each(function(){
		$(this).Layout();
	})	
})


