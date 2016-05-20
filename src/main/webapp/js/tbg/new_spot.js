$(document).ready(function(){
	$('#newTipArea').summernote({
	    lang: 'zh-CN',
	    height: 300,
	    minHeight: null,
	    maxHeight: null,
	    focus: true,
	    dialogsInBody: true,
        fontNames: ['Microsoft YaHei', '微软雅黑', '宋体', '楷体', '黑体', '隶书', 'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New'],
	    toolbar: [
	              ['fontname', ['fontname']],
	              ['style', ['bold', 'italic', 'underline', 'clear']],
	              ['fontsize', ['fontsize']],
	              ['color', ['color']],
	              ['para', ['ul', 'ol', 'paragraph']],
	              ['height', ['height']],
	              ['link', ['link']],
	              ['picture', ['picture']],
	              ['video', ['video']],
	              ['table', ['table']]
	             ],
	});
	
	$("#coverImgPreview").click(function(){
		document.getElementById('coverinputfile').click();
		
		$("#coverinputfile").change(function(){
			var data = new FormData();
			data.append('file', $('#coverinputfile')[0].files[0]);
			$.ajax({
				url:'/upload/image',
				type:'POST',
				data:data,
				cache: false,
				contentType: false,
				processData: false,
				success:function(d){
					var img = document.getElementById("coverImgPreview");
					img.src = "/resource/file/" + d;
				},
				error:function(){
					console.log("upload failed");
				}
			});
		});
	});
	
	$("#newImg").click(function(){
		document.getElementById('imageinputfile').click();
		
		$("#imageinputfile").change(function(){
			var data = new FormData();
			data.append('file', $('#imageinputfile')[0].files[0]);
			$.ajax({
				url:'/upload/image',
				type:'POST',
				data:data,
				cache: false,
				contentType: false,
				processData: false,
				success:function(d){
					var img = document.getElementById("newImg");
					img.src = "/resource/file/" + d;
				},
				error:function(){
					console.log("upload failed");
				}
			});
		});
	});
	
	$(document).ready(function(){
		var spotUuid = tbgUtil.getUrlParam("spotid");
		var spot = null;
		if(spotUuid){
			$.ajax({
	            url:'/expert/spot/'+spotUuid,
	            contentType:"application/json;charset=UTF-8"
	        }).done(function(data){
	        	spot = data;
	        	document.getElementById("spot-title-input").value = spot.name;
	        	document.getElementById("spot-days-input").value = spot.days || 0;
	        	document.getElementById("spot-cost-input").value = spot.cost || 0;
				var items = spot.items;
				if(!items){
					return;
				}
				var root = document.getElementById("article-content");
				root.innerHTML = "";
				items.map(function(item){
					var node = document.createElement("div");
					node.classList.add("article-item");
					node.dataset["type"] = item.type;
					var content = document.createElement("div");
					content.classList.add("article-item-content");
					content.dataset["type"] = item.type;
					node.appendChild(content);
					switch(item.type){
						case "title":
							node.classList.add("tbg-p-title");
							content.innerHTML = item.text;
							break;
						case "text":
							node.classList.add("tbg-p-text");
							content.innerHTML = item.text;
							break;
						case "tip":
							node.classList.add("tbg-p-tip");
							content.innerHTML = item.text;
							break;
						case "shop":
							node.classList.add("tbg-p-shop");
							content.innerHTML = item.text;
							break;
						case "food":
							node.classList.add("tbg-p-food");
							content.innerHTML = item.text;
							break;
						case "image":
							node.classList.add("tbg-p-image");
							var img = document.createElement("img");
							img.classList.add("tbg-article-img");
							img.src = item.src;
							content.appendChild(img);
							var p = document.createElement("p");
							p.classList.add("tbg-article-img-text");
							p.innerHTML = item.text;
							content.appendChild(p);
							break;
					}
					node.appendChild(createToolbar());
					root.appendChild(node);
				});
	        });
		}
		
		$( "#tbg-toolbar .draggable" ).draggable({
    		opacity: 0.7,
    		connectWith: ".droppable",
    		connectToSortable: ".droppable",
    		zIndex:100000,
    		cursor: "move",
    		helper: function() {
    			var type = this.dataset["type"];
	            var img = $(this).clone();
	            var div = $('<div class="article-item" data-type="'+type+'"></div>').append(img);
	            return $(div);
	        }
		});
		$( ".sortable" ).sortable({
    		opacity: 0.7,
    		connectWith: ".droppable",
    		receive: onDrop
		});
		$(".toolbar-item").click(function(evt){
			var ele = evt.target;
			while(!ele.classList.contains("toolbar-item")){
				ele = ele.parentNode;
			}
			var type = ele.dataset["type"];
			var node = document.createElement("div");
			node.classList.add("article-item");
			node.dataset["type"] = type;
			node.innerHTML = "";
			document.getElementById("article-content").appendChild(node);
			addItem(type, node);
		});
		
		function onDrop(evt){
	    	//var ele = evt.toElement;
			var ele=evt.originalEvent.target;
			while(!ele.classList.contains("article-item")){
				ele = ele.parentNode;
			}
			ele.innerHTML = "";
			ele.removeAttribute("style");
	    	var type = ele.dataset["type"];
	    	addItem(type, ele);
		}
		
		function addItem(type, node){
			switch(type){
	    		case "text":
	    			showText(node,"", "new");
	    			break;
	    		case "title":
	    			showTitle(node,"", "new");
	    			break;
	    		case "image":
	    			showImage(node,"/img/default.png","", "new");
	    			break;
	    		case "tip":
	    			showTip(node,"", "new");
	    			break;
	    		case "shop":
	    			showShop(node,"", "new");
	    			break;
	    		case "food":
	    			showFood(node,"", "new");
	    			break;
	    	}
		}
		
		function editItem(node){
			var type = node.dataset["type"];
			switch(type){
	    		case "text":
	    			var text = node.firstElementChild.innerHTML;
	    			showText(node,text, "edit");
	    			break;
	    		case "title":
	    			var title = node.firstElementChild.innerHTML;
	    			showTitle(node,title, "edit");
	    			break;
	    		case "image":
	    			var img = node.firstElementChild.firstElementChild;
	    			var p = img.nextSibling;
	    			showImage(node, img.getAttribute("src"), p.innerHTML, "edit");
	    			break;
	    		case "tip":
	    			var tip = node.firstElementChild.innerHTML;
	    			showTip(node,tip, "edit");
	    			break;
	    		case "shop":
	    			var shop = node.firstElementChild.innerHTML;
	    			showShop(node,shop, "edit");
	    			break;
	    		case "food":
	    			var food = node.firstElementChild.innerHTML;
	    			showFood(node,food, "edit");
	    			break;
	    	}
		}
		
		function showTitle(node,val, mode){
			$("#newTitleText").val(val);
			$("#titleModal").modal("show");
			$("#titleModal").data("node", node);
			$("#titleModal").data("mode", mode);
		}
		
		function showText(node,val, mode){
			//$("#newTipArea").val(val);
			$('#newTipArea').summernote('code', val);
			$("#tipModalTitle").html("文字段落");
			$("#tipModal").modal("show");
			$("#tipModal").data("node", node);
			$("#tipModal").data("type", "text");
			$("#tipModal").data("mode", mode);
		}
		
		function showTip(node,val, mode){
			//$("#newTipArea").val(val);
			$('#newTipArea').summernote('code', val);
			$("#tipModalTitle").html("旅行小贴士");
			$("#tipModal").modal("show");
			$("#tipModal").data("node", node);
			$("#tipModal").data("type", "tip");
			$("#tipModal").data("mode", mode);
		}
		
		function showShop(node,val, mode){
			//$("#newTipArea").val(val);
			$('#newTipArea').summernote('code', val);
			$("#tipModalTitle").html("购物推荐");
			$("#tipModal").modal("show");
			$("#tipModal").data("node", node);
			$("#tipModal").data("type", "shop");
			$("#tipModal").data("mode", mode);
		}
		
		function showFood(node,val, mode){
			//$("#newTipArea").val(val);
			$('#newTipArea').summernote('code', val);
			$("#tipModalTitle").html("美食推荐");
			$("#tipModal").modal("show");
			$("#tipModal").data("node", node);
			$("#tipModal").data("type", "food");
			$("#tipModal").data("mode", mode);
		}
		
		function showImage(node,src,val, mode){
			$("#newImg").attr("src", src);
			$("#newImageText").val(val);
			$("#imageModal").modal("show");
			$("#imageModal").data("node", node);
			$("#imageModal").data("mode", mode);
		}
		
		function createToolbar(){
			var bar = document.createElement("span");
			bar.classList.add("article-item-toolbar");
			bar.innerHTML = '<a class="toolbar-button" href="#" data-action="edit" title="编辑"><i class="fa fa-pencil"></i></a>' +
							'<a class="toolbar-button" href="#" data-action="delete" title="删除"><i class="fa fa-trash-o"></i></a>';
			return bar;
		}
		
		$("#newTitleBtn").click(function(){
			var node = $("#titleModal").data("node");
			var mode = $("#titleModal").data("mode");
			var title = $("#newTitleText").val();
			if(mode == "new"){
				node.innerHTML = "";
				var content = document.createElement("div");
				content.innerHTML = title;
				content.classList.add("article-item-content");
				content.dataset["type"] = "title";
				node.appendChild(content);
				node.appendChild(createToolbar());
				node.classList.add("tbg-p-title");
			}
			else{
				node.firstElementChild.innerHTML = title;
			}
			$("#titleModal").modal("hide");
		});
		$("#newTitleCancelBtn").click(function(){
			var node = $("#titleModal").data("node");
			var mode = $("#titleModal").data("mode");
			if(mode == "new"){
				node.parentNode.removeChild(node);
			}
		});
		
		$("#newTextBtn").click(function(){
			var node = $("#textModal").data("node");
			var mode = $("#textModal").data("mode");
			var title = $("#newTextArea").val();
			if(mode == "new"){
				node.innerHTML = "";
				var content = document.createElement("div");
				content.innerHTML = title;
				content.classList.add("article-item-content");
				content.dataset["type"] = "text";
				node.appendChild(content);
				node.appendChild(createToolbar());
				node.classList.add("tbg-p-text");
			}
			else{
				node.firstElementChild.innerHTML = title;
			}
			$("#textModal").modal("hide");
		});
		$("#newTextCancelBtn").click(function(){
			var node = $("#textModal").data("node");
			var mode = $("#textModal").data("mode");
			if(mode == "new"){
				node.parentNode.removeChild(node);
			}
		});
		
		$("#newTipBtn").click(function(){
			var node = $("#tipModal").data("node");
			var mode = $("#tipModal").data("mode");
			var title = $("#newTipArea").val();
			var type = $("#tipModal").data("type");
			if(mode == "new"){
				node.innerHTML = "";
				var content = document.createElement("div");
				content.innerHTML = title;
				content.classList.add("article-item-content");
				content.dataset["type"] = type;
				node.appendChild(content);
				node.appendChild(createToolbar());
				node.classList.add("tbg-p-" + type);
			}
			else{
				node.firstElementChild.innerHTML = title;
			}
			$("#tipModal").modal("hide");
		});
		$("#newTipCancelBtn").click(function(){
			var node = $("#tipModal").data("node");
			var mode = $("#tipModal").data("mode");
			if(mode == "new"){
				node.parentNode.removeChild(node);
			}
		});
		
		$("#newImageBtn").click(function(){
			var node = $("#imageModal").data("node");
			var mode = $("#imageModal").data("mode");
			var title = $("#newImageText").val();
			var src = $("#newImg").attr("src");
			if(mode == "new"){
				node.innerHTML = "";
				var content = document.createElement("div");
				content.classList.add("article-item-content");
				content.dataset["type"] = "image";
				var img = document.createElement("img");
				img.setAttribute("src", src);
				img.classList.add("tbg-article-img");
				content.appendChild(img);
				var p = document.createElement("p");
				p.innerHTML = title;
				p.classList.add("tbg-article-img-text");
				content.appendChild(p);
				node.appendChild(content);
				node.appendChild(createToolbar());
				node.classList.add("tbg-p-image");
			}
			else{
				var img = node.firstElementChild.firstElementChild;
				img.setAttribute("src", src);
				var p = img.nextSibling;
				p.innerHTML = title;
			}
			$("#imageModal").modal("hide");
		});
		$("#newImageCancelBtn").click(function(){
			var node = $("#imageModal").data("node");
			var mode = $("#imageModal").data("mode");
			if(mode == "new"){
				node.parentNode.removeChild(node);
			}
		});
		
		$("#article-content").on("click", ".toolbar-button", function(evt){
			var node = evt.originalEvent.target;
			while(node && node.tagName != "A"){
				node = node.parentNode;
			}
			var action = node.dataset["action"];
			while(!node.classList.contains("article-item")){
				node = node.parentNode;
			}
			if(action == "edit"){
				editItem(node);
			}
			else if(action == "delete"){
				removeParagraph(node);
			}
		});
		
		function removeParagraph(node){
			swal({
                title: "确定删除?",
                text: "如果是手误，请取消哦!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "删除",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                	node.parentNode.removeChild(node);
                    tbgUtil.showMessage("success", "成功删除");
                } else {
                    //swal("Cancelled", "Your imaginary file is safe :)", "error");
                	//tbgUtil.showMessage("error", "抱歉，出错了！");
                }
            });
		}
		
		$("#tbgSaveBtn").click(function(){
			var spot = beforeSave();
			console.log(spot);
			saveArticle(spot);
		});
		
		function beforeSave(){
			if(spotUuid == null){
				//spotUuid = "S"+tbgUtil.getUuid();
			}
			var today = tbgUtil.getToday();
			var items = [];
			var idx = 1;
			$(".article-item-content").each(function(){
				var item = {
					uuid: "S"+tbgUtil.getUuid() + idx,
					status: "P",
					index: idx,
					bookUuid: spotUuid,
					date: today
				};
			    var type = this.dataset["type"];
			    item.type = type;
			    if(type == "image"){
			    	item.src = this.children[0].src;
			    	item.text = this.children[1].innerHTML;
			    }
			    else{
			    	item.text = this.innerHTML;
			    }
			    items.push(item);
			    idx++;
			 });
			if(spot){
				spot.items = items;
				spot.name = document.getElementById("spot-title-input").value;
				spot.days = document.getElementById("spot-days-input").value;
				spot.cost = document.getElementById("spot-cost-input").value;
			}
			else{
				spot = {
						uuid: spotUuid,
						name: document.getElementById("spot-title-input").value,
						days: document.getElementById("spot-days-input").value,
						cost: document.getElementById("spot-cost-input").value,
						items: items,
						dateCreated: today
					};
			}
			return spot;
		}
		
		function saveArticle(spot){
			$.ajax({
				url:'/expert/spot/save',
				type:'POST',
				data: JSON.stringify(spot),
				contentType:"application/json;charset=UTF-8",
				success:function(d){
					console.log(d);
					spotUuid = d.uuid;
					tbgUtil.showMessage("success", "您的行程已经永久保存");
				},
				error:function(){
					console.log("upload failed");
					tbgUtil.showMessage("error", "抱歉，暂时无法保存");
				}
			});
		}
	});
});