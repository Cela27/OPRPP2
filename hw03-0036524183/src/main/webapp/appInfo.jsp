<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<%
String color = "blue";
Object boja = request.getSession().getAttribute("Color");

if (boja == null)
	boja = "white";

Object startTime= request.getServletContext().getAttribute("vrijeme");
long endTime= System.currentTimeMillis();

long timeElapsed = endTime - (Long)startTime;

long seconds = timeElapsed / 1000;
long minutes = seconds / 60;
long hours = minutes / 60;
long days = hours / 24;
long milis=timeElapsed%1000;
String time = days + " dana, " + hours % 24 + " sati, " + minutes % 60 + " minuta, " + seconds % 60+ " sekundi i "+ milis+" milisekundi."; 

%>
<body style='background-color: <%=boja%>;'>
	<p>Od inicijalizacije je prošlo: <%=time %></p>
</body>
</html>
