var tbgUtil = {
	getUuid: function(){
		var d = new Date();
		return (new Date(d.getTime() - d.getTimezoneOffset()*60*1000)).toJSON().replace(/-/g,"").replace(/:/g,"").replace(".","") + Math.ceil(Math.random()*100);
	},
	
	getToday: function(){
		var d = new Date();
		return (new Date(d.getTime() - d.getTimezoneOffset()*60*1000)).toJSON().split("T")[0];
	}
};