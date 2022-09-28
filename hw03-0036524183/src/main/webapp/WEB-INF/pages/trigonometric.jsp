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
	<p>Evo traženih rezultata.</p>

	<table border="1">
		<tr>
			<th>Broj</th>
			<th>Njegov sinus</th>
			<th>Njegov cosinus</th>
		</tr>
		<c:forEach var="zapis" items="${rezultati}">
			<tr>
				<td>${zapis.broj}</td>
				<td>${zapis.sin}</td>
				<td>${zapis.cos}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>