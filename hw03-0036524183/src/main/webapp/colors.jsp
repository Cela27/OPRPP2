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
   
   	<h1>Odaberi boju pozadine</h1>
	 <a href="/webapp2/setcolor?color=white">WHITE</a>
	 <a href="/webapp2/setcolor?color=red">RED</a>
	 <a href="/webapp2/setcolor?color=green">GREEN</a>
	 <a href="/webapp2/setcolor?color=cyan">CYAN</a>
	 
	 <p>Boja je <%= boja %> </p>
   </body>
</html>