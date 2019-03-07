(function(){
	$.fn.FormLayout = function(){
		/*接收参数*/
		var self = $(this);
		var id = self.attr('id')||'FormLayout'+new Date().getTime();
		var formLayout_type = self.attr('dataType')||'1';
		var hasSumbit = self.attr('hasSumbit')||'false';
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		
		/**
		 * 初始化布局
		 * layout_type：1表示一列布局,2表示两列布局
		 */
		function formLayout_init(layout_type){
			if(layout_type=='1'){
				//接收参数
				self.find('.tr_row').each(function(){
					var countNum = 12/parseInt($(this).attr("proportion"));
					$(this).find('.form_cols').each(function(){
						var $form_cols_con = $('<div class="col-lg-'+countNum+' col-md-'+countNum+' col-sm-'+countNum+' col-xs-12" style="margin-top:1em;"></div>')
						var $colshtml = $(this).html();
						$(this).html('');
						$form_cols_con.append($colshtml);
						$(this).append($form_cols_con);
					})
				})
				
			}else if(layout_type=='2'){				
				/*定义下标，用于计算*/
				
				var _index1 = 0;
				/*定义行*/
				var $row;
				
				/*一行一列开始*/
				self.find('.form_rows').each(function(){
					var name_type = $(this).attr("name-type")||'text';
					var name_text = $(this).attr("name-text");
					/*记下节点*/
					var $colshtml = $(this).html();
					$(this).html('');
					$row = $('<div class="row" style="margin-top:1em;">');
					var $row_con = $('<div class="form-group  col-lg-12 col-md-12 col-sm-12 col-xs-12" ></div>');
					$row.append($row_con);
					var $firstCols = $('<div class="col-lg-1 col-sm-2   col-md-1 col-xs-3 lable_LineHeight cols-width1"  style="margin-left:-4px" ></div>');
					$row_con.append($firstCols);
					var $lable = $('<label class="control-label pull-right " for="'+name_text+'" >'+name_text+'</label>');
					$firstCols.append($lable);					
					var $secondCols = $('<div class="col-lg-11 col-sm-10   col-md-11  col-xs-9 cols-width2"></div>');
					$row_con.append($secondCols);
					$secondCols.append($colshtml);
					$(this).append($row);
				})
				/*一行一列结束*/
				
				/*一行多列开始*/
				var _index = [0,0,0];
				self.find('.form_cols').each(function(){
					var me = $(this);
					var type = me.attr("special-type")||"text";
					if(type=="text"){
						/*得到计算参数*/
						var colsNum = parseInt(me.attr("colsNum"));				
						if(colsNum==2){
							cols(me,_index[0],colsNum);
							_index[0]++;	
						}else if(colsNum==3){
							deviation=-4;
							cols(me,_index[1],colsNum,deviation);
							_index[1]++;
						}else if(colsNum==4){
							deviation=1;
							cols(me,_index[2],colsNum,deviation);
							_index[2]++;
							
						}
					}else if(type=="button"){
							specialComponet(me);
					}
					
									
				})
				/*一行多列结束*/	
				
			}
		}
		

		/**
		 * 一列多行封装
		 * self：$(this)
		 * _index判断是否该增加row
		 * colsNum代表几列
		 * deviation左移距离，用来控制距离用
		 */
		function cols(self,_index,colsNum,deviation){
			var cols_takePlace = 12/colsNum;
//			if(name_type=='text'){
						/*接收参数*/
						if(_index%colsNum==0){
							$row = $('<div class="row"  id="rowId'+_index+'" ></div>');
						}
						var name_text = self.attr("name-text");
						var col_style =  self.attr("col-style");
						/*记下节点*/
						var $colshtml = self.html();
						self.html('');
						/*封装样式*/
						$firstCols = $('<div class="form-group  col-lg-'+cols_takePlace+' col-md-'+cols_takePlace+' col-sm-'+cols_takePlace+' col-xs-12 " style="margin-top:1em;"></div>');
						if(deviation!=undefined){
							$firstCols.css("margin-left",deviation+"px");
						}
						
						if(name_text){
							/*第一列封装*/
							$firstCols_div1 = $('<div class="col-sm-4 col-xs-3 col-lg-2 col-md-3 lable_LineHeight lable-width" ></div>');
							$firstCols_div1_child = $('<label class="control-label pull-right " for="'+name_text+'" >'+name_text+'</label>');
							$firstCols_div1.append($firstCols_div1_child);
							$firstCols.append($firstCols_div1);	
						}
						/*第二列封装*/
						
						$firstCols_div2 = $('<div class="col-sm-8  col-lg-10 col-md-9 col-xs-9 input-width" style="'+col_style+'"></div>');
						if(colsNum!=4){
							$firstCols_div2.removeClass('input-width');
						}
						$firstCols_div2.append($colshtml);
						$firstCols.append($firstCols_div2);
						/*加入行*/
						$row.append($firstCols);
						/*加入当生成*/
						self.append($row);
						
			//}
		}
		
		/**
		 * 特殊组件
		 */
		function specialComponet(self){
			var $buttonRow = $('<div class="row "></div>');
			var col_style =  self.attr("col-style")||"margin-top:1.5em;margin-bottom:1em;";
			$button_cols1 = $('<div class="form-group  col-lg-6 col-md-6 col-sm-6 col-xs-12 " style="'+col_style+'"></div>');
			$buttonRow.append($button_cols1);
			/*记下节点*/
			var $colshtml = self.html();
			self.html('');
			$button_cols2 = $('<div class="form-group  col-lg-6 col-md-6 col-sm-6 col-xs-12 " style="'+col_style+'"></div>');
			$buttonRow.append($button_cols2);
			$button_cols2_ch1 = $('<div class="col-sm-4 col-xs-3 col-lg-2 col-md-3 lable_LineHeight"></div>');
			$button_cols2.append($button_cols2_ch1);
			$button_cols2_ch2 = $('<div class="col-sm-8  col-lg-10 col-md-9 col-xs-9" style="position:relative;"></div>');
			$button_cols2.append($button_cols2_ch2);
			$button_cols2_ch2.append($colshtml);
			self.append($buttonRow);
		}
		
		
		/*生成布局*/
		 formLayout_init(formLayout_type);
		 System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)

$(function(){
	$('[component="formLayout"]').each(function(){
		$(this).FormLayout();
	})	
})


