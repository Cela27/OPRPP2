<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<tr>
			<th>Option title</th>
			<th>Vote Count</th>
		</tr>
		<c:forEach var="unos" items="${unosi}">
			<tr>
				<td>${unos.optionTitle}</td>
				<td>${unos.glasovi}</td>
			</tr>
		</c:forEach>
	</table>

	<h2>Grafički prikaz rezultata.</h2>

	<img src="http://localhost:8080/voting-app/servleti/glasanje-grafika?pollID=${unosi.get(0).pollID}" />

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u xls formatu dostupni su <a href="/voting-app/servleti/glasanje-xls?pollID=${unosi.get(0).pollID}">ovdje.</a>
	</p>
	
	<h2>Razno</h2>
	<ul>
		<c:forEach var="unos" items="${pobjednici}">
		<li><a href='${unos.optionLink}'
			target="_blank">${unos.optionTitle}</a></li>
		</c:forEach>
	</ul>
</body>
</html>