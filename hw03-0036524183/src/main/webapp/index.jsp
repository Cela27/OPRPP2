<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<%
String color = "blue";
Object boja = request.getSession().getAttribute("Color");

if (boja == null)
	boja = "white";
%>
<body style='background-color: <%=boja%>;'>
	<a href="colors.jsp">Background color chooser</a>
	<br>
	<a href="trigonometric?a=0&b=90">Link za trigonometriske funkcije s parametrima 0 i 90</a>

	<form action="trigonometric" method="GET">
		Početni kut:<br>
		<input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br>
		<input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset"
			value="Reset">
	</form>
	
	<br>
	<a href="/webapp2/stories/funny.jsp">Smjesna prica</a>
	
	<br>
	
	<a href="report.jsp">Report</a>
	
	<br>
	
	<a href="/webapp2/powers?a=1&b=100&n=3">Powers</a>
	
	<br>
	
	<a href="appInfo.jsp">Vrijeme trajanja aplikacije</a>
	<br>
	
	<a href="/webapp2/glasanje">Glasanje</a>
</body>
</html>