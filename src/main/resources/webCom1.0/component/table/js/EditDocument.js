/*生文档脚本的方法*/
/**
 * 
 * @obj =>{type:num,scriptId:[id1,id2],htmlId:[id3,id4],parentId:id}
 * 
 */
function editDocument (obj){
	var self = $(this);
	//创建HTML方法
	self.createHTML = function(HtmlObj){
		
	}
	
	//创建JS方法
	self.createJS = function(){
		//根据js获取节点
		var s = document.getElementById(''+obj.scriptId).innerHTML;
		s = s.replace(/</g,'&lt;');
		s = s.replace(/>/g,'&gt;');
		document.getElementById(''+obj.praentId)document.insertAdjacentHTML('beforeEnd', '&lt;script&gt;<br/>'+s+'&lt;/script&gt;<br/>');
		document.getElementById(''+obj.praentId).insertAdjacentHTML('beforeEnd', '&lt;script&gt;<br/>'+s+'&lt;/script&gt;<br/>');
	}
	
	//创建控制器
	self.controller = function(){
		if(obj.type=="1"){
			createHTML();
		}else{
			createJS();
		}
	}
	
	self.controller();
}
