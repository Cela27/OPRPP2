<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<%
	String color="blue";
	Object boja= request.getSession().getAttribute("Color");
	
	if(boja==null)
		boja="white";
	%>
   <body style='background-color: <%= boja %>;'>
   
   	<h1>OS usage</h1>
   	 <p>Here are the results of OS usage in survey that we completed.”</p>
   	 
   	 <img src="http://localhost:8080/webapp2/reportImage" />
   </body>
</html>