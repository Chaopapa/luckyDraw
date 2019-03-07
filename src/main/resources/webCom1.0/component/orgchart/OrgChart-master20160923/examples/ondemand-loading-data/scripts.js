'use strict';

(function($){

  $(function() {
		$.mockjax(function(settings){
			console.log(JSON.stringify(settings));
			return {
	      response: function( origSettings ) {
	  		  this.responseText = { 'children': [
		        { 'id': '4', 'name': 'Pang Pang', 'title': 'engineer', 'relationship': '110' },
		        { 'id': '5', 'name': 'Xiang Xiang', 'title': 'UE engineer', 'relationship': '110'}
		      ]}
        }
      }
		})

//  $.mockjax({
//    url: '/orgchart/children/2/',
//    contentType: 'application/json',
//    responseTime: 1000,
//    response: function(settings) {
//		    // Investigate the `settings` to determine the response...
//				alert(JSON.stringify(settings));
//		    this.responseText = { 'children': [
//      	{ 'id': '4', 'name': 'Pang Pang', 'title': 'engineer', 'relationship': '110' },
//      	{ 'id': '5', 'name': 'Xiang Xiang', 'title': 'UE engineer', 'relationship': '110'}
//    	]}
//		  }
////    ,responseText: { 'children': [
////      { 'id': '4', 'name': 'Pang Pang', 'title': 'engineer', 'relationship': '110' },
////      { 'id': '5', 'name': 'Xiang Xiang', 'title': 'UE engineer', 'relationship': '110'}
////    ]}
//  });
//
//  $.mockjax({
//    url: '/orgchart/parent/1/',
//    contentType: 'application/json',
//    responseTime: 1000,
//    responseText: { 'id': '6','name': 'Lao Lao', 'title': 'general manager', 'relationship': '001' }
//  });
//
//  $.mockjax({
//    url: '/orgchart/siblings/1/',
//    contentType: 'application/json',
//    responseTime: 1000,
//    responseText: { 'siblings': [
//      { 'id': '7','name': 'Bo Miao', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '8', 'name': 'Yu Jie', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '9', 'name': 'Yu Li', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '10', 'name': 'Hong Miao', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '11', 'name': 'Yu Wei', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '12', 'name': 'Chun Miao', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '13', 'name': 'Yu Tie', 'title': 'department engineer', 'relationship': '110' }
//    ]}
//  });

//  $.mockjax({
//    url: '/orgchart/families/1/',
//    contentType: 'application/json',
//    responseTime: 1000,
//    responseText: {
//      'id': '6',
//      'name': 'Lao Lao',
//      'title': 'general manager',
//      'relationship': '001',
//      'children': [
//      { 'id': '7','name': 'Bo Miao', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '8', 'name': 'Yu Jie', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '9', 'name': 'Yu Li', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '10', 'name': 'Hong Miao', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '11', 'name': 'Yu Wei', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '12', 'name': 'Chun Miao', 'title': 'department engineer', 'relationship': '110' },
//      { 'id': '13', 'name': 'Yu Tie', 'title': 'department engineer', 'relationship': '110' }
//    ]}
//  });

    //relationship:000 3个参数配置说明，中文解释可以理解为是否有？0则无1则有
//  	第一位代表自己
//  	第二位代表同级
//  	第三位代表子级
    var datascource = {
      'id': '1',
      'name': 'Su Miao',
      'title': 'department manager',
      'relationship': '111',
    	'children': [
        { 'id': '2','name': 'Bo Miao', 'title': 'department engineer', 'relationship': '001' },
        { 'id': '3', 'name': 'Yu Jie', 'title': 'department engineer', 'relationship': '001' }
      ]
    };

//		以下ajax的不同方位的请求路径,以id作为索引传到后台去查询
    var ajaxURLs = {
      'children': '',
      'parent': '/orgchart/parent/',
      'siblings': '/orgchart/siblings/',//这个不知道什么时候使用
      'families': '/orgchart/families/' 
    };

    $('#chart-container').orgchart({
//    'data' : datascource,
      'ajaxURL': ajaxURLs,
      'nodeContent': 'title',
      'nodeId': 'id'
    });

  });

})(jQuery);