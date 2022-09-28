<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<%
Object boja = request.getSession().getAttribute("Color");
if (boja == null)
	boja = "white";
%>

<body style='background-color: <%=boja%>;'>
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedečih bendova koji vam je najdraži, kliknite na link kako biste glasali.</p>
	
	<c:forEach var="zapis" items="${sudionici}">
		<p>${zapis.broj}. 
			<a href='/webapp2/glasanje-glasaj?id=${zapis.broj}'>${zapis.ime}</a>
		</p>
	</c:forEach>
	
</body>
</html>