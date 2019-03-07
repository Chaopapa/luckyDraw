(function($){
    $.fn.Editor = function(){
        var self = $(this);
        var id = self.attr('id')||'editor_'+new Date().getTime();
        self.attr('id',id);
        self = $('#'+id);
        if(System.getComponentsIds().indexOf(id)!=-1){
            if(System.getComponentsIds()[System.getComponentsIds().indexOf(id)].length==id.length){
                System.setReady(self);//告诉base我已加载完成
                return;
            }
        }
        System.setComponentsId(id);
        var option = {
            autoHeightEnabled: true,
            autoFloatEnabled: false//编辑器工具条固定在顶部
        }
        var toolbars = self.attr('editro-toolbars')||"[['bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', 'fontfamily', 'fontsize','|', 'forecolor', 'backcolor', 'selectall','justifyleft','justifyright','justifycenter','justifyjustify','cleardoc'],['insertimage','fullscreen', 'source', 'undo', 'redo','emotion','|','inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts']]";
        var ready = self.attr('editor-ready');
        var disable = self.attr('editor-disabled')||'false';
        var width = self.innerWidth();
        var height = self.innerHeight();
        //解决由于富文本编辑器设置微软雅黑字体“"”跟“style="”冲突 20171031
        self.val(self.val().replace(/"(.*?);"/g,function(s){
            var str = s.replace(/"/g,"'");
            str = '"'+str.substring(1,str.length-1)+'"';
            return str;
        }));
        var html = '<script id="editor_'+id+'" type="text/plain" style="width:'+width+'px;height:'+height+'px;">'+self.val()+'</script>';
        self.hide();
        self.after(html);
        if(toolbars!=""){
            toolbars = eval(toolbars);
            option.toolbars = toolbars;
        }
        if(window['#'+id]){
            if(window['#'+id].ready){
                UE.getEditor('editor_'+id).destroy();
            }
        }
        ready = window[ready]||function(editor){};
        var ue = UE.getEditor('editor_'+id,option);
        ue.ready(function() {
            ready(ue);
            var setWords = function(){
                if(ue.hasContents()){
                    //解决由于富文本编辑器设置微软雅黑字体“"”跟“style="”冲突 20171031
                    self.val(ue.getContent().replace(/&quot;/g,"&#39;"));
                }else{
                    self.val("");
                }
                //如果是在新版的表单上使用则change
                if(self.parents('[component="form2"]').length>0){
                    self.trigger('change');
                }
                isFocus = false;
            }
            ue.addListener('selectionchange',function(){
                setWords();
            })

            var isFocus = false;
            ue.addListener('focus',function(){
                setWords();
                isFocus = true;
            })

//		 	self.on('focus',function(){
//		 		setWords();
//		    	isFocus = true;
//		 	})

            setInterval(function(){
                if(isFocus == false){
                    setWords();
                }
            },1000)

            if(disable == 'true'){
                ue.setDisabled('fullscreen');
            }
            System.setReady(self);//告诉base我已加载完成
        });

        window['#'+id] = ue;
    }
})(jQuery)

$(function(){
    $('[component="editor"]').each(function(){
        $(this).Editor();
    })
})
