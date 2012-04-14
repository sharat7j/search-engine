<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>UTD Search Engine</title>
</head>
<body>

<%

for(int i=1;i<=10;i++){
	if(request.getParameter("r"+i)!=null){
		out.println("<h3> <a href=\""+request.getParameter("r"+i)+"\" id=\"url1\">"+request.getParameter("r"+i)+"</a> </h3> <br>");
	}
}



%>

</body>
</html>