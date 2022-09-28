<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>

<body'>
	<h1>Glasaj!:</h1>
	
	<ol>
		<c:forEach var="unos" items="${unosi}">
		<li><a href='/voting-app/servleti/glas?pollID=${unos.pollID}&id=${unos.id}'>${unos.optionTitle}</a></li>
		</c:forEach>
	</ol>
	
</body>
</html>