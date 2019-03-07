;
(function($){
	$.fn.DropDownTree = function(i){
		var rid = new Date().getTime()+''+i;
		/*接收参数开始*/
		var self = $(this);
		var id = self.attr('id')||'dropDownSearch_'+rid;
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		
		//存储旧dom结构
		self.oldDom = self[0].outerHTML;
		
		var inputId = 'inputId'+rid;
		var buttonId = 'buttonId'+rid;
		self.html('');
		var setting;
		window['#'+id] = self;
		/*接收参数结束*/
		
		/*设置样式开始*/
		self.addClass("input-group");
		var width = self.attr("dropDwonTree-width")||(self.width()==0?'100%':self.width());
		var dncolor = self.attr("dropDwonTree-color")||'#676A6C';
		var dataList = self.attr('dropDwonTree-dataList');
		var dropUrl = self.attr('dropDwonTree-dataUrl');
		var keyField = self.attr('dropDwonTree-keyField')||'';
		var dataType = self.attr("dropDwonTree-dataType")||"2";
		var checkChildren = self.attr("dropDwonTree-checkChildren")||"false";//是否只选中子节点，排除父节点
		var checkParent = self.attr("dropDwonTree-checkParent")||"false";//是否只选中父节点，排除子节点
		var tree_height = self.attr("tree-height")||"250";
		var radioType = self.attr("dropDwonTree-radioType")||"all";//radio 分组范围：同一级内level  整棵树内all
		var checkNodes = self.attr("data-checkNodes")||'[]';
		/*设置样式结束*/
		/*回调点击下拉树选项函数开始*/
		var r_callback =  self.attr("drop-callback");
		var callback = window[r_callback];
		if(typeof callback == 'undefined'){
			callback = function(){};
		}
		/*回调点击下拉树选项函数结束*/
		
		/*回调选中复选框开始*/
		var d_callback = self.attr("drop-getCheck");
		var getCheckNodes = window[d_callback];
		if(typeof getCheckNodes == 'undefined'){
			getCheckNodes = function(){};
		}
		/*获得第一次节点选中*/
		var getFirstNode=self.attr('getFirstNode-callback');
		var getFirstNodes = window[getFirstNode];
		if(typeof getFirstNodes == 'undefined'){
			getFirstNodes = function(){};
		}
		
		/*搜索回调*/
		var s_callback = self.attr("search-callback");
		var searchBack = window[s_callback];
		if(typeof getCheckNodes == 'undefined'){
			searchBack = function(){};
		}
		
		/*回调选中复选框结束*/
		
		/*样式封装开始*/
		self.css({
			"width":width,
			"color":dncolor,
			"position":"relative"
		})
		
		/*输入框开始*/
		var $input,$group,$button,$ul,$treeDiv = null;
		$input = $('<input type="text" class="form-control"  id="'+inputId+'" readonly><span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>');
		self.append($input);		
		$group = $('<div class="input-group-btn"></div>');
		self.append($group);
		$button = $('<button type="button" id="'+buttonId+'" class="btn btn-white dropdown-toggle" data-toggle="dropdown"></button>');
		$group.append($button);
		$button_text = $('<span class="caret"></span>');
		$button.append($button_text);
		$ul = $('<ul class="dropdown-menu dropdown-menu-right" role="menu"></ul>');
		$group.append($ul);
		/*输入框结束*/
		
		/*下拉树*/
		var treeId = self.attr("dropDowntreeId")||'treeId_'+rid;
		var tree_Class = 'treeClass'+rid;
		$treeDiv = $('<div id='+treeId+' class="'+tree_Class+'"></div>');
		$treeDiv.css({
			"position":"absolute",
			"top":self.outerHeight(),
			"left":0,
			"min-width":width+"px",
			"max-height":tree_height+"px",
			"overflow":"auto",
			"z-index":"9999",
			"display":"none",
			"background-color":"#fff",
			"border":"medium none",
			"box-shadow": "0 0 3px rgba(86, 96, 117, 0.3)"
		})
		self.append($treeDiv);
		/*样式封装结束*/
	
		/*初始化树*/
		function tree_init(){
			//1.出现下列树
			//1.1获取树类型,等于1，说明是json获取，等于2说明是url获取
			if(dataType=='1'){
				var setting = returnSetting("",1);
		        data_tree(setting);
			}else if(dataType=='2'){
				var setting = returnSetting({"Y": "s", "N": "ps"});		        		        
		        data_tree(setting);	
			}else if(dataType=='3'){
				var setting = returnSetting({"Y": "p", "N": "ps"});
				data_tree(setting);	
			}else if(dataType=='4'){
				var setting = returnSetting({"Y": "ps"});		        		        
		        data_tree(setting);	
			}else if(dataType=='5'){
				var setting = returnSetting({"Y": "", "N": "ps"});	
				 setting.check.chkStyle = "radio";  //单选框
				 setting.check.radioType = radioType; 
		        data_tree(setting);
			}
		
		}
		
		/**
		 * 配复选置
		 */
		function returnSetting(checkSet,type){
			if(type == undefined){
				var setting = {
		            view: {		              
		                selectedMulti: false
		            },
		            check: {
		                enable: true,
		                chkStyle:"checkbox", // 添加生效
						chkboxType : checkSet,//Y:勾选（参数：p:影响父节点），N：不勾（参数s：影响子节点）[p 和 s 为参数]
		            },
		            data: {
		                simpleData: {
		                    enable: true
		                }
		            },
		           
		            callback:{
						onCheck: get_onCheck,
						onClick:get_onClick
					}
		        };
			}else{
				var setting = {
		            view: {
		                selectedMulti: false
		            },
		
		            data: {
		                simpleData: {
		                    enable: true
		                }
		            },
		            callback: {
		            	onClick:onClick
		            }
		       };
			}
			 return setting;
		}
		
		var treeObj = null;
		/*生成树*/
		function data_tree(setting){
			if(self.attr('dropDwonTree-dataList')!=undefined){
				treeObj = $.fn.zTree.init($("#"+treeId),setting,eval(self.attr('dropDwonTree-dataList')));
				self.DefaultSelect();
			}else if(self.attr('dataUrl')!=undefined){
				 setting.callback.onAsyncSuccess =  tree_myselect;
				 setting.async = {
				 					enable: true,
									url:""+self.attr("dropUrl"),
									type:"get",
									autoParam:["id", "name=n", "level=lv"],
									otherParam:{"otherParam":"zTreeAsyncTest"},
									dataFilter: filter
								}
				 $(document).ready(function(){
						$.fn.zTree.init($("#"+treeId), setting);
				});
			}
			$treeDiv.addClass('ztree');
		}
		
		
		/**
		 * 异步加载回调
		 */
		function tree_myselect(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj(id);
			var newTreeObjId = JSON.parse(msg)[0].id;
//			var node = treeObj.getNodeByParam("id", newTreeObjId, null);
			 getFirstNodes(node);
//			treeObj.selectNode(node);
			$input.val(node.name);
		}
		/*第一次选中*/
		self.DefaultSelect = function(){
			if(checkNodes!=undefined&&checkNodes!=""&&checkNodes!=null){
				var treeObj =  $.fn.zTree.getZTreeObj(treeId);
				var nodes = treeObj.getNodes();
				var fristCheckMy_node=[];
				var myCheck = eval('('+checkNodes+')')||[];
				var selectStr = [];
				for(var i=0;i<myCheck.length;i++){
					var node = treeObj.getNodeByParam("id", myCheck[i].id, null);
					if(node){
						fristCheckMy_node.push(node);
						treeObj.checkNode(node, true,false);
					}
					selectStr.push(myCheck[i].name||node.name);
				}
				getCheckNodes(fristCheckMy_node);
				$input.val(selectStr.join(';'));
				getFirstNodes(fristCheckMy_node);
			}else{
		       // var node = treeObj.selectNode(nodes[0]);
		      //  getFirstNodes(nodes[0]);
		        $input.val("请选择...");
		       // fristCheckMy_node.push(nodes[0]);
			}
			
		}
		
		/**
		 * 根据id选中
		 */
		self.selectCustomNode = function(selectId){
			var treeObj =  $.fn.zTree.getZTreeObj(treeId);
			var node = treeObj.getNodeByParam("id", selectId, null);
			treeObj.selectNode(node);
			$input.val(node.name);
		}
		
		/**
		 * 返回select数据
		 */
		self.getSNode = function(){
			var treeObj1 =  $.fn.zTree.getZTreeObj(treeId);
        	return treeObj1.getSelectedNodes();
		}
		
		/**
		 * 获取但前点击子级所有的父节点对象数据
		 */
		var nd = [];
		function getAllParentsNodes(treeNode,callback,pId){
			if(!pId){
				nd = [];
				nd.push(treeNode);
			}
			if(treeNode.pId){
				var node = treeObj.getNodeByTId(treeNode.parentTId);
				nd.push(node);
				getAllParentsNodes(node,callback,treeNode.pId);
			}else{
				callback = callback||function(){};
				callback(nd);
			}
		}
		
		self.getAllParentsNodes = function(treeNode,callback){
			getAllParentsNodes(treeNode,callback);
		}
		
		/*功能开始*/
		/*选中点击时候触发开始*/
		function onCheck1(e, treeId, treeNode){
        	var checktree =  $.fn.zTree.getZTreeObj(""+treeId);
			var checknodes = checktree.getCheckedNodes();
			getCheckNodes(checknodes);
        };
		/*选中点击时候触发结束*/
		
		/*选中时候事件*/
		function get_onCheck(e, treeId, treeNode){
			var checktree =  $.fn.zTree.getZTreeObj(""+treeId);
			var checknodes = checktree.getCheckedNodes();
			var list = [];
			var str = "";
			for(var i = 0;i<checknodes.length;i++){
				if(checkChildren == 'true'){
					if(!checknodes[i].isParent){
						str+=checknodes[i].name+';';
						list.push(checknodes[i]);
					}
				}else if(checkParent == 'true'){
					if(checknodes[i].isParent){
						str+=checknodes[i].name+';';
						list.push(checknodes[i]);
					}
				}else{
					str+=checknodes[i].name+';';
					list.push(checknodes[i]);
				}
			}
			$input.val(str);
			getCheckNodes(list);
		}
		
		
		/*过滤开始*/
		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		}
		/*过滤结束*/
		
		/*改变选中背景色*/
		/*function changeBgColor(className){
			
		  $(document).click(function (e) {
		       
				$('#'+id+" ."+className).removeClass(''+className);
				$(".curSelectedNode").addClass(''+className);
		   })	
		}*/

		/*生成树*/
//		function data_tree(setting,data){
//			console.log(data);
//	        $.fn.zTree.init($("#"+treeId),setting,data);        
//		}

		/* 选中添加方法*/
		window['#'+id].addCheckNodes = function(obj){
			//获取选中ID
     		var treeObj =  $.fn.zTree.getZTreeObj(""+id);
        	var nodes = treeObj.getCheckedNodes();
        	
        	//{id:(100 + i), pId:nodes[i].id, name:"new node"}
     		//使用添加方法
     		for (var i=0, l=nodes.length; i < l; i++) 
			{
				obj.id = rid;
			    obj.pId =  nodes[i].id;
			    //删除选中的节点
				treeObj.addNodes(nodes[i],obj);
				//treeObj.addNode(nodes[i]);
			}
		}
		
		/*选中删除方法*/
		self.removeCheckNodes = function(){
			var treeObj =  $.fn.zTree.getZTreeObj(""+treeId);
        	var nodes = treeObj.getCheckedNodes();
        	for (var i=0, l=nodes.length; i < l; i++) 
			{
			    //删除选中的节点
				treeObj.removeNode(nodes[i]);
			}
		}
		
		/*搜索方法*/
		self.search = function(searchVal){
			//console.log(JSON.stringify(setting));
			//console.log(setting);
			/*通过后台接口获取数据*/
			$.ajax({
				type:"get",
				url:"../../../webCom1.0/component/dropDownTree/js/treeData2.json?searchval"+searchVal,
				success:function(data){
					//重新生成树
					//$.fn.zTree.init($("#"+treeId),setting);
					data_tree(setting,data)
					$treeDiv.show(500,searchBack(data));
				}
			});		
		}
		
		//刷新树方法
		self.reload = function(){
			var idd = self.attr('id');
			System.removeComponentsIds(idd);
			self.after(self.oldDom);
			self.remove();
			System.initComponent('#'+idd);
		}

		/* 获取点击选中的数据*/
		function onClick(event, treeId, treeNode, clickFlag) {
			if(!treeNode.nocheck){
				//修改选中值
				$("#"+inputId).val(treeNode.name);
				$("#"+treeId).hide();
				//回调
				callback(treeNode);
			}
		}
		/* 获取点击选中的数据*/
		function get_onClick(event, treeId, treeNode, clickFlag) {
			callback(treeNode);
		}
	 /*功能结束*/	
		tree_init();	
		
		$(document).on('click',function(e){
			System.stopBubble();
			if(inputId!=e.target.id){
				if(!$(e.target).parents('.'+tree_Class).hasClass(tree_Class)&&!$(e.target).hasClass(tree_Class)){
					$("#"+treeId).hide();
				}
			}
		})
		
		/*点击输入框事件*/
		$(document).on('click','#'+inputId+',#'+buttonId,function(){
			var _this = $(this);
			$('[component="dropDownTree"]').each(function(){
				if(this == _this.parents('[component="dropDownTree"]')[0]){
					$treeDiv.toggle();
				}else{
					$(this).find('.ztree').hide();
				}
			})
		})
	
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)
$(function(){
	$('[component="dropDownTree"]').each(function(i){		
		$(this).DropDownTree(i);
	})
})

