<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<%
Object boja = request.getSession().getAttribute("Color");
if (boja == null)
	boja = "white";
%>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
</head>
<body style='background-color: <%=boja%>;'>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<tr>
			<th>Ime benda</th>
			<th>Broj glasova</th>
		</tr>
		<c:forEach var="zapis" items="${rezultati}">
			<tr>
				<td>${zapis.bend}</td>
				<td>${zapis.brojGlasova}</td>
			</tr>
		</c:forEach>
	</table>

	<h2>Grafički prikaz rezultata.</h2>

	<img src="http://localhost:8080/webapp2/glasanje-grafika" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u xls formatu dostupni su <a href="/webapp2/glasanje-xls">ovdje.</a>
	</p>

	<h2>Razno</h2>
	<ul>
		<c:forEach var="zapis" items="${pobjednici}">
		<li><a href='${zapis.link}'
			target="_blank">${zapis.bend}</a></li>
		</c:forEach>
	</ul>
</body>
</html>