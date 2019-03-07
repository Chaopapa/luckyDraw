(function($){
	$.fn.DateControl = function(){
		var self = $(this);
		var id = self.attr('id')||'date_'+new Date().getTime();	
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		//封装
		var dateType = self.attr("dateType")||'1';
		
		/**
		 * 回调方法
		 */
		var d_callback = self.attr("dateControl-callback");
		var callback = window[d_callback];
		if(typeof callback == 'undefined'){
			callback = function(){};
		}
		
		//根据不同的效果封装不同样式
		if(dateType!='5'){
			/**
			 * 封装
			 */		
			
			$('#'+id).find('div').eq(0).addClass('input-group date');
			$('#'+id+' input[type="text"]').addClass('form-control');			
			$('#'+id).find('div').eq(0).find('span').addClass('input-group-addon');
						
			if(dateType=='1'){
				/**
				 * 设置并回调
				 */
				  $('#'+id+' .input-group.date').datepicker({
		                todayBtn: "linked",
		                keyboardNavigation: false,
		                forceParse: false,
		                calendarWeeks: true,
		                autoclose: true,
		                format: "yyyy-mm-dd",
		               
		               
		          }).on('changeDate', function(e) {
						callback(formatDate(e.date));
		         });
			}else if(dateType=='2'){
				$('#'+id+' .input-group.date').datepicker({
	                startView: 2,
	                todayBtn: "linked",
	                keyboardNavigation: false,
	                forceParse: false,
	                autoclose: true
	            }).on('changeDate', function(e) {
						callback(formatDate(e.date));
		         });
			}else if(dateType=='3'){
				$('#'+id+' .input-group.date').datepicker({
                startView: 2,
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true
            	}).on('changeDate', function(e) {
						callback(formatDate(e.date));
		         });
			}else if(dateType=='4'){
				$('#'+id+' .input-group.date').datepicker({
	                minViewMode: 1,
	                keyboardNavigation: false,
	                forceParse: false,
	                autoclose: true,
	                todayHighlight: true
	            }).on('changeDate', function(e) {
						callback(formatDate(e.date));
		         });
			}
		
           callback($('#'+id+' input[type="text"]').eq(0).val());
		}else{
			/**
			 * 样式封装
			 */
			console.log(self.find('div').size());
			self.find('div').eq(0).addClass('input-daterange input-group');
			$('#'+id+' input[type="text"]').addClass('input-sm form-control');			
			self.find('div').eq(0).find('span').addClass('input-group-addon');
			
			/**
			 * 获取值
			 */
			var val1 = $('#'+id+' input[type="text"]').eq(0).val();
            var val2 = $('#'+id+' input[type="text"]').eq(1).val();			
            $('#'+id+' .input-daterange').datepicker({
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true
            }).on('hide', function(e) {
            		val1 = $('#'+id+' input[type="text"]').eq(0).val();
            		val2 = $('#'+id+' input[type="text"]').eq(1).val();
					callback({"start":''+val1,"end":''+val2});						
		       });
		       
		    callback({"start":''+val1,"end":''+val2});   
		}
		
		
		
		/**
		 * 格式化日期
		 */
		var formatDate = function (date) {  
		    var y = date.getFullYear();  
		    var m = date.getMonth() + 1;  
		    m = m < 10 ? '0' + m : m;  
		    var d = date.getDate();  
		    d = d < 10 ? ('0' + d) : d;  
		    return y + '-' + m + '-' + d;  
		};  
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	
	$('[component="dateControl"]').each(function(){
		$(this).DateControl();
	})
	
})