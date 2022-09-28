<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
   
	<body>
   	<h1>Choose your poll:</h1>
	
	<ul>
		<c:forEach var="unos" items="${unosi}">
		<li><a href="/voting-app/servleti/glasanje?pollID=${unos.id}">${unos.title}</a>:${unos.message}</li>
		</c:forEach>
	</ul>
</body>
</html>