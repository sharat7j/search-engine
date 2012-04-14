var query="";
var iframe; 
var result=""; 

function doSearch(query){
	result="";
	
	var url="doSearch?q="+query;
	$.getJSON(url, function(data) {

	})
	.success(function(data) {
		var i=1;
		$.each(data, function(key, val) {
			result+="r"+i+"="+key+"&";
			i++;
			
		});
		$(".current").removeAttr("class");
		$("#ourTab").addClass("current");
		iframe = document.getElementById('frmContent');
		iframe.src = "queryresults.jsp?"+result;
		iframe.src = iframe.src;
	});

}

$(function(){
	iframe= document.getElementById('frmContent');

	$("li").click(function(){
		$(".current").removeAttr("class");
		$(this).addClass("current");
	});

	$("#queryTab").click(function(){
		//alert("Google clicked");
		iframe = document.getElementById('frmContent');
		iframe.src = "utd.html";
		iframe.src = iframe.src;
	});
	
	$("#ourTab").click(function(){
		iframe = document.getElementById('frmContent');
		iframe.src = "queryresults.jsp?"+result;
		iframe.src = iframe.src;
	});
	
	$("#googleTab").click(function(){
		//alert("Google clicked");
		iframe = document.getElementById('frmContent');
		iframe.src = "http://www.google.com/cse?cx=013269018370076798483%3A8eec3papwpi&ie=UTF-8&q="+query+"#gsc.tab=0&gsc.q="+query+"&gsc.page=1";
		iframe.src = iframe.src;
		
	});

	$("#bingTab").click(function(){
		//alert("Bing clicked");
		iframe = document.getElementById('frmContent');
		iframe.src = "http://www.bing.com/search?q="+query;
		iframe.src = iframe.src;
	});
	
});


function setQuery(queryString){
	query=queryString;
}