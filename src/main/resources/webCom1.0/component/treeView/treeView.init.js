/**
 * 功能：树形视图插件
 */
;
(function($){
	
	$.fn.TreeView = function(){
		/*定义接收参数开始*/
		var self = $(this);
		var id = self.attr('id')||'radio_'+new Date().getTime();
		self.attr('id',id);
		if(System.getComponentsIds().indexOf(id)!=-1){
			if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
				System.setReady(self);//告诉base我已加载完成 
				return;	
			}
		}
		System.setComponentsId(id);
		var dataType = self.attr('dataType')||'1';
		var clickClass = self.attr('clickClass')||'testColor';
		var treeNodeClick = self.attr('click-callback');
		var getcheckNodes = self.attr('tree-getNodes');
		var getFirstNode=self.attr('getFirstNode-callback');
		
		/*回调方法*/
		var get_checkNodes = window[getcheckNodes];
		if(typeof get_checkNodes == 'undefined'){
			get_checkNodes = function(){};
		}
		var getClickData = window[treeNodeClick];
		if(typeof getClickData == 'undefined'){
			getClickData = function(){};
		}
		var getFirstNodes = window[getFirstNode];
		if(typeof getFirstNodes == 'undefined'){
			getFirstNodes = function(){};
		}
		//var checkedkList;
		self.addClass('ztree');
		window['#'+id] = self;
		/*定义接收参数结束*/
		
		
		
		/**
		 * 功能:生成树
		 * @param {Object} dataType
		 * dataType 根据配置的dataType值生成不同的树
		 */
		function tree_init(dataType){
			if(dataType	== 1){
				var setting = returnSetting("",1);
		        data_tree(setting);
		       		        
			}else if(dataType == 2){
				var setting = returnSetting({"Y": "s", "N": "ps"});
		        data_tree(setting);		        

			}else if(dataType == 3){
				var setting = returnSetting({"Y": "s", "N": "ps"});
				data_tree(setting);	
			}else if(dataType == 4){
				var setting = returnSetting({"Y": "p", "N": "ps"});		        		        
		        data_tree(setting);	
						
			}else if(dataType == 5){
				var setting = returnSetting({"Y": "ps"});		        		        
		        data_tree(setting);	
			}else if(dataType == 6){
				var setting = returnSetting({"Y": "", "N": "ps"});		        		        
		        data_tree(setting);
			}else if(dataType == 7){
				var setting = returnSetting("",2);
		        data_tree(setting);
			}

		}
		/**
		 * 功能定义
		 */
		 var newCount = 1;
		
//		  function addHoverDom(treeId, treeNode) {
//          var sObj = $("#" + treeNode.tId + "_span");
//          if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
//          var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
//              + "' title='add node' onfocus='this.blur();'></span>";
//          sObj.after(addStr);
//          var btn = $("#addBtn_"+treeNode.tId);
//          if (btn) btn.bind("click", function(){
//          	//这里可以添加弹出框
//              var zTree = $.fn.zTree.getZTreeObj(""+id);
//              zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new node" + (newCount++)});
//              return false;
//          });
//	     };
//	        
//	     function removeHoverDom(treeId, treeNode) {
//	            $("#addBtn_"+treeNode.tId).unbind().remove();
//	     };
		
		 function onCheck1(e, treeId, treeNode){
	        	var checktree =  $.fn.zTree.getZTreeObj(""+id);
    			var checknodes = checktree.getCheckedNodes();        			
				get_checkNodes(checknodes);
		 };		 
		
		function onClick(e, treeId, treeNode,clickFlag){
			getClickData(treeNode);
			var treeObj =  $.fn.zTree.getZTreeObj(treeId);
			treeObj.checkNode(treeNode, !treeNode.checked, true);
			onCheck1(e, treeId, treeNode);
		}
		
		function onClick1(e, treeId, treeNode){
			getClickData(treeNode);
		}
		
		
		/**
		 * 改变背景
		 */
//		function changeBgColor(className){		
//		  $(document).click(function (e) {		       
//				$('#'+id+" ."+className).removeClass(''+className);
//				$(".curSelectedNode").addClass(''+className);
//		   })	
//		}
		
		/**
		 * 判断传入的配置，生成树
		 */
		
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
						onCheck: onCheck1,
						onClick:onClick
					}
		        },zTree;
			}else{
				if(type=="2"){
					var setting = {
						check: {
							enable: true,
							chkStyle: "radio",
							radioType: "all"
						},
						data: {
							simpleData: {
								enable: true
							}
						},		           
			            callback:{
							onCheck: onCheck1,
							onClick:onClick
						}
					}
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
			       //self.DefaultSelect();
				}
				
			}
			 return setting;
		}
		
		/*生成树*/
		function data_tree(setting){
			var treeList = [];
			if(self.attr('tree-Url')!=undefined){
				//发送ajax
				$.ajax({
					type:"get",
					url:""+self.attr('tree-Url'),
					success:function(data){
						var treeData = data.data;
						if(data.respCode =="0000"){
							//等到根节点的值
							var rootData = {id:treeData.id,pId:0,name:treeData.username}; 
							treeList.push(rootData);
							//取节点
							if(treeData.childrens!=undefined){
								for(var i =0;i<treeData.childrens.length;i++){
									var childrensData = {id:treeData.childrens[i].cID,pId:treeData.id,name:treeData.childrens[i].cUsername};
									treeList.push(childrensData);
									//findHasNodes(childrensData);
									if(treeData.childrens[i].c!=undefined){
										var node = treeData.childrens[i];
										for(var j = 0;j<node.c.length;j++){
											var nodelist = {id:node.c[j].cID,pId:node.cID,name:node.c[j].cUsername}
											treeList.push(nodelist);
											findHasNodes(node.c[j]);
										}
									}
								}
							}
							$.fn.zTree.init($("#"+id),setting,treeList);
							self.DefaultSelect();
						}
						
						//递归
						function findHasNodes(thisNode){
							if(thisNode==undefined||thisNode.c==undefined){   
								return;   
							}else{
								if(thisNode.c!=undefined){
									var node = thisNode;
									for(var j = 0;j<node.c.length;j++){
										var nodelist = {id:node.c[j].cID,pId:node.cID,name:node.c[j].cUsername}
										treeList.push(nodelist);
										findHasNodes(node.c[j])
									}
								} 
							}   
						}  
						
					}
				});
			}else{
				if(self.attr('dataUrl')!=undefined){
				setting.callback.onAsyncSuccess =  tree_myselect;
				 setting.async = {
				 					enable: true,
									url:""+self.attr("dataUrl"),
									type:"get"
								 }
				
				 $(document).ready(function(){
						$.fn.zTree.init($("#"+id), setting);
				 });
				
				}else if(self.attr('dataList')!=undefined){
					console.log(self.attr('dataList'));
					$.fn.zTree.init($("#"+id),setting,eval(self.attr('dataList')));
					self.DefaultSelect();
				}
			}
			
			iconStylePos();
		}
		
		
		/**
		 * 异步加载回调
		 */
		function tree_myselect(event, treeId, treeNode, msg){
			var treeObj = $.fn.zTree.getZTreeObj(id);
			var newTreeObjId = JSON.parse(msg)[0].id;
			var node = treeObj.getNodeByParam("id", newTreeObjId, null);
			 getFirstNodes(node);
			treeObj.selectNode(node);
		}
		
		/**
		 * 选中添加方法
		 */
		window['#'+id].addCheckNodes = function(obj){
			//获取选中ID
     		var treeObj =  $.fn.zTree.getZTreeObj(""+id);
        	var nodes = treeObj.getCheckedNodes();
        	
        	//{id:(100 + i), pId:nodes[i].id, name:"new node"}
     		//使用添加方法
     		for (var i=0, l=nodes.length; i < l; i++) 
			{
				obj.id = new Date().getTime();
			    obj.pId =  nodes[i].id;
			    //删除选中的节点
				treeObj.addNodes(nodes[i],obj);
				//treeObj.addNode(nodes[i]);
			}
		}
		
		/**
		 * 选中删除方法
		 */
		self.removeCheckNodes = function(){
			var treeObj =  $.fn.zTree.getZTreeObj(id);
        	var nodes = treeObj.getCheckedNodes();
        	for (var i=0, l=nodes.length; i < l; i++) 
			{
			    //删除选中的节点
				treeObj.removeNode(nodes[i]);
				//console.log("选中删除节点名称"+nodes[i].name);
			}
		}
		
		/**
		 * 获取选中的数据
		 */
		self.get_allChecked = function(){
			var treeObj =  $.fn.zTree.getZTreeObj(""+id);
        	return treeObj.getCheckedNodes();
		}
		
		/**
		 * 返回select数据
		 */
		self.getSNode = function(){
			var treeObj1 =  $.fn.zTree.getZTreeObj(id);
        	return treeObj1.getSelectedNodes();
		}
		
		/*默认第一条选中*/
		self.DefaultSelect = function(){		
			var treeObj =  $.fn.zTree.getZTreeObj(id);
			var nodes = treeObj.getNodes();
			
			if (nodes.length>0) 
			{
		        var node = treeObj.selectNode(nodes[0]);
		        getFirstNodes(nodes[0]);
			}
		}
		
		
		/**
		 * 根据id选中
		 */
		self.selectCustomNode = function(selectId){
			var treeObj =  $.fn.zTree.getZTreeObj(id);
			var node = treeObj.getNodeByParam("id", selectId, null);
			treeObj.selectNode(node);
		}
		
		
		/**
		 * 功能:判断是否有图片属性，有则修改样式
		 */
/*		function ModifyIconStyle(obj){
			for(var i=0;i<obj.length;i++){
				if(obj[i].hasOwnProperty('icon')){
					//调整修改图标和文字的位置
				}
			}
		}*/
		
		//调用生成树方法
		tree_init(dataType);
		
		/**
		 * 修改图标位置样式
		 */
		function iconStylePos(){
			for(var i = 0;i<$(".ztree li span.button.ico_docu").length;i++){
				if($(".ztree li span.button.ico_docu").get(i).style.backgroundPosition!="")
				$(".ztree li span.button.ico_docu").get(i).style.backgroundPosition = "0 4px";
			}
		}
		
		System.setReady(self);//告诉base我已加载完成 
	}
})(jQuery)

$(function(){
	$('[component="treeView"]').each(function(){
		$(this).TreeView();
	})
})
