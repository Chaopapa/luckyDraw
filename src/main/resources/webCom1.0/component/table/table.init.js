/**
 * 表格插件
 */
;
(function($){
	$.fn.Table = function(i){
		//alert("进来");
		/* 初始化*/
		var mycurrentPage = 1;
		var self = $(this);		
		var Tableparams = {};
		var isRest = false;
		var reset = "";
		var hasData = true;
		window["table_tNumber"] = 1;
		var id = self.attr('id')||'table'+new Date().getTime()+''+i;
		var formInitData = "";
		if(self.attr("data-search-from")){
			var formId = self.attr("data-search-from");
			if($('#'+formId).length>0){
				formInitData = System.getComponent('#'+formId).getJSONParameters();
			}else{
				formInitData = {};
			}
		}
		//特殊情况，配置后台需要的导出功能
		self.attr("data-show-export","true");
		
		//修改分页配置
		self.attr("data-page-list","[5,10,25,50,100,250,500,1000]");
		
		self.attr('data-mobile-responsive',self.attr('data-mobile-responsive')||'true');
		
		var urlType = "post";
		self.attr("id",id)	
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		//存储原来的dom结构
		self[0].template = self[0].outerHTML;
		
		var tableUrl = self.attr('table-url')||"";
		//self.attr('data-resizable','true');//表格内容可伸缩
		var dataList;
		var dataType = self.attr("dataType");//表格的分页的类型。1代表服务器分页,2代表本地分页,3代表数据填充生成的表格
		var searchPage = self.attr("data-search-page")||'1';//查询表格数据时候返回的页数，默认为第一页,可选择‘local’则返回当前页
		var data_remove_callback = self.attr("data-remove-callback");
		var remove_callback = window[data_remove_callback];
		var data_refresh_callback = self.attr("data-refresh-callback");
		var refresh_callback = window[data_refresh_callback];
		var ajaxType = self.attr("data-ajaxType")||"json";
		var checkbox = self.attr("data-checkbox")||"false";	
		var data_add_callback = self.attr("data-add-callback");
		var add_addBack = window[data_add_callback];
		var searchCallback =  self.attr("data-search-callback");
		var onDblClickCellBack =  self.attr("data-double-click");
		var double_click = window[onDblClickCellBack];
		if( typeof double_click == 'undefined'){
			double_click = function(a,b,c,d){};
		}
		var tableForm = self.attr('data-search-from');
		var tableLoadStr = '';
		var showView = self.attr('data-show-curd')||'true';
		var showAdd = self.attr('data-show-add')||'true';
		var showIndex = self.attr('data-show-index')||'true';//是否显示序列号
		var toolbar = self.attr('data-toolbar')||null;
		searchCallback = window[searchCallback];
		if(typeof searchCallback=='undefined'){
			searchCallback= function(){};
		}
		if(typeof refresh_callback =='undefined'){
			refresh_callback= function(){};
		}
		/*回调函数*/
		if(typeof add_addBack=='undefined'){
			add_addBack = function(){};
		}
		if( typeof remove_callback == 'undefined'){
			remove_callback = function(){};
		}
		
		var loadback = self.attr("data-load-callback");
		var loadCallback = window[loadback];
		if(typeof loadCallback=='undefined'){
			loadCallback = function(){};
		}
		
		var tableNum = self.attr('tableNum')||'1';
		/*工具栏样式及效果封装开始*/		
		self.attr("tableNum",tableNum)
		var toolbarId = 'toolbarId'+new Date().getTime();
		$toolbar = $('<div id="'+toolbarId+'" style="position: relative;"></div>');
		self.before($toolbar);
		if(tableNum==1){
			if(showIndex == 'true' && dataType != '3'){
				self.find("thead").find("tr").eq(0).find("th").eq(0).before('<th data-field="tindex" data-width="70px" data-align="center" >序号</th>');
			}
			if(showView=='true'){
				/*是否有复选*/
				if(checkbox=="true"){
					//如果有自定义toolbar，则无需要data-show-curd
					if(toolbar != null){
						
					}else{
						//封装			
						var removeId = 'removeId'+new Date().getTime();
						var addId = 'addId'+new Date().getTime();
						var refreshId = 'refresh'+new Date().getTime();
						
						$button1 = $('<button id="'+removeId+'" type="button" class="btn btn-danger" disabled></button>');
						$toolbar.append($button1);			
						$button1.append('<i class="glyphicon glyphicon-remove"></i> 删除');
						if(showAdd=="true"){
							$button2 = $('<button id="'+addId+'" type="button" class="btn btn-primary" ></button>');
							$button2.css({
								"margin-left":"5px"
							})
							$toolbar.append($button2);
							$button2.append('<i class="glyphicon glyphicon-plus"></i> 添加');
						}
		
						self.attr("data-toolbar","#"+toolbarId);
						self.on('#'+id+' check.bs.table uncheck.bs.table ' +
			                'check-all.bs.table uncheck-all.bs.table', function () {
				            $("#"+removeId).prop('disabled', !self.bootstrapTable('getSelections').length);
				            /*测试调用选中*/
				            //console.log(self.checkInfo());
				        });
						 /*删除*/
				        $("#"+removeId).click(function () {
							self.remove();
				        });
				        /*添加*/
				        $("#"+addId).click(function () {
							self.add();
						});
						 /*刷新*/
				        $("#"+refreshId).click(function () {
							self.refresh();
						});
					}
					//插入id
					if(dataType!='3'){
						self.find("thead").find("tr").eq(0).find("th").eq(0).before('<th data-field="state" data-checkbox="true" data-width="20px"></th>');
					}
				}else{
					if(showAdd=="true"){
						//var toolbarId = 'toolbarId'+new Date().getTime();			
						var addId = 'addId'+new Date().getTime();			
						$button2 = $('<button id="'+addId+'" class="btn btn-primary" ></button>');
						$button2.css({
							"margin-left":"0.6em"
						})
						$toolbar.append($button2);
						$button2.append('<i class="glyphicon glyphicon-plus"></i> 添加');			
						
						self.attr("data-toolbar","#"+toolbarId);
						self.on('#'+id+' check.bs.table uncheck.bs.table ' +
			                'check-all.bs.table uncheck-all.bs.table', function () {
				            $("#"+removeId).prop('disabled', !self.bootstrapTable('getSelections').length);
				            /*测试调用选中*/
				            //console.log(self.checkInfo());
				        });
				       
				        /*添加*/
				        $("#"+addId).click(function () {
							self.add();
						});	
					}
				}
			}
		}
		tableNum++;
		self.attr("tableNum",tableNum);
		
		/*工具栏样式及效果封装结束*/
		if(self.attr("data-newButton")!=undefined){
			self.attr("data-toolbar","#"+toolbarId);
			var newButtonId = self.attr("data-newButton");
			var $newButtonDom = $(newButtonId).html();
			$("#"+toolbarId).append($newButtonDom);		
			$(newButtonId).remove();
		}
		
		
		/*添加分页设置*/
		//self.attr("data-side-pagination","client");
		self.attr("data-pagination","true");
		//fixed-table-container
	
		/**
		 * formatter封装
		 */
		
		self.find('[data-formatter]').each(function(){
			if(typeof $(this).attr('data-formatter-list')!='undefined'){
				window[$(this).attr('data-formatter')] = function(value,row){
					if(typeof this.formatterList !='undefined'){
						var formatterData = eval("("+this.formatterList+")");
						var str = '';
						for(var j=0;j<formatterData.length;j++){
							//console.log(value +'=='+ formatterData[j].key+"=="+(value == formatterData[j].key));
							if(value == formatterData[j].key){
								str = formatterData[j].value;
								break;
							}else{
								str = value;
							}
						}
						return str;
					}
				}
				window[$(this).attr('data-formatter')]['data-formatter-list'] = $(this).attr('data-formatter-list');
			}
		});
		
		/*定义获取self的方法*/
		window['#'+id] = self;
		if(tableForm!=undefined){
			if($('#'+tableForm).length>0){
				Tableparams = System.getComponent('#'+tableForm).getJSONParameters();
			}else{
				Tableparams = {};
			}
		}
		
		/*url拼接开始*/
 		var tableURLParams = "";
 		//获取参数
 		var urlParams = self.attr("data-url-params");
 		if( typeof urlParams !=undefined){
 			var urlParamsArray = eval("("+urlParams+")");
 			 for(var key in urlParamsArray ){
		        tableURLParams +="&"+key+"="+urlParamsArray[key];
		    }
 		}
 		if(tableUrl!=""){
 			if(tableUrl.indexOf("?") <= 0 ){
 			tableUrl=tableUrl+"?";
	 		}else{
	 			tableUrl=tableUrl+"&";
	 		}
 			
	 		if(tableURLParams){
	 			tableUrl +=tableURLParams+"&";
	 		}
 		}
 		
 		
 		/*url拼接结束*/
		
		
		if(tableNum=='2'){
			//过滤忽略列表
			var ignoreColumn = [];
			self.find('[data-field]').each(function(i){
				var _this = $(this);
				if(_this.attr('data-export') == 'false'||_this.text().Trim()==''){
					ignoreColumn.push(i);
				}
			})
			
			self[0].ignoreColumn = ignoreColumn;
			
			if(dataType=='1'){
			self.attr("data-side-pagination","server");
			serverPagination();
			}else if(dataType=='2'){
				clientPagination();
			}else if(dataType=='3'){
				var data = eval("("+self.attr("data-dataList")+")");
				loadTable(data);
			}
		}
		
		
		
		/*服务端分页*/
		var sortParams = {};
		function serverPagination(){
			var pageCurrent = 1; 
			var searchVal = "";
		 	sortParams = $.extend(true,sortParams,Tableparams);
		 	if(self[0].parameter){
		 		sortParams = $.extend(true,sortParams,self[0].parameter);
		 	}
		 	$('#'+id).bootstrapTable({
			 	ajax:function(params){
			 		pageCurrent = (params.data.offset/params.data.limit)+1;
			 		searchVal = params.data.search;
//			 		alert(JSON.stringify(Tableparams));
			 		//alert(tableUrl+"pageCurrent="+pageCurrent+"&pageSize="+params.data.limit+"&searchVal="+params.data.search);
			 		var pars = "pageNumber="+pageCurrent+"&pageSize="+params.data.limit+"&searchVal="+params.data.search;
			 		var pars2 = tableUrl.split('?')[1];
			 		var url = tableUrl.split('?')[0];
			 		var parsJson = System.urlToJson(pars);
			 		var pars2Json = System.urlToJson(pars2);
			 		parsJson = $.extend(true,parsJson,pars2Json);
			 		Tableparams.size = parsJson.size = params.data.limit;
			 		pars = System.jsonToUrl(parsJson);
			 		$.ajax({
					 	type:urlType,
					 	url:url+'?'+pars,
					 	data:(JSON.stringify(sortParams) == '{}'?Tableparams:sortParams),
					 	dataType:""+ajaxType,
					    //dataType: "jsonp",
						jsonp: "callbackparam",
					 	success:function(data){
					 		dataList = data.data;
					 		//alert(dataList.list.length)
					 		//alert(""+tableUrl+"pageNumber="+pageCurrent+"&pageSize="+params.data.limit+"&Tableparams="+JSON.stringify(Tableparams))
					 		//debugger;
					 		if(data.respCode == "0000"){
					 			//储存总条数用来导出所有excel使用
					 			self[0].total = data.data.totalPages*data.data.size;
					 			
					 			self.parent().css("height","100%");
					 			hasData = true;
					 			tableLoadStr = "操作成功";		
					 			for(var i = 0;i<dataList.list.length;i++){
					 				console.log(params.data.limit)
						 			dataList.list[i].tindex = (pageCurrent-1)*params.data.limit+i+1;
						 		}					 			
					            params.success({
					                total: dataList.total,
					                rows:dataList.list
					            });
					            
					 		}else if(data.respCode == "0001"){
					 			tableLoadStr = "缺少参数";
					 		}else if(data.respCode == "0002"){
					 			tableLoadStr = "参数不正确";
					 		}else if(data.respCode == "0404"){
					 			tableLoadStr = "找不到资源";
					 		}else if(data.respCode == "0204"){
					 			hasData = false;
					 			//debugger;
					 			tableLoadStr = "数据为空";
					 			if(parseInt(self.parent().css("height"))>41){
					 				self.parent().css("height","82px");
					 			}else{
					 				self.parent().css("height","100%");
					 			}
					 			self.parent().parent().css("padding-bottom","0");
					 			self.parent().parent().find(".fixed-table-pagination").css("display","none");
					 		}else if(data.respCode == "1111"){
					 			tableLoadStr = "操作失败";
					 		}else if(data.respCode == "0403"){
					 			tableLoadStr = "没有权限";
					 		}else if(data.respCode == "0101"){
					 			tableLoadStr = "业务错误提示";
					 		}
					 		self.parent().find(".fixed-table-loading").html(data.respMsg);
					 		
					 		if(data.respCode!="0000"){
					 			//debugger;
					 			//alert(111)
					 			var th_len = self.find("thead tr th ").size();
					 			if(urlType=="post"){
					 				if((self.find("tbody").find(".no-records-found").find("td").html()!="")||self.attr("data-classes")){
					 					self.find("tbody").find(".no-records-found").find("td").html(data.respMsg);
							 			if(self.attr("data-classes")){
							 				self.parent().find(".fixed-table-loading").css("z-index","-1");
							 			}
							 			if(self.parent().find(".fixed-table-loading").height()>=36||self.parent().find(".fixed-table-loading").height()==0){
							 				self.find("tbody").html('<tr class="no-records-found"><td colspan="'+th_len+'">'+data.respMsg+'</td></tr>');					 								 			
							 				self.parent().find(".fixed-table-loading").css("z-index","-1");	
								 			self.parent().css("height","36px");
					 					}	
							 		}
					 			}else{
					 				//self.parent().find(".fixed-table-loading").html("");
//					 				if(self.attr("data-classes")=="table table-no-bordered"){
//					 					//self.parent().find(".fixed-table-loading").html("");
//					 					self.find("tbody").html('<tr class="no-records-found"><td colspan="'+th_len+'">'+data.respMsg+'</td></tr>');					 					
//							 			self.parent().find(".fixed-table-loading").css("z-index","-1");	
//					 				}
					 			}
					 			
								loadCallback(null);	
					 		}					 		
					 		if(parseInt(self.parent().find(".fixed-table-loading").css('height'))>=41){
								self.parent().find(".fixed-table-loading").css("line-height",parseInt(self.parent().find(".fixed-table-loading").css('top'))*2+'px');
							}else{
								self.parent().find(".fixed-table-loading").css("line-height",parseInt(self.parent().find(".fixed-table-loading").css('top'))+'px');
							}
							
					 		if(JSON.stringify(sortParams) == '{}'){
					 			//清除所有排序样式
								var bts = self.parents('.bootstrap-table');
								bts.find('.sortable.desc').removeClass('desc');
								bts.find('.sortable.asc').removeClass('asc');
					 		}
						}
					});
				},
				onLoadSuccess:function(){
					if(hasData){
						self.parent().parent().find(".fixed-table-pagination").css("display","block");
					}else{
						self.parent().find(".fixed-table-loading").html(tableLoadStr);
						self.parent().parent().find(".fixed-table-pagination").css("display","none");
					}
					urlType = "post";
					modifyPageStyle()
					loadCallback(dataList);	
				},
				onRefresh:function(){
					var status = true;
					if(self.bootstrapTable('getSelections').length!=0&&self.bootstrapTable('getSelections').length!=""){
						status = false;
					}
					if(!status){
						$("#"+removeId).prop('disabled', true);
					}
					refresh_callback(dataList);
				},
				onSearch:function(){
					searchCallback(searchVal);
				},
				onDblClickCell:function(field,value,row,td){
					//alert(value);
					double_click(field,value,row,td);
				}
				//列表排序功能
				,onSort:function(sortName,sortType){
					sortParams['sortName'] = sortName;
					sortParams['sortType'] = sortType;
					serverPagination();
				}
		 	});
		 
		}
		
		/*本地分页*/
		function clientPagination(){
			var pageCurrent = 1; 
			var searchVal = "";
			 $('#'+id).bootstrapTable({
			 	ajax:function(params){
			 		//console.log("params:"+JSON.stringify(params));		 		
			 		var searchVal = params.data.search;		
			 		if(tableUrl.indexOf("?") <= 0 ){
			 			tableUrl=tableUrl+"?";
			 		}else{
			 			tableUrl=tableUrl+"&";
			 		}
				 	$.ajax({
					 	type:"post",
					 	url:""+tableUrl+"pageNumber="+pageCurrent+"&pageSize="+params.data.limit+"&searchVal="+params.data.search,
					 	dataType: ""+ajaxType,
					 	data:Tableparams,
					    //dataType: "jsonp",
					   // data:self.getParameter(),
						jsonp: "callbackparam", 
					 	success:function(data){
					 		dataList = data.data;
					 		//console.log(data);
					 		if(data.respCode == "0000"){
					 			tableLoadStr = "操作成功";
					 			self.bootstrapTable('load',dataList.list);
						 		for(var i = 0;i<dataList.list.length;i++){
						 			dataList.list[i].tindex = (pageCurrent-1)*params.data.limit+i+1;
						 		}						 		
					            params.success({
					                total: dataList.total,
					                rows:dataList.list
					            });
						      	modifyPageStyle();
						      	for(var i=0;i<$(".search input").size();i++){					      		
						      		if($(".search input").eq(i).attr("id")==undefined){
						      				$(".search input").eq(i).attr("id",id+"_searchId");
							      			return;
						      		}
						      	}
						      	
					 		}else if(data.respCode == "0001"){
					 			tableLoadStr = "缺少参数";
					 		}else if(data.respCode == "0002"){
					 			tableLoadStr = "参数不正确";
					 		}else if(data.respCode == "0404"){
					 			tableLoadStr = "找不到资源";
					 		}else if(data.respCode == "0204"){
					 			tableLoadStr = "数据为空";
					 		}else if(data.respCode == "1111"){
					 			tableLoadStr = "操作失败";
					 		}else if(data.respCode == "0403"){
					 			tableLoadStr = "没有权限";
					 		}else if(data.respCode == "0101"){
					 			tableLoadStr = "业务错误提示";
					 		}
					 		self.parent().find(".fixed-table-loading").html(data.respMsg);
					 		self.parent().find(".fixed-table-loading").css("line-height",parseInt(self.parent().find(".fixed-table-loading").css('top'))*2+'px');
					 		
					 		if(data.respCode != "0000"){
					 			loadCallback(null);	
					 		}
						}
					});
				},
				onLoadSuccess:function(data){
					loadCallback(dataList);
					//console.log(self.parent().find(".fixed-table-loading"));					
				},
				onRefresh:function(data){
					$("#"+removeId).prop("disabled",true);				     
					refresh_callback(dataList);
				},
				onSearch:function(){
					searchCallback($("#"+id+"_searchId").val());
				},
				onDblClickCell:function(field, value,row,td){
					double_click(field, value,row,td);
				}
		 	});	 
			 
		}
		self.parent().find(".fixed-table-loading").html(tableLoadStr);
		
		/**
		 * 修改分页宽高
		 */
		function modifyPageStyle(){
			//给分页添加快捷输入分页框
			var num = dataList.pageNumber;
			self.parents('.bootstrap-table').find('.fixed-table-pagination .pagination-detail').append('<span class="page-fast">前往<input type="text" class="form-control" value="" />页</span>')
			
			$('.bootstrap-table .page-fast input').on('blur',function(){
				var _this = $(this);
				if(_this.val().Trim() != num){
					$("#"+id).bootstrapTable('selectPage',_this.val());
				}
			})
			
			if(self.parents('.bootstrap-table').find('.pagination .page-number.active').length == 0){
				self.parents('.bootstrap-table').find('.pagination .page-number').each(function(){
					var _this = $(this);
					console.log(_this.text());
					if(_this.text() == num){
						_this.addClass('active');
					}
				})
			}
			
			/*表格宽高修改*/
			if(self.attr("data-height").indexOf("%")!="-1"){
				self.parent().parent().addClass("tablespacing");
				return;
			}
		}

		/**
		 * 刷新
		 */
		self.refresh = function(){			
			self.bootstrapTable('refresh');
		}	

		/*拿到选中*/
		self.checkInfo = function() {
	          return self.bootstrapTable('getSelections');
    	}		
		/*隐藏列*/
		self.hideColumn = function(column){
			self.bootstrapTable('hideColumn', ''+column);
		}

		//删除
		self.remove = function(){
			var rowData = [];
			var ids = $.map(self.bootstrapTable('getSelections'), function (row) {
				rowData.push(row);
                return row.id;
          });
            remove_callback(rowData);          
		}
		
		//添加
		self.add = function(){
			add_addBack();
		}
		
		//重置
		self.reset = function(){
			//清空列排序
			sortParams = {};
			hasData = true;
			isRest = true;
 			self.parent().css("height","100%");
			//alert(JSON.stringify(Tableparams))
 			//self.refresh();
 			self.bootstrapTable('selectPage', 1);
			
		}
		
		//快速版搜索
		self.quickSearch = function(formId){
			//清空列排序
			sortParams = {};
			//console.log("进来")
			var id = formId!=undefined?formId:self.attr('data-search-from');
			var val = System.getComponent('#'+id).getJSONParameters();
			self.setParameter(val);
		}
		//快速版搜索
		self.quickInitSearch = function(){
			self.setParameter(formInitData);
		}
		
		
		//快速版重置
		self.quickReset = function(formId){
			//清空查询条件
			Tableparams = {};
			sortParams = {};
			var id = formId!=undefined?formId:self.attr('data-search-from');
			//调用重置表单方法
			System.getComponent('#'+id).resetTable(function(){
				setTimeout(function(){
					System.hideLoading();
				},400)
				//重置搜索样式
				f_initSearchHTML();
				if(formInitData){
					self.quickInitSearch();
				}else{
					self.quickSearch();
				}				
				//重置
				hasData = true;
				isRest = true;
	 			self.parent().css("height","100%");
				//self.reset();
			});			
		}
		
		//导出指定条数的数据
		self.tableExport = function(option,callback){
			callback = callback||function(){}
			var txid = 'tableExport'+Math.floor(Math.random()*10000000);
			var ttemplate = $('<div id="panel'+txid+'" style="opacity:0;overflow:hidden;height:0;">'+$('#table')[0].template+'<div>');
			$('body').append(ttemplate);
			var table = ttemplate.find('table');
			table.attr('id',txid);
			var turl = table.attr('table-url');
			var id = self.attr('data-search-from');
			var val = System.getComponent('#'+id).getJSONParameters();
			var setting = {
				url:turl,
				type:'xlsx',//预留字段
				fileName:(document.title||self.parents('body').find('.ibox-title').text()||'xlsx报表').Trim(),
				pageNumber:1,
				pageSize:self[0].total
			};
			setting = $.extend(true,setting,option);
			var data = {
				pageNumber:setting.pageNumber,
				pageSize:setting.pageSize
			}
			data = $.extend(true,val,data);
			table[0].parameter = data;
			var turl2 = turl+(turl.indexOf('?')==-1?'?':'&')+'pageNumber='+setting.pageNumber+'&pageSize='+setting.pageSize;
			$('#'+txid).attr({
				'table-url':turl2,
				'data-page-number':setting.pageNumber,
				'data-page-size':setting.pageSize
			});
			//去除data-toolbar,防止工具条被替换了
			$('#'+txid).removeAttr('data-toolbar');
			System.showLoading();
			//alert(JSON.stringify(data));
//			alert(JSON.stringify(setting))
			$('#'+txid).on('load-success.bs.table', function () {
				System.hideLoading();
				$('#'+txid).tableExport({ type: setting.type, escape: false,
					ignoreColumn: self[0].ignoreColumn,  //忽略某一列的索引  
					fileName: setting.fileName,  //文件名称设置  
					//worksheetName: 'sheet1',  //表格工作区名称  
					//tableName: '总台帐报表',  
					//excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
		           	//onMsoNumberFormat: function(cell, row, col) {  
		           	//	var result = "";  
		           	//	if (row > 0 && col == 0)  
		           	//		result = "\\@";  
		           	//	return result;  
					//}
 				});
 				$('#panel'+txid).remove();
 				self[0].parameter = null;
 				callback(data);
			});
			System.initComponent('#'+txid,'table',function(){});
		}
		
		
		/**
		 * 设置表格参数
		 */
		var sst = null;
		self.setParameter = function(obj){
			Tableparams = $.extend(true,Tableparams,obj);
			//urlType = "post";
			//查询表格数据时候返回的页数，默认为第一页,可选择‘local’则返回当前页
			if(searchPage == '1'){
				//self.refresh();
				var allTableData = self.bootstrapTable('getData');//获取表格的所有内容行  
				if(allTableData.length>0){
					self.bootstrapTable('selectPage', 1);
				}else{
					self.refresh();
				}
			}
			if(searchPage == 'local'){
				self.refresh();
			}
		}
		
		self.getParameter = function(){
			return Tableparams;
		}

		/*加载表格数据,初始化序列,并生成表格(用于客户端分页)*/
		function loadTable(dataList){
			var columnsList = [];
			if(showIndex == 'true' && dataType == '3'){
				$("#"+id+" thead tr").prepend("<th></th>");
				columnsList.unshift({
			  		field: 'uid',
                    sortable: false,
                    sortOrder: "asc",
                    title: "序号",
                    width: 60,
                    align: 'center'
			  	});
			}
			if(checkbox=="true"){
				$("#"+id+" thead tr").prepend("<th></th>");
				columnsList.unshift({
                    field: 'state',
                    sortable: false,
                    sortOrder: "asc",
                    checkbox: true,
                    width: 40,
                    align: 'center'
               	});
			}

			dataList = dataList.data;
			if(showIndex == 'true' && dataType == '3'){
				for(var i = 0;i<dataList.list.length;i++){
			 		dataList.list[i].uid = i+1;
			 	}
			}
			
			self.bootstrapTable({
				columns:columnsList,
				data:dataList.list
			});
			
			modifyPageStyle();

		}
		if(self.attr('data-search').Trim()=="false"&&
			self.attr('data-show-refresh').Trim()=="false"&&
			self.attr('data-show-toggle').Trim()=="false"&&
			self.attr('data-show-columns').Trim()=="false"&&
			self.attr('data-show-export').Trim()=="false"){
			if(self.parents('.bootstrap-table').find('.fixed-table-toolbar .bs-bars.pull-left').length == 0){
				self.parents('.bootstrap-table').find('.fixed-table-toolbar').hide();
			}
		}
		System.setReady(self);//告诉base我已加载完成 
	}
	
})(jQuery)
$(function(){	
	$('[component="table"]').each(function(i){
		$(this).Table(i);		
	})
})