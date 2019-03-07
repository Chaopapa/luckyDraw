	function f_initSearchHTML(){
		var f_searchLenth =  $(".search-con").length;
		var TotalWidth = $("#searchTool").width();
		var ClearFixNum  =  1;
		var bodyInitWidth = $("body").width();
		//alert(11111111)
		//alert(bodyInitWidth)
		for(var i=0;i<f_searchLenth-1;i++){
			var m_search = $(".search-con").eq(i).find("lable").width();
			var total = $(".search-con").eq(i).width()-m_search;
			$(".search-con").eq(i).find("input").css({"width":total+"px","margin-left":m_search+"px"})
			$(".search-con").eq(i).find("select").css({"width":total+"px","margin-left":m_search+"px","position":"relative","margin-top":"-50px"})
			var bodyWidth = bodyInitWidth*ClearFixNum;
			var totalWidth = 240*(i+1);
			//alert("总高="+totalWidth+"bodyWidth="+bodyWidth)
			if(totalWidth>bodyWidth){
				//alert("jinlai")
				//alert($(".search-con").eq(i).html())
				//alert("总高="+totalWidth+"bodyWidth="+bodyWidth)
				$(".search-con").eq(i).before('<div class="clearfix"></div>');
				++ClearFixNum;			
			}
			if(i==5){
				if(TotalWidth>1600){
					$(".search-con").eq(i-1).after('<div class="clearfix"></div>');
				}
			}
			
			if(bodyInitWidth>1028&&bodyInitWidth<=1225){
				$(".search-con").eq(3).after('<div class="clearfix"></div>');
			}
			
//			if(bodyInitWidth>1200&&bodyInitWidth<1400){
//				$(".search-con").eq(4).after('<div class="clearfix"></div>');
//			}
			
		}
		
		var ls = $('.tablePanel.parts-none li').length;
		var l = 0;
		function cls(){
			l++;
			if(l>=ls){
				console.log('tablePanel.parts-none布局渲染结束');
				clearInterval(window.tblist);
			}
		}
		window.tblist = setInterval(function(){
			$('.tablePanel.parts-none li').each(function(){
				var _this = $(this);
				if(_this.attr('data-isinit')!='true'){
					if(_this.find('label').length>0){
						if(_this.find('label').width()>0){
							//console.log(_this.find('label').width());
							var pw = (_this.attr('parts-width')||250).toString().Trim();
							if(pw.indexOf('%')!=-1){
								pw = pw.substring(0,pw.length-1)/100;
								pw = ($(window).width()-_this.find('label').width()-40)*pw;
							}
							_this.css({
								width:_this.find('label').width()+pw*1+40,
								paddingLeft:_this.find('label').width()+10
							})
							_this.attr('data-isinit','true');
							cls();
						}
					}else{
						var pw = (_this.attr('parts-width')||250).toString().Trim();
						if(pw.indexOf('%')!=-1){
							pw = pw.substring(0,pw.length-1)/100;
							pw = $(window).width()*pw;
						}
						_this.css({
							width:pw,
							paddingLeft:0
						})
						_this.attr('data-isinit','true');
						cls();
					}
				}else{
				}
			})
		},300)
	}
	
	setTimeout(function(){
		f_initSearchHTML();
	},300)
