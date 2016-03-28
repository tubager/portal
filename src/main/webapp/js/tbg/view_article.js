$(document).ready(function(){
	var articleUuid = tbgUtil.getUrlParam("articleid");
	var article = null;
	if(!articleUuid){
		return;
	}
	$.ajax({
        url:'/readarticle/'+articleUuid,
        contentType:"application/json;charset=UTF-8"
    }).done(function(data){
    	article = data;
    	$("#article-title").html(article.title);
		var items = article.items;
		if(!items){
			return;
		}
		var root = document.getElementById("article-content");
		root.innerHTML = "";
		items.map(function(item){
			var node = document.createElement("div");
			node.classList.add("article-item");
			var content = document.createElement("div");
			content.classList.add("article-item-content");
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
			root.appendChild(node);
		});
    }.bind(this));
});